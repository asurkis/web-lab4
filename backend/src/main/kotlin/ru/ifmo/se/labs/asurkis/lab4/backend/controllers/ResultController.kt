package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.ResultAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Role
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ForbiddenException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ResultNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository

@RestController
class ResultController(val userRepository: UserRepository,
                       val pointRepository: PointRepository,
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
}
