package com.intellilanghub.server.repository

import com.intellilanghub.server.model.User
import org.springframework.data.mongodb.repository.MongoRepository


interface UserRepository : MongoRepository<User, String>