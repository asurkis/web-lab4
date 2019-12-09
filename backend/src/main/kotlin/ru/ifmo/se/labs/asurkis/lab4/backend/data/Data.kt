package ru.ifmo.se.labs.asurkis.lab4.backend.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "USER"
) : GrantedAuthority {
    constructor(name: String) : this(id = 0, name = name)
    override fun getAuthority() = name
    override fun equals(other: Any?) = other is Role && other.name == name
}

@Entity
@Table(name = "users")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        private var username: String = "",
        @JsonIgnore
        private var password: String = "",
        @JsonIgnore
        @OneToMany
        var roles: List<Role> = mutableListOf(Role())
) : UserDetails {
    override fun getAuthorities() = roles
    override fun isEnabled() = true
    override fun getUsername() = username
    override fun isCredentialsNonExpired() = true
    override fun getPassword() = password
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
}

@Entity
@Table(name = "points")
data class Point(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @ManyToOne
        @JsonIgnore
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
