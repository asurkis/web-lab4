package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.InvalidPasswordException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.services.UserService
import java.security.Principal
import javax.servlet.http.HttpSession

@RestController
class UserController(val userService: UserService,
                     val authenticationManager: AuthenticationManager) {
    @PostMapping("/register")
    fun register(@RequestParam username: String,
                 @RequestParam password: String,
                 session: HttpSession) {
        val user = userService.registerNew(username, password)
        val token = UsernamePasswordAuthenticationToken(user, password, user.authorities)
        authenticationManager.authenticate(token)
        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
        }
    }
}
