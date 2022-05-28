package com.intellilanghub.server.controller

import com.intellilanghub.server.model.CommitStatus
import com.intellilanghub.server.service.CommitService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@RequestMapping("/moderation")
@Controller
class ModerationController(
    private val commitService: CommitService,
) {

    @GetMapping
    fun index(model: Model): String {
        return "index"
    }

    @GetMapping("commits")
    fun getCommits(model: Model): String {
        val commits = commitService.getCommits(null)
        model.addAttribute("commits", commits)
        return "commits"
    }

    @GetMapping("active-commits")
    fun getActiveCommits(model: Model): String {
        val commits = commitService.getCommits(CommitStatus.ACTIVE)
        model.addAttribute("commits", commits)
        return "commits-active"
    }

    @GetMapping("commit/{id}")
    fun getCommit(@PathVariable id: String, model: Model): String {
        val commit = commitService.getCommit(id)
        model.addAttribute("commit", commit)
        if (commit.status == CommitStatus.ACTIVE) {
            return "commit-active"
        }
        return "commit-resolved"
    }

    @PostMapping("commit/{id}/accept")
    fun acceptCommit(@PathVariable id: String): RedirectView {
        commitService.acceptCommit(id)
        return RedirectView("/moderation/commit/$id")
    }


    @PostMapping("commit/{id}/reject")
    fun rejectCommit(@PathVariable id: String): RedirectView {
        commitService.rejectCommit(id)
        return RedirectView("/moderation/commit/$id")
    }
}