package com.intellilanghub.server.service

import com.intellilanghub.server.repository.UserRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class UserService (
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findById(username).orElseThrow { UsernameNotFoundException(username) }
    }
}