package com.intellilanghub.server.controller

import com.intellilanghub.server.data.Rule
import com.intellilanghub.server.data.RuleRepository
import com.intellilanghub.server.request.CreateRuleRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RequestMapping("/rule")
@RestController
class RuleController(private val ruleRepository: RuleRepository, private val mongoTemplate: MongoTemplate) {
    @GetMapping
    fun getRules(@RequestParam(required = false) library: String?): ResponseEntity<List<Rule>> {
        val query = Query()
        library?.run { query.addCriteria(Criteria.where("library").`is`(library)) }
        val rules = mongoTemplate.find(query, Rule::class.java)
        return ResponseEntity.ok(rules)
    }

    @PostMapping
    fun createRule(@RequestBody request: CreateRuleRequest): ResponseEntity<Rule> {
        val rule = ruleRepository.save(
            Rule(
                name = request.name,
                injection = request.injection,
                library = request.library,
                createdDate = LocalDateTime.now()
            )
        )
        return ResponseEntity(rule, HttpStatus.CREATED)
    }
}