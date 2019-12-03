package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.*
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.UserAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserAlreadyExistsException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.UserForm
import ru.ifmo.se.labs.asurkis.lab4.backend.generateHash
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.services.UserService
import java.util.*
import javax.servlet.http.HttpSession

@RestController
class UserController(val userRepository: UserRepository,
                     val userAssembler: UserAssembler,
                     val userService: UserService) {
    @GetMapping("/token")
    fun token(session: HttpSession) = Collections.singletonMap("token", session.id)

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

    @PostMapping("/register")
    fun register(@RequestParam username: String, @RequestParam password: String): EntityModel<User> {
        return userAssembler.toModel(userService.registerNew(UserForm(username, password)))
    }

//    @PostMapping("/login")
//    fun login(username: String, password: String): EntityModel<User> {
//        val user = userRepository.findByName(username).orElseThrow { UserNotFoundException(username) }
//        if (user.passwordHash !== generateHash(password)) {
//            throw InvalidPasswordException()
//        }
//        return userAssembler.toModel(user)
//    }
//
    @GetMapping("/logout")
    fun logout() {

    }
}
