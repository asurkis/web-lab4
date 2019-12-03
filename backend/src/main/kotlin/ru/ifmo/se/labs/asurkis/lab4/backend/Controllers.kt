package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class UserController(val userRepository: UserRepository,
                     val userAssembler: UserAssembler) {
    @ResponseBody
    @CrossOrigin("http://localhost:4200")
    @GetMapping("/users/{id}")
    fun one(@PathVariable id: Long): EntityModel<User> {
        val user = userRepository.findById(id)
        if (!user.isPresent) {
            throw UserNotFoundException(id)
        }
        return userAssembler.toModel(user.get())
    }

    @ResponseBody
    @CrossOrigin("http://localhost:4200")
    @GetMapping("/users")
    fun all(): CollectionModel<EntityModel<User>> {
        val list = userRepository.findAll().map { userAssembler.toModel(it!!) }
        return CollectionModel(list,
                linkTo(methodOn(javaClass).all()).withRel("users"))
    }

    @RequestMapping("/user")
    fun user(user: Principal?) = user
}

@RestController
class PointController(val pointRepository: PointRepository,
                      val pointAssembler: PointAssembler) {
    @ResponseBody
    @CrossOrigin("http://localhost:4200")
    @GetMapping("/points/{id}")
    fun one(@PathVariable id: Long): EntityModel<Point> {
        val point = pointRepository.findById(id).orElseThrow { PointNotFoundException(id) }
        return pointAssembler.toModel(point)
    }

    @ResponseBody
    @CrossOrigin("http://localhost:4200")
    @GetMapping("/points")
    fun all(): CollectionModel<EntityModel<Point>> {
        val list = pointRepository.findAll().map { pointAssembler.toModel(it) }
        return CollectionModel(list,
                linkTo(methodOn(PointController::class.java).all()).withRel("points"))
    }
}

@RestController
class ResultController(val resultRepository: ResultRepository,
                       val resultAssembler: ResultAssembler) {
    @ResponseBody
    @CrossOrigin("http://localhost:4200")
    @GetMapping("/results/{id}")
    fun one(@PathVariable id: Long): EntityModel<Result> {
        val result = resultRepository.findById(id).orElseThrow { ResultNotFoundException(id) }
        return resultAssembler.toModel(result)
    }

    @ResponseBody
    @CrossOrigin("http://localhost:4200")
    @GetMapping("/results")
    fun all(): CollectionModel<EntityModel<Result>> {
        val list = resultRepository.findAll().map { resultAssembler.toModel(it) }
        return CollectionModel(list,
                linkTo(methodOn(ResultController::class.java).all()).withRel("results"))
    }
}
