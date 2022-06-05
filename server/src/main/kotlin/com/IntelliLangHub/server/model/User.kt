package com.intellilanghub.server.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
class User(
    @MongoId private val username: String,
    private val password: String,
    private val roles: List<String>

) : UserDetails {
    private val ROLE_PREFIX = "ROLE_"

    override fun getAuthorities(): MutableList<GrantedAuthority> {
        val list: MutableList<GrantedAuthority> = ArrayList()
        for (role in roles) {
            list.add(SimpleGrantedAuthority(ROLE_PREFIX + role))
        }
        return list
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}