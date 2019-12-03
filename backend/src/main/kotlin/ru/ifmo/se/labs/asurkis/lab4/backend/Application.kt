package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.data.generateHash
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository

//@Configuration
//@Order(SecurityProperties.BASIC_AUTH_ORDER)
//class SecurityConfiguration : WebSecurityConfigurerAdapter() {
//    override fun configure(http: HttpSecurity?) {
//        http!!.httpBasic()
//                .and()
//                .authorizeRequests().antMatchers("/", "/users", "/points", "/results").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//    }
//}

@SpringBootApplication
class BackendApplication {
    @Bean
    fun initData(userRepository: UserRepository,
                 pointRepository: PointRepository,
                 resultRepository: ResultRepository) = CommandLineRunner {
        val users = listOf(
                User(name = "A", passwordHash = generateHash("A")),
                User(name = "B", passwordHash = generateHash("B")))
        users.forEach { userRepository.save(it) }
        val points = users.cartesian(1..2)
                .map { Point(user = it.first, x = it.second.toDouble() * 2 - 3, y = it.first.id.toDouble() * 2 - 3) }
        points.forEach { pointRepository.save(it) }
        val results = points.cartesian(3..4)
                .map { Result(point = it.first, radius = it.second.toDouble()) }
        results.forEach { resultRepository.save(it) }
    }
}

infix fun <A, B> Iterable<A>.cartesian(other: Iterable<B>): List<Pair<A, B>> {
    val result = mutableListOf<Pair<A, B>>()
    for (a in this) for (b in other) result.add(Pair(a, b))
    return result.toList()
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
