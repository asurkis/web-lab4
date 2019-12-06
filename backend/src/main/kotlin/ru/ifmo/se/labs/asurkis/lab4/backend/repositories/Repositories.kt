package ru.ifmo.se.labs.asurkis.lab4.backend.repositories

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Role
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import java.util.*

@Component
interface RoleRepository : CrudRepository<Role, Long>

@Component
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(name: String): Optional<User>
}

@Component
interface PointRepository : CrudRepository<Point, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): Optional<Point>
    fun findByUserId(id: Long): Iterable<Point>
    fun findByUser(user: User): Iterable<Point>
    @Modifying
    @Query("DELETE FROM Point p WHERE 0=(SELECT COUNT(r.id) FROM Result r WHERE p=r.point)")
    fun deleteEmpty();
}

@Component
interface ResultRepository : CrudRepository<Result, Long> {
    fun findByIdAndPointIdAndPointUserId(id: Long, pointId: Long, userId: Long): Optional<Result>
    fun findByPointIdAndPointUserId(pointId: Long, userId: Long): Iterable<Result>
    fun findByPointId(id: Long): Iterable<Result>
    fun findByPoint(point: Point): Iterable<Result>
    fun findByPointUserId(id: Long): Iterable<Result>
    fun findByPointUser(user: User): Iterable<Result>
}
