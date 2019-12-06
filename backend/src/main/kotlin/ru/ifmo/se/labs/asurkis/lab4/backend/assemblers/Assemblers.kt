package ru.ifmo.se.labs.asurkis.lab4.backend.assemblers

import org.springframework.hateoas.CollectionModel
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
class UserAssembler : RepresentationModelAssembler<User, EntityModel<User>> {
    override fun toModel(user: User): EntityModel<User> {
        return EntityModel(user,
                linkTo(methodOn(UserController::class.java).one(user.id, user)).withSelfRel(),
                linkTo(methodOn(UserController::class.java).all(user)).withRel("all"),
                linkTo(methodOn(PointController::class.java).all(user.id, user)).withRel("points"))
    }

}

@Component
class PointAssembler : RepresentationModelAssembler<Point, EntityModel<Point>> {
    override fun toModel(point: Point): EntityModel<Point> {
        return EntityModel(point,
                linkTo(methodOn(PointController::class.java).one(point.userId, point.id, point.user)).withSelfRel(),
                linkTo(methodOn(PointController::class.java).all(point.userId, point.user)).withRel("all"),
                linkTo(methodOn(UserController::class.java).one(point.userId, point.user)).withRel("owner"),
                linkTo(methodOn(ResultController::class.java).all(point.userId, point.id, point.user)).withRel("results"))
    }
}

@Component
class ResultAssembler : RepresentationModelAssembler<Result, EntityModel<Result>> {
    override fun toModel(result: Result): EntityModel<Result> {
        return EntityModel(result,
                linkTo(methodOn(ResultController::class.java).one(result.userId, result.pointId, result.id, result.user)).withSelfRel(),
                linkTo(methodOn(ResultController::class.java).all(result.userId, result.pointId, result.user)).withRel("all"),
                linkTo(methodOn(PointController::class.java).one(result.userId, result.pointId, result.user)).withRel("point"),
                linkTo(methodOn(UserController::class.java).one(result.userId, result.user)).withRel("owner"))
    }
}
