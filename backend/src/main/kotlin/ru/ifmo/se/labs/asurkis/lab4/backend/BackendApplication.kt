package ru.ifmo.se.labs.asurkis.lab4.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class BackendApplication {
    @Bean
    fun initData(userRepository: UserRepository,
                 pointRepository: PointRepository,
                 resultRepository: ResultRepository): CommandLineRunner {
        return CommandLineRunner {
            val users = listOf(
                    User(name = "A"),
                    User(name = "B"))
            val points = users.zip(1..2)
                    .map { Point(user = it.first, x = it.second.toDouble(), y = it.first.id.toDouble()) }
            val results = points.zip(1..2)
                    .map { Result(point = it.first, radius = it.second.toDouble()) }

            for (r in results) {
                userRepository.save(r.point.user)
                pointRepository.save(r.point)
                resultRepository.save(r)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
