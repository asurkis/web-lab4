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
    @ResponseBody
    @GetMapping("/users/{id}")
    fun one(@PathVariable id: Long): EntityModel<User> {
        val user = userRepository.findById(id)
        if (!user.isPresent) {
            throw UserNotFoundException(id)
        }
        return userAssembler.toModel(user.get())
    }

    @ResponseBody
    @GetMapping("/users")
    fun all(): CollectionModel<EntityModel<User>> {
        val list = userRepository.findAll().map { userAssembler.toModel(it!!) }
        return CollectionModel(list,
                linkTo(methodOn(javaClass).all()).withRel("users"))
    }

    @ResponseBody
    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): String {
        val user = userRepository.findByName(username)
        val hash = generateHash(password)

        if (!user.isPresent) {
            val newUser = User(name = username, passwordHash = hash)
            userRepository.save(newUser)
            return "${newUser.id}"
        }

        if (user.get().passwordHash == hash) {
            return "${user.get().id}"
        } else {
            throw InvalidPasswordException()
        }
    }
}

@CrossOrigin("localhost:4200")
@RestController
class PointController(val pointRepository: PointRepository,
                      val pointAssembler: PointAssembler) {
    @ResponseBody
    @GetMapping("/points/{id}")
    fun one(@CookieValue userId: Optional<Long>, @PathVariable id: Long): EntityModel<Point> {
        userId.orElseThrow { UnauthorizedException() }
        val point = pointRepository.findById(id).orElseThrow { PointNotFoundException(id) }
        if (point.userId != userId.get()) {
            throw ForbiddenException()
        }
        return pointAssembler.toModel(point)
    }

    @ResponseBody
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
    @ResponseBody
    @GetMapping("/results/{id}")
    fun one(@CookieValue userId: Optional<Long>, @PathVariable id: Long): EntityModel<Result> {
        userId.orElseThrow { UnauthorizedException() }
        val result = resultRepository.findById(id).orElseThrow { ResultNotFoundException(id) }
        if (result.point.userId != userId.get()) {
            throw ForbiddenException()
        }
        return resultAssembler.toModel(result)
    }

    @ResponseBody
    @GetMapping("/results")
    fun all(@CookieValue userId: Optional<Long>): CollectionModel<EntityModel<Result>> {
        userId.orElseThrow { UnauthorizedException() }
        val list = resultRepository.findByPointUserId(userId.get()).map { resultAssembler.toModel(it) }
        return CollectionModel(list,
                linkTo(methodOn(ResultController::class.java).all(userId)).withRel("results"))
    }
}
