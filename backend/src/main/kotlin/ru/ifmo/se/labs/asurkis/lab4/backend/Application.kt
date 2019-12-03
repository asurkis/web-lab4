package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.session.web.http.HeaderHttpSessionIdResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Point
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.PointRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.services.UserService

@Configuration
@EnableWebSecurity
class SecurityConfiguration(val userService: UserService,
                            val passwordEncoder: BCryptPasswordEncoder) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http!!
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/token", "/register").permitAll()
                .anyRequest().authenticated()
                .and().cors()
                .and().formLogin()
                .and().logout().logoutUrl("/logout")
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }
}

@SpringBootApplication
class Application {
    @Bean
    fun initData(userService: UserService,
                 pointRepository: PointRepository,
                 resultRepository: ResultRepository,
                 passwordEncoder: BCryptPasswordEncoder) = CommandLineRunner {
        val users = listOf(
                User(username = "A", password = passwordEncoder.encode("A")),
                User(username = "B", password = passwordEncoder.encode("B")))
        users.forEach { userService.registerNew(it) }
        val points = users.cartesian(1..2)
                .map { Point(user = it.first, x = it.second.toDouble() * 2 - 3, y = it.first.id.toDouble() * 2 - 3) }
        points.forEach { pointRepository.save(it) }
        val results = points.cartesian(3..4)
                .map { Result(point = it.first, radius = it.second.toDouble()) }
        results.forEach { resultRepository.save(it) }
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        class Configurer : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200")
            }
        }
        return Configurer()
    }

    @Bean
    fun sessionStrategy() = HeaderHttpSessionIdResolver.xAuthToken()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

infix fun <A, B> Iterable<A>.cartesian(other: Iterable<B>): List<Pair<A, B>> {
    val result = mutableListOf<Pair<A, B>>()
    for (a in this) for (b in other) result.add(Pair(a, b))
    return result.toList()
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
