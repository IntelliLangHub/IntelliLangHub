package com.intellilanghub.server.service

import com.intellilanghub.server.exception.CommitNotActiveException
import com.intellilanghub.server.exception.EntityNotFoundException
import com.intellilanghub.server.model.Commit
import com.intellilanghub.server.model.CommitStatus
import com.intellilanghub.server.model.InjectionPack
import com.intellilanghub.server.repository.CommitRepository
import com.intellilanghub.server.repository.InjectionPackRepository
import com.intellilanghub.server.request.CreateInjectionPackCommitRequest
import com.intellilanghub.server.utils.mergeInjectionConfigurations
import com.intellilanghub.server.utils.refactorInjectionConfiguration
import com.intellilanghub.server.utils.validateConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommitService(
    private val injectionPackService: InjectionPackService,
    private val commitRepository: CommitRepository,
    private val injectionPackRepository: InjectionPackRepository,
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

    fun getCommitDiff(commit: Commit): CommitDiff {
        val injectionPack = injectionPackRepository.findByLibrary(commit.library)
        val newInjectionConfiguration =
            applyCommitToInjectionConfiguration(injectionPack?.injectionConfiguration, commit)

        return CommitDiff(injectionPack?.injectionConfiguration ?: "", newInjectionConfiguration)
    }

    @Transactional(readOnly = false)
    fun createCommit(request: CreateInjectionPackCommitRequest): Commit {
        validateConfiguration(request.injectionConfiguration)

        return commitRepository.save(
            Commit(
                injectionConfiguration = request.injectionConfiguration,
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

        var injectionPack = injectionPackRepository.findByLibrary(commit.library)
        val newInjectionConfiguration =
            applyCommitToInjectionConfiguration(injectionPack?.injectionConfiguration, commit)

        if (injectionPack == null) {
            injectionPack = InjectionPack(
                library = commit.library,
                injectionConfiguration = newInjectionConfiguration
            )
        } else {
            injectionPack.injectionConfiguration = newInjectionConfiguration
        }

        injectionPackRepository.save(injectionPack)
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

    fun applyCommitToInjectionConfiguration(injectionConfiguration: String?, commit: Commit): String {
        if (injectionConfiguration == null) {
            return refactorInjectionConfiguration(commit.injectionConfiguration, commit.library)
        }
        return mergeInjectionConfigurations(
            listOf(injectionConfiguration, commit.injectionConfiguration),
            commit.library
        )
    }
}

data class CommitDiff(
    val currentInjectionConfiguration: String, val newInjectionConfiguration: String
)