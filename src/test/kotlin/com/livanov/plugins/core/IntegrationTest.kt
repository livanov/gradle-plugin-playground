package com.livanov.plugins.core

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class IntegrationTest {

    @Rule
    @JvmField
    val temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Rule
    @JvmField
    val mockServer: WireMockRule = WireMockRule(4444)

    @Test
    fun stubbed_message_from_mock_server_received_instead_of_actual_http_call() {

        // GIVEN
        val setup = ProjectSetup(temporaryFolder.root)
        setup.buildGradle
                .addDSLPlugin("com.livanov.plugins.http-calling-plugin")
        setup.done()

        val stubbedMessage = "{'message':'the world is stubbed'}".replace("'", "\"")

        stubFor(get(urlEqualTo("/rest/api/v1/appstatus"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(stubbedMessage)));

        // WHEN
        val result = setup
                .withArguments(
                        "-Dhttp.proxyHost=localhost",
                        "-Dhttp.proxyPort=${mockServer.port()}")
                .build()

        // THEN
        assert(result.output.contains(stubbedMessage))
    }
}
