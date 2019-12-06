package ru.ifmo.se.labs.asurkis.lab4.backend.forms

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserForm(
        @NotNull
        @Size(min = 1, max = 255)
        var username: String,
        @NotNull
        @Size(min = 1, max = 255)
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
        var r: Double)

data class MultipleRequestsForm(
        @NotNull
        var requests: List<RequestForm>)
