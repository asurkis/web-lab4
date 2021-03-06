package ru.ifmo.se.labs.asurkis.lab4.backend.services

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.ifmo.se.labs.asurkis.lab4.backend.data.User
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserAlreadyExistsException
import ru.ifmo.se.labs.asurkis.lab4.backend.exceptions.UserNotFoundException
import ru.ifmo.se.labs.asurkis.lab4.backend.forms.UserForm
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.RoleRepository
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.UserRepository
import javax.validation.Valid

@Service
class UserService(val roleRepository: RoleRepository,
                  val userRepository: UserRepository,
                  val passwordEncoder: BCryptPasswordEncoder) : UserDetailsService {
    fun registerNew(user: User): User {
        if (userRepository.findByUsername(user.username).isPresent) {
            throw UserAlreadyExistsException(user.username)
        }
        user.authorities.forEach { roleRepository.save(it) }
        return userRepository.save(user)
    }

    fun registerNew(username: String, rawPassword: String) =
            registerNew(User(username = username, password = passwordEncoder.encode(rawPassword)))

    fun registerNew(@Valid userForm: UserForm) =
            registerNew(userForm.username!!, userForm.password!!)

    override fun loadUserByUsername(username: String?) =
            userRepository.findByUsername(username!!).orElseThrow { UserNotFoundException(username) }!!
}
