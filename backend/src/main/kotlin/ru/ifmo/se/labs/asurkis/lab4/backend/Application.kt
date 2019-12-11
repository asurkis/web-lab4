package ru.ifmo.se.labs.asurkis.lab4.backend

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
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.ifmo.se.labs.asurkis.lab4.backend.services.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class SecurityConfiguration(val userService: UserService,
                            val passwordEncoder: BCryptPasswordEncoder) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        val logoutSuccessHandler = object : SimpleUrlLogoutSuccessHandler() {
            override fun onLogoutSuccess(request: HttpServletRequest?,
                                         response: HttpServletResponse?,
                                         authentication: Authentication?) {
            }
        }

        http!!
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/login").permitAll()
                .anyRequest().authenticated()
                .and().cors()
                .and().logout().logoutSuccessHandler(logoutSuccessHandler)
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
    fun corsConfigurer() = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
                    .allowCredentials(true)
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .allowedOrigins("http://localhost:4200")
        }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
