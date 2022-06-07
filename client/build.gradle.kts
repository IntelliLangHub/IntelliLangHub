plugins {
    id("org.jetbrains.intellij") version "1.6.0"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    java
}

group = "org.intelliLangHub"
version = "0.9.2"
val changes = """Initial release of the plugin.""".trimIndent()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.1")
    plugins.add("org.intellij.intelliLang")
}
tasks {
    patchPluginXml {
        version.set("${project.version}")
        changeNotes.set(changes)
        sinceBuild.set("213")
        untilBuild.set("221.*")
    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}