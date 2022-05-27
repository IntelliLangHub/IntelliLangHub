package com.intellilanghub.server.service

import com.intellilanghub.server.model.Commit
import com.intellilanghub.server.model.CommitStatus
import com.intellilanghub.server.model.Rule
import com.intellilanghub.server.repository.CommitRepository
import com.intellilanghub.server.repository.RuleRepository
import com.intellilanghub.server.request.CreateInjectionPackCommitRequest
import exception.CommitNotActiveException
import exception.EntityNotFoundException
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommitService(
    private val commitRepository: CommitRepository,
    private val ruleRepository: RuleRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun getCommits(status: CommitStatus?): List<Commit> {
        val query = Query()
        status?.run { query.addCriteria(Criteria.where("status").`is`(status)) }
        return mongoTemplate.find(query, Commit::class.java)
    }

    fun getCommit(id: String): Commit {
        return commitRepository.findById(id).orElseThrow { EntityNotFoundException("Comment with id=$id not found") }
    }

    @Transactional(readOnly = false)
    fun createCommit(request: CreateInjectionPackCommitRequest): Commit {
        return commitRepository.save(
            Commit(
                injections = request.injections,
                library = request.library,
            )
        )
    }

    @Transactional(readOnly = false)
    fun acceptCommit(id: String) {
        val commit =
            commitRepository.findById(id).orElseThrow { EntityNotFoundException("Commit with id=$id not found") }
        if (commit.status != CommitStatus.ACTIVE) {
            throw CommitNotActiveException("Commit with id=$id is already ${commit.status}")
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


    @Transactional(readOnly = false)
    fun rejectCommit(id: String) {
        val commit =
            commitRepository.findById(id).orElseThrow { EntityNotFoundException("Commit with id=$id not found") }
        if (commit.status != CommitStatus.ACTIVE) {
            throw CommitNotActiveException("Commit with id=$id is already ${commit.status}")
        }

        commit.status = CommitStatus.REJECTED
        commitRepository.save(commit)
    }
}