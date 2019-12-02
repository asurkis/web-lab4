package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long>

interface PointRepository: CrudRepository<Point, Long> {
    fun findByUserId(id: Long): Iterable<Point>
    fun findByUser(user: User): Iterable<Point>
}

interface ResultRepository: CrudRepository<Result, Long> {
    fun findByPointUserId(id: Long): Iterable<Result>
    fun findByPointUser(user: User): Iterable<Result>
}
