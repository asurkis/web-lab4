package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.web.bind.annotation.*
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.assemblers.ResultAssembler
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.ResultNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository

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
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResultController::class.java).all()).withRel("results"))
    }
}
