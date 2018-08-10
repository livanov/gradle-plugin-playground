package com.livanov.plugins.core

import org.gradle.testkit.runner.internal.DefaultGradleRunner
import java.io.File

class ProjectSetup(private val rootDir: File) : DefaultGradleRunner() {

    private val debug = true

    val buildGradle: BuildGradle = BuildGradle()

    fun done(): ProjectSetup {
        buildGradle.create(rootDir)

        this.withDebug(debug)
                .withPluginClasspath()
                .withProjectDir(rootDir)

        return this
    }

    class BuildGradle {

        private val plugins: MutableList<Plugin> = mutableListOf()

        fun addDSLPlugin(id: String, mavenPath: String? = null): BuildGradle {

            plugins.add(DSLPlugin(id, mavenPath))
            return this
        }

        fun create(rootDir: File) {

            val file = File(rootDir, "build.gradle")

            addBuildScriptDependenciesSection(file)
            addPluginsDSLSection(file)

            file.createNewFile()
        }

        private fun addBuildScriptDependenciesSection(file: File) {

            val content = plugins.mapNotNull { it.mavenPath }
                    .joinToString("\n") {
                        "classpath '$it'"
                    }

            if (content.isEmpty()) {
                return
            }

            file.writeText("""
                buildscript {
                    dependencies {
                        $content
                    }
                }
            """.trimIndent())
        }

        private fun addPluginsDSLSection(file: File) {
            val content = plugins.joinToString("\n") {
                "id '${it.id}'"
            }

            file.writeText("""
                plugins {
                    $content
                }
            """.trimIndent())
        }
    }

    abstract class Plugin(val id: String, val mavenPath: String?)

    class DSLPlugin(id: String, mavenPath: String?) : Plugin(id, mavenPath)
}
