package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.ResultAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.data.userId
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ForbiddenException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ResultNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository

@RestController
class ResultController(val resultRepository: ResultRepository,
                       val resultAssembler: ResultAssembler) {
    @GetMapping("/results/{id}")
    fun one(@PathVariable id: Long, @AuthenticationPrincipal user: User): EntityModel<Result> {
        val result = resultRepository.findById(id).orElseThrow { ResultNotFoundException(id) }
        if (result.userId != user.id) {
            throw ForbiddenException()
        }
        return resultAssembler.toModel(result)
    }

    @GetMapping("/results")
    fun all(@AuthenticationPrincipal user: User): CollectionModel<EntityModel<Result>> {
        val list = resultRepository.findByPointUser(user).map { resultAssembler.toModel(it) }
        return CollectionModel(list,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResultController::class.java).all(user)).withRel("results"))
    }
}
