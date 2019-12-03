package ru.ifmo.se.labs.asurkis.lab4.backend.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserAlreadyExistsException
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.UserForm
import ru.ifmo.se.labs.asurkis.lab4.backend.generateHash
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository

@Service
class UserService(val userRepository: UserRepository) : UserDetailsService {
    fun registerNew(userForm: UserForm): User {
        if (userRepository.findByUsername(userForm.username).isPresent) {
            throw UserAlreadyExistsException(userForm.username)
        }
        val user = User(username = userForm.username, password = generateHash(userForm.password))
        return userRepository.save(user)
    }

    override fun loadUserByUsername(username: String?): User? {
        return if (username == null) {
            null
        } else {
            userRepository.findByUsername(username).orElse(null)
        }
    }
}
