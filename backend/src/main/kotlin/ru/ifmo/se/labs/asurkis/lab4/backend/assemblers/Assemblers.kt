package ru.ifmo.se.labs.asurkis.lab4.backend.assemblers

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import ru.ifmo.se.labs.asurkis.lab4.backend.controllers.PointController
import ru.ifmo.se.labs.asurkis.lab4.backend.controllers.ResultController
import ru.ifmo.se.labs.asurkis.lab4.backend.controllers.UserController
import ru.ifmo.se.labs.asurkis.lab4.backend.data.*

@Component
class PointAssembler: RepresentationModelAssembler<Point, EntityModel<Point>> {
    override fun toModel(point: Point): EntityModel<Point> {
        return EntityModel(point,
                linkTo(methodOn(PointController::class.java).one(point.id, point.user)).withSelfRel(),
                linkTo(methodOn(PointController::class.java).all(point.user)).withRel("all"))
    }
}

@Component
class ResultAssembler: RepresentationModelAssembler<Result, EntityModel<Result>> {
    override fun toModel(result: Result): EntityModel<Result> {
        return EntityModel(result,
                linkTo(methodOn(ResultController::class.java).one(result.id, result.user)).withSelfRel(),
                linkTo(methodOn(ResultController::class.java).all(result.user)).withRel("all"),
                linkTo(methodOn(PointController::class.java).one(result.pointId, result.user)).withRel("point"))
    }
}
