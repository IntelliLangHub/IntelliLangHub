package org.example.langClient.settings

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.generateServiceName

fun createCredentialAttributes(key: String): CredentialAttributes {
    return CredentialAttributes(generateServiceName("IntelliLangClientSystem", key))
}
