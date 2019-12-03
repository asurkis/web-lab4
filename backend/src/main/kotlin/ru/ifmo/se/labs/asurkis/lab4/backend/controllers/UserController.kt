package ru.ifmo.se.labs.asurkis.lab4.backend.controllers

import org.springframework.web.bind.annotation.*
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.UserForm
import ru.ifmo.se.labs.asurkis.lab4.backend.services.UserService
import java.util.*
import javax.servlet.http.HttpSession

@RestController
class UserController(val userService: UserService) {
    @PostMapping("/register")
    fun register(@RequestParam username: String,
                 @RequestParam password: String,
                 session: HttpSession): Map<String, String> {
        userService.registerNew(UserForm(username, password))
        return Collections.singletonMap("token", session.id)
    }
}
