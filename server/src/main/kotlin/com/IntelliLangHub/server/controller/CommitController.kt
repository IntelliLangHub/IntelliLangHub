package com.intellilanghub.server.controller

import com.intellilanghub.server.model.Commit
import com.intellilanghub.server.model.CommitStatus
import com.intellilanghub.server.request.CreateInjectionPackCommitRequest
import com.intellilanghub.server.service.CommitService
import org.springframework.web.bind.annotation.*

@RequestMapping("/commit")
@RestController
class CommitController(
    private val commitService: CommitService,
) {
    @GetMapping
    fun getCommits(@RequestParam(required = false) status: CommitStatus?): List<Commit> {
        return commitService.getCommits(status)
    }

    @GetMapping("{id}")
    fun getCommit(@PathVariable id: String): Commit {
        return commitService.getCommit(id)
    }

    @PostMapping
    fun createCommit(@RequestBody request: CreateInjectionPackCommitRequest): Commit {
        return commitService.createCommit(request)
    }
}