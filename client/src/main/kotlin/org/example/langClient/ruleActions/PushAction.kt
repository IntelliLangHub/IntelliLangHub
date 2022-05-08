package org.example.langClient.ruleActions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import kotlinx.serialization.*
import kotlinx.serialization.json.*

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Suppress("PROVIDED_RUNTIME_TOO_LOW")  // https://github.com/Kotlin/kotlinx.serialization/issues/993
@Serializable
data class PushedInjection(
    val name: String,
    val rule: String?,
    val library: String,
)

class PushAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        e.presentation.isEnabledAndVisible = (project != null && editor != null && editor.selectionModel.hasSelection())
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        val serverUrl = "http://localhost:8090"

        val rule = editor?.selectionModel?.selectedText
        if (rule == null) {
            Messages.showMessageDialog(project, "No text was selected.", "Error", Messages.getInformationIcon())
            return
        }
        val injection = PushedInjection("Test Rule", rule, "testlib")

        val request : HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(serverUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(injection)))
            .build()

        val client: HttpClient = HttpClient.newBuilder().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        Messages.showMessageDialog(project, response.body(), "Created Rule", Messages.getInformationIcon())
    }
}