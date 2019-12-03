package ru.ifmo.se.labs.asurkis.lab4.backend.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import java.util.*

@Component
interface UserRepository: CrudRepository<User, Long> {
    fun findByName(name: String): Optional<User>
}

@Component
interface PointRepository: CrudRepository<Point, Long> {
    fun findByUserId(id: Long): Iterable<Point>
    fun findByUser(user: User): Iterable<Point>
}

@Component
interface ResultRepository: CrudRepository<Result, Long> {
    fun findByPointUserId(id: Long): Iterable<Result>
    fun findByPointUser(user: User): Iterable<Result>
}
