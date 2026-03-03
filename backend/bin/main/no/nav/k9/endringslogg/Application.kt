package no.nav.k9.endringslogg

import SanityClient
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json
import no.nav.k9.endringslogg.env.DB_PASSWORD
import no.nav.k9.endringslogg.env.DB_URL
import no.nav.k9.endringslogg.env.DB_USERNAME
import no.nav.k9.endringslogg.env.SANITY_PROJECT_ID
import no.nav.k9.endringslogg.plugins.configureLogging
import no.nav.k9.endringslogg.plugins.configureRouting
import no.nav.k9.endringslogg.plugins.errorHandling
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.k9.endringslogg.Application")

fun Application.main() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                encodeDefaults = true
            },
        )
    }
    install(CORS) {
        logger.info("Setter opp cors")
        allowHost("k9.intern.nav.no", listOf("https"))
        allowHost("k9.dev.intern.nav.no", listOf("https"))
        allowHost("k9.intern.dev.nav.no", listOf("https"))
        allowHost("k9-los-web.intern.nav.no", listOf("https"))
        allowHost("k9-los-web.intern.dev.nav.no", listOf("https"))
        allowHost("kelvin.intern.nav.no", listOf("https"))
        allowHost("kelvin.intern.dev.nav.no", listOf("https"))
        allowHost("kelvin.ansatt.dev.nav.no", listOf("https"))

        allowHost("familie-endringslogg.sanity.studio", listOf("https"))
        allowHost("navikt.github.io", listOf("https"))
        allowHost("localhost:8000", listOf("http"))
        allowHost("localhost:3000", listOf("http"))
        allowHost("localhost:9000", listOf("http"))

        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.ContentType)
    }
}

fun main() {
    logger.info("Kjører flyway")
    val flyway: Flyway =
        Flyway.configure().dataSource(
            "jdbc:$DB_URL",
            DB_USERNAME,
            DB_PASSWORD,
        ).load()
    flyway.migrate()

    val client = SanityClient(SANITY_PROJECT_ID, "v1")

    connectToDatabase()

    embeddedServer(
        Netty,
        environment =
            applicationEngineEnvironment {
                module {
                    configureLogging()
                    errorHandling()
                    main()
                    configureRouting(client)
                }
                connector {
                    port = 8080
                    host = "0.0.0.0"
                }
            },
    ) {
    }.start(wait = true)
}
