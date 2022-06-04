package com.intellilanghub.server.utils

// Put correct display-name
// Delete unused fields
// Format
fun refactorInjectionConfiguration(configurationXml: String, library: String): String {
    return mergeInjectionConfigurations(listOf(configurationXml), library)
}