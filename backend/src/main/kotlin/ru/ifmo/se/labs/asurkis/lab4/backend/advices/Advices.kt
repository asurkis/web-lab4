package ru.ifmo.se.labs.asurkis.lab4.backend.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.*

@ControllerAdvice
class Advices {
    @ResponseBody
    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun userNotFoundHandler(e: UserNotFoundException) = e.message

    @ResponseBody
    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun unauthorizedHandler(e: UnauthorizedException) = e.message

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun userAlreadyExists(e: InvalidPasswordException) = e.message

    @ResponseBody
    @ExceptionHandler(InvalidPasswordException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun invalidPasswordHandler(e: InvalidPasswordException) = e.message

    @ResponseBody
    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun forbiddenHandler(e: ForbiddenException) = e.message

    @ResponseBody
    @ExceptionHandler(PointNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun pointNotFoundHandler(e: PointNotFoundException) = e.message

    @ResponseBody
    @ExceptionHandler(ResultNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun resultNotFoundHandler(e: ResultNotFoundException) = e.message
}
