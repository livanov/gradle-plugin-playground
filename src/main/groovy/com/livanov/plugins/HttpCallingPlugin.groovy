package com.livanov.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class HttpCallingPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "http://some_address_doesnt_matter/rest/api/v1/appstatus".toURL().text
    }
}