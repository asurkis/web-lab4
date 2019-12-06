package ru.ifmo.se.labs.asurkis.lab4.backend.forms

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class UserForm(
        @NotEmpty
        var username: String,
        @NotEmpty
        var password: String)

class RequestForm(
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

class ResultChangeForm(
        @NotNull
        var id: Long,
        @NotNull
        @Min(0)
        @Max(4)
        var radius: Double)
