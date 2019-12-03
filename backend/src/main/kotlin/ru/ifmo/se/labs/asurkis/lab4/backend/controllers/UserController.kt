package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.*
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.UserAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository

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
}
