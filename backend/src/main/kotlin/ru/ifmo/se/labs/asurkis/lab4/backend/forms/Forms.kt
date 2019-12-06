package ru.ifmo.se.labs.asurkis.lab4.backend.forms

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UserForm(
        @NotEmpty
        var username: String,
        @NotEmpty
        var password: String)

data class RequestForm(
        @NotNull
        @Min(-4)
        @Max(4)
        var x: Double,
        @NotNull
        @Min(-5)
        @Max(3)
        var y: Double,
        @NotNull
        @Min(0)
        @Max(4)
        var rs: List<Double>)

data class ResultChangeForm(
        @NotNull
        var id: Long,
        var toDelete: Boolean,
        @NotNull
        @Min(0)
        @Max(4)
        var radius: Double)
