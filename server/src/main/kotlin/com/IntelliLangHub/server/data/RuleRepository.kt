package com.intellilanghub.server.data

import org.springframework.data.mongodb.repository.MongoRepository

interface RuleRepository : MongoRepository<Rule, String> {
    fun findOneByLibrary(library: String): List<Rule>
    override fun deleteAll()
}