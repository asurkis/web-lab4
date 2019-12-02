package ru.ifmo.se.labs.asurkis.lab4.backend

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "",
        var passwordHash: String = ""
)

@Entity
@Table(name = "points")
data class Point(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @ManyToOne
        var user: User = User(),
        var x: Double = 0.0,
        var y: Double = 0.0
) {
    fun fallsInto(r: Double): Boolean {
        val x = x
        val y = y
        return (-r <= x && x <= 0 && 0 <= y && y <= r)
                || (x >= 0 && y >= 0 && x + y <= .5 * r)
                || (x >= 0 && y <= 0 && x * x + y * y <= r * r)
    }
}

val Point.userId: Long
    get() = user.id

@Entity
@Table(name = "results")
data class Result(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @ManyToOne
        var point: Point = Point(),
        var radius: Double = 0.0
) {
    val result: Boolean
        get() = point.fallsInto(radius)
}

val Result.pointId: Long
    get() = point.id

val Result.user: User
    get() = point.user

val Result.userId: Long
    get() = user.id
