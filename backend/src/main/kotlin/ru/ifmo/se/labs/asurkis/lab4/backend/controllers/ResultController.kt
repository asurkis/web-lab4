package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.PointAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.ResultAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.*
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ForbiddenException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ResultNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.RequestForm
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.ResultChangeForm
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository
import javax.validation.Valid

@RestController
class ResultController(val pointRepository: PointRepository,
                       val pointAssembler: PointAssembler,
                       val resultRepository: ResultRepository,
                       val resultAssembler: ResultAssembler) {
    @GetMapping("/users/{userId}/points/{pointId}/results/{resultId}")
    fun one(@PathVariable userId: Long,
            @PathVariable pointId: Long,
            @PathVariable resultId: Long,
            @AuthenticationPrincipal user: User): EntityModel<Result> {
        val isAdmin = user.authorities.contains(Role("ADMIN"))
        if (!isAdmin && userId != user.id) {
            throw ForbiddenException()
        }
        val foundResult = resultRepository.findByIdAndPointIdAndPointUserId(resultId, pointId, userId).orElseThrow { ResultNotFoundException(resultId) }
        return resultAssembler.toModel(foundResult)
    }

    @GetMapping("/me/points/{pointId}/results/{resultId}")
    fun myOne(@PathVariable pointId: Long,
              @PathVariable resultId: Long,
              @AuthenticationPrincipal user: User) = one(user.id, pointId, resultId, user)

    @GetMapping("/users/{userId}/points/{pointId}/results")
    fun all(@PathVariable userId: Long,
            @PathVariable pointId: Long,
            @AuthenticationPrincipal user: User): CollectionModel<EntityModel<Result>> {
        val isAdmin = user.authorities.contains(Role("ADMIN"))
        if (!isAdmin && userId != user.id) {
            throw ForbiddenException()
        }
        val foundResults = resultRepository.findByPointIdAndPointUserId(pointId, userId)
        return CollectionModel(foundResults.map { resultAssembler.toModel(it) },
                linkTo(methodOn(ResultController::class.java).all(userId, pointId, user)).withSelfRel(),
                linkTo(methodOn(PointController::class.java).one(userId, pointId, user)).withRel("point"),
                linkTo(methodOn(UserController::class.java).one(userId, user)).withRel("owner"))
    }

    @GetMapping("/me/points/{pointId}/results")
    fun myAll(@PathVariable pointId: Long,
              @AuthenticationPrincipal user: User) = all(user.id, pointId, user)

    @Transactional
    @PostMapping("/add")
    fun addResults(@Valid @RequestBody requests: Iterable<RequestForm>,
                   @AuthenticationPrincipal user: User): CollectionModel<EntityModel<Point>> {
        val points = requests.map {
            val newPoint = Point(user = user, x = it.x, y = it.y)
            for (radius in it.rs) {
                val newResult = Result(point = newPoint, radius = radius)
                pointRepository.save(newPoint)
                resultRepository.save(newResult)
            }
            newPoint
        }
        return CollectionModel(points.map { pointAssembler.toModel(it) },
                linkTo(methodOn(PointController::class.java).all(user.id, user)).withSelfRel(),
                linkTo(methodOn(UserController::class.java).one(user.id, user)).withRel("owner"))
    }

    @Transactional
    @PostMapping("/change")
    fun changeResults(@Valid @RequestBody changes: Iterable<ResultChangeForm>,
                      @AuthenticationPrincipal user: User) {
        val isAdmin = user.authorities.contains(Role("ADMIN"))

        for (change in changes) {
            val result = resultRepository.findById(change.id).orElseThrow { ResultNotFoundException(change.id) }
            if (!isAdmin && result.userId != user.id) {
                throw ForbiddenException()
            }
            if (change.toDelete) {
                resultRepository.deleteById(result.id)
            } else {
                result.radius = change.radius
                resultRepository.save(result)
            }
        }
        pointRepository.deleteEmpty()
    }
}
