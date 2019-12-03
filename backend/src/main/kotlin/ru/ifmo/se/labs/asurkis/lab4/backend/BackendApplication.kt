package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

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
        val points = users.cartesian(1..2)
                .map { Point(user = it.first, x = it.second.toDouble(), y = it.first.id.toDouble()) }
        val results = points.cartesian(1..2)
                .map { Result(point = it.first, radius = it.second.toDouble()) }

        for (r in results) {
            userRepository.save(r.point.user)
            pointRepository.save(r.point)
            resultRepository.save(r)
        }
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
