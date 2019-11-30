package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.web.bind.annotation.CrossOrigin
import java.util.*

@CrossOrigin("localhost:4200")
@RepositoryRestResource(exported = true, collectionResourceRel = "users", path = "users")
interface UserRepository: Repository<User, Long> {
    fun save(u: User): User
    fun findById(id: Long): Optional<User>
    fun existsById(id: Long): Boolean
    fun deleteById(id: Long)
    fun delete(u: User)
}

@CrossOrigin("localhost:4200")
@RepositoryRestResource(exported = true, collectionResourceRel = "points", path = "points")
interface PointRepository: CrudRepository<Point, Long> {
    fun findByUserId(id: Long): Iterable<Point>
    fun findByUser(user: User): Iterable<Point>
}

@CrossOrigin("localhost:4200")
@RepositoryRestResource(exported = true, collectionResourceRel = "results", path = "results")
interface ResultRepository: CrudRepository<Result, Long> {
    fun findByPointUserId(id: Long): Iterable<Result>
    fun findByPointUser(user: User): Iterable<Result>
}
