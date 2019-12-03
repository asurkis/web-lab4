package ru.ifmo.se.labs.asurkis.lab4.backend.exceptions

class UserNotFoundException : Exception {
    constructor(id: Long) : super("User $id not found")
    constructor(name: String) : super("User $name not found")
}
class UnauthorizedException : Exception("Not logged in")
class UserAlreadyExistsException(name: String) : Exception("User $name exists")
class InvalidPasswordException : Exception("Invalid password")
class ForbiddenException : Exception("User doesn't have access to this resource")
class PointNotFoundException(id: Long) : Exception("Point not found: $id")
class ResultNotFoundException(id: Long) : Exception("Result not found: $id")
