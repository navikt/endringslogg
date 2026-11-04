package no.nav.k9.endringslogg

import kotlinx.serialization.json.JsonArray
import no.nav.k9.endringslogg.plugins.harForcedModal
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ForcedModalTest {
    private val iDag = LocalDate.now()

    private fun endring(
        date: LocalDate?,
        expiryDate: LocalDate? = null,
        modal: Modal? = Modal("Nyhet", forcedModal = true, slides = emptyList()),
    ) = Endring(
        title = "Endring",
        description = JsonArray(emptyList()),
        date = date?.toString(),
        expiryDate = expiryDate?.toString(),
        id = "id",
        modal = modal,
    )

    @Test
    fun `ny endring med tvungen modal vises`() {
        assertTrue(harForcedModal(endring(date = iDag)))
    }

    @Test
    fun `tvungen modal utløper en måned etter lansering`() {
        assertTrue(harForcedModal(endring(date = iDag.minusMonths(1))))
        assertFalse(harForcedModal(endring(date = iDag.minusMonths(1).minusDays(1))))
    }

    @Test
    fun `utløpsdato fra Sanity overstyrer standard utløp`() {
        assertTrue(harForcedModal(endring(date = iDag.minusMonths(3), expiryDate = iDag.plusDays(1))))
        assertFalse(harForcedModal(endring(date = iDag, expiryDate = iDag.minusDays(1))))
    }

    @Test
    fun `modal som ikke er tvungen vises ikke som tvungen`() {
        assertFalse(harForcedModal(endring(date = iDag, modal = Modal("Nyhet", forcedModal = false, slides = emptyList()))))
    }

    @Test
    fun `endring uten modal er ikke tvungen`() {
        assertFalse(harForcedModal(endring(date = iDag, modal = null)))
    }

    @Test
    fun `endring uten dato bruker utløpsdato hvis den finnes`() {
        assertTrue(harForcedModal(endring(date = null, expiryDate = iDag.plusDays(1))))
        assertFalse(harForcedModal(endring(date = null, expiryDate = iDag.minusDays(1))))
    }

    @Test
    fun `endring uten dato og utløpsdato er ikke tvungen`() {
        assertFalse(harForcedModal(endring(date = null)))
    }
}
