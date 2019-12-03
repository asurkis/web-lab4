package ru.ifmo.se.labs.asurkis.lab4.backend.services

import org.springframework.stereotype.Service
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserAlreadyExistsException
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.UserForm
import ru.ifmo.se.labs.asurkis.lab4.backend.generateHash
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository

@Service
class UserService(val userRepository: UserRepository) {
    fun registerNew(userForm: UserForm): User {
        if (userRepository.findByName(userForm.username).isPresent) {
            throw UserAlreadyExistsException(userForm.username)
        }
        val user = User(name = userForm.username, passwordHash = generateHash(userForm.password))
        return userRepository.save(user)
    }


}
