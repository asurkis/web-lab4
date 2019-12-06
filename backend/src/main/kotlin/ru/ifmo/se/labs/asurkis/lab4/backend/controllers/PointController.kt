package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.PointAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Role
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.data.userId
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ForbiddenException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.PointNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository

@RestController
class PointController(val pointRepository: PointRepository,
                      val pointAssembler: PointAssembler) {
    @GetMapping("/users/{userId}/points/{pointId}")
    fun one(@PathVariable userId: Long,
            @PathVariable pointId: Long,
            @AuthenticationPrincipal user: User): EntityModel<Point> {
        val isAdmin = user.authorities.contains(Role("ADMIN"))
        val foundPoint = pointRepository.findByIdAndUserId(pointId, userId).orElseThrow { PointNotFoundException(pointId) }
        if (!isAdmin && !(userId == user.id && foundPoint.userId == user.id)) {
            throw ForbiddenException()
        }
        return pointAssembler.toModel(foundPoint)
    }

    @GetMapping("/me/points/{pointId}")
    fun myOne(@PathVariable pointId: Long,
              @AuthenticationPrincipal user: User) = one(user.id, pointId, user)

    @GetMapping("/users/{userId}/points")
    fun all(@PathVariable userId: Long,
            @AuthenticationPrincipal user: User): CollectionModel<EntityModel<Point>> {
        val isAdmin = user.authorities.contains(Role("ADMIN"))
        val foundPoints = pointRepository.findByUserId(userId)
        if (!isAdmin && userId != user.id) {
            throw ForbiddenException()
        }
        return CollectionModel(foundPoints.map { pointAssembler.toModel(it) },
                linkTo(methodOn(PointController::class.java).all(userId, user)).withSelfRel(),
                linkTo(methodOn(UserController::class.java).one(userId, user)).withRel("owner"))
    }

    @GetMapping("/me/points")
    fun myAll(@AuthenticationPrincipal user: User) = all(user.id, user)
}
