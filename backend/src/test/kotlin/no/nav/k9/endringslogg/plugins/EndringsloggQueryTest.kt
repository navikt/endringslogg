package no.nav.k9.endringslogg.plugins

import java.net.URLDecoder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EndringsloggQueryTest {
    private fun dekod(queryString: String) =
        queryString.split("&").associate { del ->
            val (navn, verdi) = del.split("=", limit = 2)
            URLDecoder.decode(navn, Charsets.UTF_8) to URLDecoder.decode(verdi, Charsets.UTF_8)
        }

    @Test
    fun `sender appId og maxEntries som GROQ-parametere`() {
        val params = dekod(lagEndringsloggQueryString("K9_SAK", "production", 150, visUpubliserte = false))

        assertEquals(
            "*[_type == \$appId && publisert == true] | order(_createdAt desc)[0...\$maxEntries]",
            params["query"],
        )
        assertEquals("\"K9_SAK\"", params["\$appId"])
        assertEquals("150", params["\$maxEntries"])
    }

    @Test
    fun `avviser appId som ikke er kjent`() {
        assertFailsWith<IllegalArgumentException> {
            lagEndringsloggQueryString("K9_SAK' || true || _type=='x", "production", 150, visUpubliserte = false)
        }
    }

    @Test
    fun `avviser ukjent dataset og maxEntries utenfor grensene`() {
        assertFailsWith<IllegalArgumentException> {
            lagEndringsloggQueryString("K9_SAK", "production/../../projects", 150, visUpubliserte = false)
        }
        assertFailsWith<IllegalArgumentException> {
            lagEndringsloggQueryString("K9_SAK", "production", 10_000, visUpubliserte = false)
        }
    }
}
