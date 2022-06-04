package com.intellilanghub.server.utils

import com.intellilanghub.server.exception.InvalidLanguageConfigurationException
import org.simpleframework.xml.*
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream

@Root(name = "LanguageInjectionConfiguration", strict = true)
class LanguageInjectionConfiguration {
    @field:ElementList(name = "injection", inline = true, required = false)
    var injections = mutableListOf<Injection>()
}


@Root(name = "injection", strict = false)
class Injection {
    @field:Attribute(name = "language", required = true)
    lateinit var language: String

    @field:Attribute(name = "injector-id", required = true)
    lateinit var injectorId: String

    @field:Element(name = "display-name", required = true)
    lateinit var displayName: String

    @field:ElementList(name = "place", inline = true, required = false)
    var places = mutableListOf<Place>()
}

@Root(name = "place", strict = false)
data class Place(
    @field:Text(required = false)
    var value: String = ""
)

fun readInjectionConfiguration(injectionConfigurationXml: String): LanguageInjectionConfiguration {
    val serializer: Serializer = Persister()
    try {
        return serializer.read(LanguageInjectionConfiguration::class.java, injectionConfigurationXml)
    } catch (e: Exception) {
        throw InvalidLanguageConfigurationException("Invalid language configuration: $e")
    }
}

fun writeInjectionConfiguration(injectionConfiguration: LanguageInjectionConfiguration): String {
    val serializer: Serializer = Persister()
    val stream = ByteArrayOutputStream()

    try {
        serializer.write(injectionConfiguration, stream)
    } catch (e: Exception) {
        throw InvalidLanguageConfigurationException("Invalid language configuration: $e")
    }

    return stream.toString()
}

fun validateConfiguration(injectionConfigurationXml: String): Boolean {
    val serializer: Serializer = Persister()
    try {
        return serializer.validate(LanguageInjectionConfiguration::class.java, injectionConfigurationXml)
    } catch (e: Exception) {
        throw InvalidLanguageConfigurationException("Invalid language configuration: $e")
    }
}