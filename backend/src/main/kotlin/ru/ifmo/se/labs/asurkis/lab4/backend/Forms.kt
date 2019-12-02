package ru.ifmo.se.labs.asurkis.lab4.backend

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class LoginForm(var username: String,
                     var password: String)

data class RequestForm(
        @Min(-4)
        @Max(4)
        var x: Double,
        @Min(-5)
        @Max(3)
        var y: Double,
        @Min(-4)
        @Max(4)
        var r: Double)
