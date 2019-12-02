package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, Long> {
    fun findByName(name: String): Optional<User>
}

interface PointRepository: CrudRepository<Point, Long> {
    fun findByUserId(id: Long): Iterable<Point>
    fun findByUser(user: User): Iterable<Point>
}

interface ResultRepository: CrudRepository<Result, Long> {
    fun findByPointUserId(id: Long): Iterable<Result>
    fun findByPointUser(user: User): Iterable<Result>
}
