package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAssembler: RepresentationModelAssembler<User, EntityModel<User>> {
    override fun toModel(user: User): EntityModel<User> {
        return EntityModel(user,
                linkTo(methodOn(UserController::class.java).one(user.id)).withSelfRel(),
                linkTo(methodOn(UserController::class.java).all()).withRel("users"))
    }
}

@Component
class PointAssembler: RepresentationModelAssembler<Point, EntityModel<Point>> {
    override fun toModel(point: Point): EntityModel<Point> {
        return EntityModel(point,
                linkTo(methodOn(PointController::class.java).one(Optional.of(point.user.id), point.id)).withSelfRel(),
                linkTo(methodOn(PointController::class.java).all(Optional.of(point.user.id))).withRel("points"))
    }
}

@Component
class ResultAssembler: RepresentationModelAssembler<Result, EntityModel<Result>> {
    override fun toModel(result: Result): EntityModel<Result> {
        return EntityModel(result,
                linkTo(methodOn(ResultController::class.java).one(Optional.of(result.point.user.id), result.id)).withSelfRel(),
                linkTo(methodOn(ResultController::class.java).all(Optional.of(result.point.user.id))).withRel("results"))
    }
}
