package no.nav.k9.endringslogg

import SanityClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.k9.endringslogg.plugins.configureRouting
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AnalyticsRoutesTest {
    @BeforeTest
    fun setup() {
        TestDatabase.reset()
    }

    private fun ApplicationTestBuilder.app() =
        application {
            main()
            configureRouting(SanityClient("test", "v1"))
        }

    @Test
    fun `lagrer sette endringer fra frontend`() =
        testApplication {
            app()
            val doc = UUID.randomUUID()

            val response =
                client.post("/analytics/sett-endringer") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"userId":"Z111111","appId":"K9","documentIds":["$doc"]}""")
                }

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(listOf(doc), getSeenEntriesForUser("Z111111"))
        }

    @Test
    fun `lagrer sette tvungne modaler fra frontend`() =
        testApplication {
            app()
            val doc = UUID.randomUUID()

            val response =
                client.post("/analytics/seen-forced-modal") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"userId":"Z111111","documentIds":["$doc"]}""")
                }

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(listOf(doc), getSeenForcedEntriesForUser("Z111111"))
        }
}
