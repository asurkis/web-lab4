package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.UserAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Role
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.BindingResultException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ForbiddenException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.UserForm
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.services.UserService
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
class UserController(val userService: UserService,
                     val userRepository: UserRepository,
                     val userAssembler: UserAssembler,
                     val authenticationManager: AuthenticationManager) {
    @PostMapping("/register")
    fun register(@Valid userForm: UserForm,
                 bindingResult: BindingResult,
                 session: HttpSession) {
        if (bindingResult.hasErrors()) {
            throw BindingResultException(bindingResult.allErrors)
        }
        val user = userService.registerNew(userForm)
        val token = UsernamePasswordAuthenticationToken(user, userForm.password, user.authorities)
        authenticationManager.authenticate(token)
        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
        }
    }

    @PostMapping("/login")
    fun login(@Valid userForm: UserForm,
              bindingResult: BindingResult,
              session: HttpSession) {
        if (bindingResult.hasErrors()) {
            throw BindingResultException(bindingResult.allErrors)
        }
        val user = userService.loadUserByUsername(userForm.username)
        val token = UsernamePasswordAuthenticationToken(user, userForm.password, user.authorities)
        authenticationManager.authenticate(token)
        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
        }
    }

    @GetMapping("/users/{userId}")
    fun one(@PathVariable userId: Long,
            @AuthenticationPrincipal user: User): EntityModel<User> {
        val isAdmin = user.authorities.contains(Role("ADMIN"))
        if (!isAdmin && userId != user.id) {
            throw ForbiddenException()
        }
        val userFound = userRepository.findById(userId).orElseThrow { UserNotFoundException(userId) }
        return userAssembler.toModel(userFound)
    }

    @GetMapping("/users")
    fun all(@AuthenticationPrincipal user: User): CollectionModel<EntityModel<User>> {
        val isAdmin = user.authorities.contains(Role("ADMIN"))
        if (!isAdmin) {
            throw ForbiddenException()
        }
        return CollectionModel(userRepository.findAll().map { userAssembler.toModel(it) },
                linkTo(methodOn(UserController::class.java).all(User())).withSelfRel())
    }

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: User): EntityModel<User> {
        return userAssembler.toModel(user)
    }
}
