package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class UserAssembler: RepresentationModelAssembler<User, EntityModel<User>> {
    override fun toModel(user: User): EntityModel<User> {
        return EntityModel(user,
                linkTo(methodOn(UserController::class.java).one(user.id)).withSelfRel(),
                linkTo(methodOn(UserController::class.java).all()).withRel("all"))
    }
}

@Component
class PointAssembler: RepresentationModelAssembler<Point, EntityModel<Point>> {
    override fun toModel(point: Point): EntityModel<Point> {
        return EntityModel(point,
                linkTo(methodOn(PointController::class.java).one(point.id)).withSelfRel(),
                linkTo(methodOn(PointController::class.java).all()).withRel("all"),
                linkTo(methodOn(UserController::class.java).one(point.userId)).withRel("user"))
    }
}

@Component
class ResultAssembler: RepresentationModelAssembler<Result, EntityModel<Result>> {
    override fun toModel(result: Result): EntityModel<Result> {
        return EntityModel(result,
                linkTo(methodOn(ResultController::class.java).one(result.id)).withSelfRel(),
                linkTo(methodOn(ResultController::class.java).all()).withRel("all"),
                linkTo(methodOn(PointController::class.java).one(result.pointId)).withRel("point"),
                linkTo(methodOn(UserController::class.java).one(result.userId)).withRel("user"))
    }
}
