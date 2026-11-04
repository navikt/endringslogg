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
import no.nav.k9.endringslogg.plugins.errorHandling
import no.nav.k9.endringslogg.plugins.gyldigAppId
import no.nav.k9.endringslogg.plugins.gyldigBildeRef
import no.nav.k9.endringslogg.plugins.gyldigDataset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SanityInputValideringTest {
    private fun ApplicationTestBuilder.app() =
        application {
            errorHandling()
            main()
            configureRouting(SanityClient("test", "v1"))
        }

    @Test
    fun `godtar appId, dataset og bildereferanser som er i bruk`() {
        listOf("K9_SAK", "AAP").forEach { assertTrue(gyldigAppId.matches(it), it) }
        listOf("production", "development", "test-1").forEach { assertTrue(gyldigDataset.matches(it), it) }
        assertTrue(gyldigBildeRef.matches("image-a1b2c3d4e5-800x600-png"))
    }

    @Test
    fun `avviser verdier som kan endre GROQ-spørringen eller URL-en`() {
        listOf("K9_SAK'] || _type=='x", "K9_SAK]", "", "a b").forEach { assertFalse(gyldigAppId.matches(it), it) }
        listOf("production/../x", "production?query=x", "Production", "").forEach { assertFalse(gyldigDataset.matches(it), it) }
        assertFalse(gyldigBildeRef.matches("../../annet-prosjekt/bilde.png"))
    }

    @Test
    fun `svarer 400 på ugyldig appId uten å spørre Sanity`() =
        testApplication {
            app()

            val response =
                client.post("/endringslogg/text") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"userId":"Z111111","appId":"K9_SAK'] || _type=='x","dataset":"production","maxEntries":10}""")
                }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

    @Test
    fun `svarer 400 på ugyldig dataset ved bildehenting`() =
        testApplication {
            app()

            val response =
                client.post("/endringslogg/image") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"slideImageRef":"image-abc-800x600-png","dataset":"../x"}""")
                }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
}
