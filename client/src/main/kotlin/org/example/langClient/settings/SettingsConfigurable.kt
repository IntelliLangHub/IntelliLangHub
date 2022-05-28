package org.example.langClient.settings

import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings.
 */
class SettingsConfigurable : Configurable {
    private var mySettingsComponent: SettingsComponent? = null

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "IntelliLangHub Client Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = SettingsComponent()
        return mySettingsComponent?.panel
    }

    override fun isModified(): Boolean {
        return true
    }

    override fun apply() {
        val credentialAttributes = createCredentialAttributes("githubToken")
        val githubTokenValue = mySettingsComponent?.getGithubToken() ?: return
        val credentials = Credentials(null, githubTokenValue)
        PasswordSafe.instance.set(credentialAttributes, credentials)
    }

    override fun reset() {
        val credentialAttributes = createCredentialAttributes("githubToken")
        val credentials = PasswordSafe.instance.get(credentialAttributes)
        if (credentials != null) {
            mySettingsComponent?.setGithubToken("Secret token")
        }
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
