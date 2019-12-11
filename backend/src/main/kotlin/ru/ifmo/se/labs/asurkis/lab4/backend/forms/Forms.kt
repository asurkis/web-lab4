package ru.ifmo.se.labs.asurkis.lab4.backend.forms

import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserForm(
        @get:NotBlank
        var username: String?,
        @get:NotBlank
        var password: String?)

data class RequestForm(
        @get:NotNull
        @get:Min(-4)
        @get:Max(4)
        var x: Double?,
        @get:NotNull
        @get:Min(-5)
        @get:Max(3)
        var y: Double?,
        @get:NotNull
        @get:Min(0)
        @get:Max(4)
        var rs: List<Double?>?)

data class MultipleRequestsForm(
        @get:NotNull
        var items: Iterable<@NotNull @Valid RequestForm?>?)

data class SingleResultChangeForm(
        @get:NotNull
        var id: Long?,
        @get:NotNull
        var toDelete: Boolean?,
        @get:NotNull
        @get:Min(0)
        @get:Max(4)
        var radius: Double?)

data class MultipleResultChangeForm(
        @get:NotNull
        var items: Iterable<@NotNull @Valid SingleResultChangeForm?>?)
