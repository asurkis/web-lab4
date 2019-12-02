package ru.ifmo.se.labs.asurkis.lab4.backend

class UserNotFoundException(id: Long) : Exception("User not found: $id")
class UnauthorizedException : Exception("Not logged in")
class ForbiddenException : Exception("User doesn't have access to this resource")
class PointNotFoundException(id: Long) : Exception("Point not found: $id")
class ResultNotFoundException(id: Long) : Exception("Result not found: $id")
