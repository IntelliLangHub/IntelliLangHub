package com.intellilanghub.server.utils


private data class InjectionKey(
    val language: String,
    val injectionId: String
)


fun mergeInjectionConfigurations(
    configurationsXml: List<String>,
    library: String
): String {
    val configurations = configurationsXml.map { readInjectionConfiguration(it) }
    val merged = mergeInjectionConfigurations(configurations, library)
    return writeInjectionConfiguration(merged)
}

fun mergeInjectionConfigurations(
    configurations: List<LanguageInjectionConfiguration>,
    library: String
): LanguageInjectionConfiguration {
    val injectionKeyToPlaces = mutableMapOf<InjectionKey, MutableSet<Place>>()

    for (configuration in configurations) {
        for (injection in configuration.injections) {
            val key = InjectionKey(injection.language, injection.injectorId)
            val places = injectionKeyToPlaces.getOrPut(key) { mutableSetOf() }
            places.addAll(injection.places)
        }
    }

    val mergedConfiguration = LanguageInjectionConfiguration()

    for ((key, places) in injectionKeyToPlaces) {
        val injection = Injection()
        injection.language = key.language
        injection.injectorId = key.injectionId
        injection.displayName = "$library ${injection.language}"
        injection.places = places.toMutableList()

        mergedConfiguration.injections.add(injection)
    }

    return mergedConfiguration
}