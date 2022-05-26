package org.example.langClient.ruleActions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

import java.io.FileNotFoundException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Suppress("PROVIDED_RUNTIME_TOO_LOW")  // https://github.com/Kotlin/kotlinx.serialization/issues/993
@Serializable
data class PushedInjection(
    val library: String,
    val injections: List<String>,
)

class PushAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project

        val serverUrl = this::class.java.classLoader.getResource("server_url")?.readText()
        serverUrl ?: throw FileNotFoundException("File with server url not found.")

        val libName =
            Messages.showInputDialog(project, "Enter library name", "Library Name", Messages.getQuestionIcon())
        if (libName == null) {
            Messages.showWarningDialog(project, "Library name was not specified", "Specify Library Name")
            return
        }
        val rule = Messages.showMultilineInputDialog(
            project,
            "Enter injection configuration",
            "Injection",
            null,
            Messages.getQuestionIcon(),
            null
        )
        if (rule == null) {
            Messages.showWarningDialog(project, "Injection configuration was not specified", "Specify Injection")
            return
        }

        val injection = PushedInjection(libName, listOf(rule))

        try {
            val request: HttpRequest = HttpRequest.newBuilder()
                .uri(URI.create("$serverUrl/injection-pack-commit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(injection)))
                .build()

            val client: HttpClient = HttpClient.newBuilder().build()
            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (ex: Exception) {
            Messages.showWarningDialog(
                project,
                "Cannot send injection", "Request Error"
            )
            return
        }

        Messages.showMessageDialog(
            project,
            "Your injection will appear in the repository after moderation",
            "Injection Created",
            Messages.getInformationIcon()
        )
    }
}