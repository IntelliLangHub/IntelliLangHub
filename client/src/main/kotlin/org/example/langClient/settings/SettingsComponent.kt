package org.example.langClient.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class SettingsComponent {
    val panel: JPanel
    private val myGithubToken = JBPasswordField()

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Enter github token: "), myGithubToken, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = myGithubToken

    fun getGithubToken(): CharArray {
        return myGithubToken.password
    }
    fun setGithubToken(newToken: String) {
        myGithubToken.text = newToken
    }
}
