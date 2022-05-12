package com.intellilanghub.server.controller

import com.intellilanghub.server.model.Rule
import com.intellilanghub.server.repository.RuleRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RequestMapping("/injection-pack")
@RestController
class InjectionPackController(private val ruleRepository: RuleRepository, private val mongoTemplate: MongoTemplate) {
    @GetMapping("")
    fun getInjectionPackLibraries(): List<String> {
        return mongoTemplate.getCollection("rule").distinct(Rule::library.name, String::class.java).toList()
    }


    @GetMapping("{library}")
    fun getInjectionPack(@PathVariable library: String): List<String> {
        val query = Query()
        query.addCriteria(Criteria.where(Rule::library.name).`is`(library))
        val rules = mongoTemplate.find(query, Rule::class.java)

        if (rules.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        return rules.map { it.injection }
    }
}