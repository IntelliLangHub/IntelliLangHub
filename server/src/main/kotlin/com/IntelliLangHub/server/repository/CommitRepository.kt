package com.intellilanghub.server.repository

import com.intellilanghub.server.model.Commit
import org.springframework.data.mongodb.repository.MongoRepository

interface CommitRepository : MongoRepository<Commit, String>