package org.example.langClient.ruleActions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class PushAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        e.presentation.isEnabledAndVisible = (project != null && editor != null && editor.selectionModel.hasSelection())
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        if (editor?.selectionModel?.selectedText == null) {
            Messages.showMessageDialog(project, "No text was selected.", "Error", Messages.getInformationIcon())
            return
        }
        val name = editor.selectionModel.selectedText
        val requestJson = "{ \"name\":\"$name\", \"rule\":\"$name\", \"library\":\"test\" }"

        val request : HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8090"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestJson))
            .build()

        val client: HttpClient = HttpClient.newBuilder().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Messages.showMessageDialog(project, response.body(), "Created Rule", Messages.getInformationIcon())
    }
}