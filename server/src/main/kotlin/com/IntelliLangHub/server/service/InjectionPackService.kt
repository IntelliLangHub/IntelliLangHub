package com.intellilanghub.server.service

import com.intellilanghub.server.exception.EntityNotFoundException
import com.intellilanghub.server.model.Rule
import com.intellilanghub.server.repository.RuleRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InjectionPackService(
    private val ruleRepository: RuleRepository,
    private val mongoTemplate: MongoTemplate
) {
    fun getInjectionPackLibraries(): List<String> {
        return mongoTemplate.getCollection("rule").distinct(Rule::library.name, String::class.java).toList()
    }


    fun getInjectionPack(library: String): List<String> {
        val query = Query()
        query.addCriteria(Criteria.where(Rule::library.name).`is`(library))
        val rules = mongoTemplate.find(query, Rule::class.java)

        if (rules.isEmpty()) {
            throw EntityNotFoundException("No rules found for library $library")
        }

        return rules.map { it.injection }
    }
}