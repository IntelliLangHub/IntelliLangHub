package org.example.langClient.ruleActions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.*
import com.intellij.openapi.ui.Messages
import org.intellij.plugins.intelliLang.Configuration
import org.intellij.plugins.intelliLang.inject.InjectorUtils
import org.intellij.plugins.intelliLang.inject.config.BaseInjection

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val libNameRegex = Regex("^.+?:\\s*(.+)\\s*:.*\$") // Probably needs refactoring

class PullAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val project: Project? = e.project

        e.presentation.isEnabledAndVisible = (project != null)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return

        val serverUrl = "http://localhost:8090"

        val libTable = LibraryTablesRegistrar.getInstance()
        val libs: MutableList<Library> = ArrayList()
        //libs.addAll(libTable.libraryTable.libraries)
        libs.addAll(libTable.getLibraryTable(project).libraries)

        val rules: MutableList<String> = ArrayList()
        for (lib in libs) {
            val matchResult = libNameRegex.matchEntire(lib.name ?: continue)
            val nameStripped = matchResult?.groupValues?.get(1) ?: continue

            try {
                val request: HttpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("$serverUrl/injection-pack/$nameStripped"))
                    .build()

                val client: HttpClient = HttpClient.newBuilder().build()
                val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                if (response.statusCode() == 404) continue

                rules.addAll(Json.decodeFromString<List<String>>(response.body()))
            } catch (ex: Exception) {
                Messages.showWarningDialog(
                    project,
                    "Cannot get rules", "Request Error"
                )
                return
            }
        }

        var hasImportErrors = false

        for (rule in rules) {
            try {
                val cfgStream = rule.byteInputStream()
                val cfg = Configuration.load(cfgStream) ?: continue

                val injections: MutableList<BaseInjection> = ArrayList()
                for (supportId in InjectorUtils.getActiveInjectionSupportIds()) {
                    injections.addAll(cfg.getInjections(supportId))
                }
                Configuration.getProjectInstance(project).replaceInjections(injections, injections, false)
            } catch (ex: Exception) {
                hasImportErrors = true
            }
        }

        if (!hasImportErrors) {
            Messages.showMessageDialog(
                project,
                "Rules have been imported", "Import Successful", Messages.getInformationIcon()
            )
        } else {
            Messages.showWarningDialog(
                project,
                "Errors occurred, not all rules were imported", "Import Errors"
            )
        }
    }
}