package com.intellilanghub.server.controller

import com.intellilanghub.server.model.Commit
import com.intellilanghub.server.model.CommitStatus
import com.intellilanghub.server.model.Rule
import com.intellilanghub.server.repository.CommitRepository
import com.intellilanghub.server.repository.RuleRepository
import com.intellilanghub.server.request.CreateInjectionPackCommitRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RequestMapping("/injection-pack-commit")
@RestController
@Transactional(readOnly = true)
class InjectionPackCommitController(
    private val commitRepository: CommitRepository,
    private val ruleRepository: RuleRepository,
    private val mongoTemplate: MongoTemplate
) {
    @GetMapping
    fun getCommits(@RequestParam(required = false) status: CommitStatus?): List<Commit> {
        val query = Query()
        status?.run { query.addCriteria(Criteria.where("status").`is`(status)) }
        return mongoTemplate.find(query, Commit::class.java)
    }

    @GetMapping("{id}")
    fun getCommit(@PathVariable id: String): Commit {
        return commitRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    }

    @PostMapping
    @Transactional(readOnly = false)
    fun createCommit(@RequestBody request: CreateInjectionPackCommitRequest): Commit {
        return commitRepository.save(
            Commit(
                injections = request.injections,
                library = request.library,
            )
        )
    }

    @PostMapping("{id}/accept")
    @Transactional(readOnly = false)
    fun acceptCommit(@PathVariable id: String) {
        val commit = commitRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (commit.status != CommitStatus.ACTIVE) {
            throw ResponseStatusException(HttpStatus.GONE, "commit is not active}")
        }
        commit.status = CommitStatus.ACCEPTED

        commitRepository.save(commit)
        for (injection in commit.injections) {
            val rule = Rule(
                injection = injection,
                library = commit.library,
            )
            ruleRepository.save(rule)
        }
    }


    @PostMapping("{id}/reject")
    @Transactional(readOnly = false)
    fun rejectCommit(@PathVariable id: String) {
        val commit = commitRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (commit.status != CommitStatus.ACTIVE) {
            throw ResponseStatusException(HttpStatus.GONE, "commit is not active}")
        }

        commit.status = CommitStatus.REJECTED
        commitRepository.save(commit)
    }
}