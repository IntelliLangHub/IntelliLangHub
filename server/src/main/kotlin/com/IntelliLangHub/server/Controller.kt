package com.intellilanghub.server

import com.intellilanghub.server.data.Rule
import com.intellilanghub.server.data.RuleRepository
import com.intellilanghub.server.request.RuleRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class Controller(private val rulesRepository: RuleRepository) {
    @GetMapping("/{library}")
    fun getRulesByLibrary(@PathVariable("library") library: String): ResponseEntity<List<Rule>> {
        val rule = rulesRepository.findOneByLibrary(library)
        return ResponseEntity.ok(rule)
    }

    @PostMapping
    fun createRule(@RequestBody request: RuleRequest): ResponseEntity<Rule> {
        val rule = rulesRepository.save(
            Rule(
                name = request.name,
                rule = request.rule,
                library = request.library,
                createdDate = LocalDateTime.now()
            )
        )
        return ResponseEntity(rule, HttpStatus.CREATED)
    }
}