package no.nav.k9.endringslogg

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SanityParsingTest {
    private val json = Json { ignoreUnknownKeys = true }

    private val endringMedModal =
        """
        {
          "_id": "abc",
          "title": "Ny funksjon",
          "description": [],
          "date": "2026-01-01",
          "modal": {
            "numSlides": 2,
            "modalHeader": "Nyhet",
            "forcedModal": true,
            "modalSlide1": { "slideHeader": "Steg 1", "slideDescription": [] },
            "modalSlide2": {
              "slideHeader": "Steg 2",
              "slideImage": { "asset": { "_ref": "image-abc123-800x600-png" } },
              "altText": "Skjermbilde"
            }
          }
        }
        """.trimIndent()

    @Test
    fun `leser modal med slides fra Sanity`() {
        val modal = json.decodeFromString(Endring.serializer(), endringMedModal).modal!!

        assertEquals("Nyhet", modal.title)
        assertTrue(modal.forcedModal)
        assertEquals(listOf("Steg 1", "Steg 2"), modal.slides.map { it.header })
        assertEquals("image-abc123-800x600-png", (modal.slides[1].image as SlideImageJson).getReference())
    }

    @Test
    fun `modal uten slides blir null`() {
        val endring =
            json.decodeFromString(
                Endring.serializer(),
                """{ "_id": "abc", "title": "T", "description": [], "modal": { "numSlides": 0 } }""",
            )

        assertNull(endring.modal)
    }

    @Test
    fun `sender modal til frontend i forventet format`() {
        val endring = json.decodeFromString(Endring.serializer(), endringMedModal)

        val modal = Json.encodeToJsonElement(Endring.serializer(), endring).jsonObject["modal"]!!.jsonObject
        val slides = modal["slides"]!!.jsonArray

        assertEquals("Nyhet", modal["header"]!!.jsonPrimitive.content)
        assertEquals("Steg 1", slides[0].jsonObject["slideHeader"]!!.jsonPrimitive.content)
        assertEquals("image-abc123-800x600-png", slides[1].jsonObject["slideImageRef"]!!.jsonPrimitive.content)
        assertEquals("Skjermbilde", slides[1].jsonObject["altText"]!!.jsonPrimitive.content)
    }
}
