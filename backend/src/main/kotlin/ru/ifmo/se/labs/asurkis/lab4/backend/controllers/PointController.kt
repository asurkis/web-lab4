package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.PointAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.data.userId
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ForbiddenException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.PointNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository

@RestController
class PointController(val pointRepository: PointRepository,
                      val pointAssembler: PointAssembler) {
    @GetMapping("/points/{id}")
    fun one(@PathVariable id: Long, @AuthenticationPrincipal user: User): EntityModel<Point> {
        val point = pointRepository.findById(id).orElseThrow { PointNotFoundException(id) }
        if (point.userId != user.id) {
            throw ForbiddenException()
        }
        return pointAssembler.toModel(point)
    }

    @GetMapping("/points")
    fun all(@AuthenticationPrincipal user: User): CollectionModel<EntityModel<Point>> {
        val list = pointRepository.findByUserId(user.id).map { pointAssembler.toModel(it) }
        return CollectionModel(list,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PointController::class.java).all(user)).withRel("points"))
    }
}
