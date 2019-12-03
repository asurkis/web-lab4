package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.web.bind.annotation.*
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.PointAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.PointNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository

@RestController
class PointController(val pointRepository: PointRepository,
                      val pointAssembler: PointAssembler) {
    @GetMapping("/points/{id}")
    fun one(@PathVariable id: Long): EntityModel<Point> {
        val point = pointRepository.findById(id).orElseThrow { PointNotFoundException(id) }
        return pointAssembler.toModel(point)
    }

    @GetMapping("/points")
    fun all(): CollectionModel<EntityModel<Point>> {
        val list = pointRepository.findAll().map { pointAssembler.toModel(it) }
        return CollectionModel(list,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PointController::class.java).all()).withRel("points"))
    }
}
