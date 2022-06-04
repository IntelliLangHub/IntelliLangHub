package com.intellilanghub.server.controller

import com.intellilanghub.server.model.CommitStatus
import com.intellilanghub.server.service.CommitService
import com.intellilanghub.server.service.InjectionPackService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.view.RedirectView


@RequestMapping("/moderation")
@Controller
class ModerationController(
    private val commitService: CommitService,
    private val injectionPackService: InjectionPackService,
) {

    @GetMapping
    fun index(model: Model): String {
        return "index"
    }

    @GetMapping("injection-packs")
    fun getInjectionPacks(model: Model): String {
        model.addAttribute("libraries", injectionPackService.getInjectionPackLibraries())
        return "injection-packs"
    }

    @GetMapping("injection-pack/{library}")
    fun getInjectionPack(@PathVariable library: String, model: Model): String {
        model.addAttribute("pack", injectionPackService.getInjectionPack(library))
        return "injection-pack"
    }

    @PostMapping("injection-pack/{library}")
    fun postInjectionPack(
        @PathVariable library: String, @RequestBody formData: MultiValueMap<String, String>, model: Model
    ): RedirectView {
        val injectionConfiguration = formData.getFirst("injectionConfiguration")
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Missing injectionConfiguration"
            )
        injectionPackService.updateInjectionPack(library, injectionConfiguration)
        return RedirectView("/moderation/injection-pack/$library")
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
            val diff = commitService.getCommitDiff(commit)
            model.addAttribute("diff", diff)
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