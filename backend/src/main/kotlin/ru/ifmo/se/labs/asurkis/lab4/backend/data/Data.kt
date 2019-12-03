package ru.ifmo.se.labs.asurkis.lab4.backend.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        private var username: String = "",
        private var password: String = ""
) : UserDetails {
    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority("USER"))
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
        @JsonIgnore
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
        @JsonIgnore
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
