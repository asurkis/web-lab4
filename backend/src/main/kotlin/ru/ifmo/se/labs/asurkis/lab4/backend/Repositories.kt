package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.data.repository.CrudRepository

//@CrossOrigin("localhost:4200")
//@RepositoryRestResource(exported = true, collectionResourceRel = "users", path = "users")
interface UserRepository: CrudRepository<User, Long>

//@CrossOrigin("localhost:4200")
//@RepositoryRestResource(exported = true, collectionResourceRel = "points", path = "points")
interface PointRepository: CrudRepository<Point, Long> {
    fun findByUserId(id: Long): Iterable<Point>
    fun findByUser(user: User): Iterable<Point>
}

//@CrossOrigin("localhost:4200")
//@RepositoryRestResource(exported = true, collectionResourceRel = "results", path = "results")
interface ResultRepository: CrudRepository<Result, Long> {
    fun findByPointUserId(id: Long): Iterable<Result>
    fun findByPointUser(user: User): Iterable<Result>
}
