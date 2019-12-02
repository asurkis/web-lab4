package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin("localhost:4200")
@RestController
class UserController(val userRepository: UserRepository,
                     val userAssembler: UserAssembler) {
    @GetMapping("/users/{id}")
    fun one(@PathVariable id: Long): EntityModel<User> {
        val user = userRepository.findById(id)
        if (!user.isPresent) {
            throw UserNotFoundException(id)
        }
        return userAssembler.toModel(user.get())
    }

    @GetMapping("/users")
    fun all(): CollectionModel<EntityModel<User>> {
        val list = userRepository.findAll().map { userAssembler.toModel(it!!) }
        return CollectionModel(list,
                linkTo(methodOn(javaClass).all()).withRel("users"))
    }

    @GetMapping("/login")
    fun login() {
        TODO()
    }
}

@CrossOrigin("localhost:4200")
@RestController
class PointController(val pointRepository: PointRepository,
                      val pointAssembler: PointAssembler) {
    @GetMapping("/points/{id}")
    fun one(@CookieValue userId: Optional<Long>, @PathVariable id: Long): EntityModel<Point> {
        userId.orElseThrow { UnauthorizedException() }
        val point = pointRepository.findById(id).orElseThrow { PointNotFoundException(id) }
        if (point.userId != userId.get()) {
            throw ForbiddenException()
        }
        return pointAssembler.toModel(point)
    }

    @GetMapping("/points")
    fun all(@CookieValue userId: Optional<Long>): CollectionModel<EntityModel<Point>> {
        userId.orElseThrow { UnauthorizedException() }
        val list = pointRepository.findByUserId(userId.get()).map { pointAssembler.toModel(it) }
        return CollectionModel(list,
                linkTo(methodOn(PointController::class.java).all(userId)).withRel("points"))
    }
}

@CrossOrigin("localhost:4200")
@RestController
class ResultController(val resultRepository: ResultRepository,
                       val resultAssembler: ResultAssembler) {
    @GetMapping("/results/{id}")
    fun one(@CookieValue userId: Optional<Long>, @PathVariable id: Long): EntityModel<Result> {
        userId.orElseThrow { UnauthorizedException() }
        val result = resultRepository.findById(id).orElseThrow { ResultNotFoundException(id) }
        if (result.point.userId != userId.get()) {
            throw ForbiddenException()
        }
        return resultAssembler.toModel(result)
    }

    @GetMapping("/results")
    fun all(@CookieValue userId: Optional<Long>): CollectionModel<EntityModel<Result>> {
        userId.orElseThrow { UnauthorizedException() }
        val list = resultRepository.findByPointUserId(userId.get()).map { resultAssembler.toModel(it) }
        return CollectionModel(list,
                linkTo(methodOn(ResultController::class.java).all(userId)).withRel("results"))
    }
}
