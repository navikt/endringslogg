package no.nav.k9.endringslogg

import kotlinx.serialization.json.JsonArray
import no.nav.k9.endringslogg.plugins.harForcedModal
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ForcedModalTest {
    private val iDag = LocalDate.of(2026, 2, 28)

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
        assertTrue(harForcedModal(endring(date = iDag), iDag))
    }

    @Test
    fun `tvungen modal utløper en måned etter lansering`() {
        val lanseringsdato = LocalDate.of(2026, 1, 31)
        assertTrue(harForcedModal(endring(date = lanseringsdato), iDag))
        assertFalse(harForcedModal(endring(date = lanseringsdato), iDag.plusDays(1)))
    }

    @Test
    fun `utløpsdato fra Sanity overstyrer standard utløp`() {
        assertTrue(harForcedModal(endring(date = iDag.minusMonths(3), expiryDate = iDag.plusDays(1)), iDag))
        assertFalse(harForcedModal(endring(date = iDag, expiryDate = iDag.minusDays(1)), iDag))
    }

    @Test
    fun `modal som ikke er tvungen vises ikke som tvungen`() {
        assertFalse(harForcedModal(endring(date = iDag, modal = Modal("Nyhet", forcedModal = false, slides = emptyList())), iDag))
    }

    @Test
    fun `endring uten modal er ikke tvungen`() {
        assertFalse(harForcedModal(endring(date = iDag, modal = null), iDag))
    }

    @Test
    fun `endring uten dato bruker utløpsdato hvis den finnes`() {
        assertTrue(harForcedModal(endring(date = null, expiryDate = iDag.plusDays(1)), iDag))
        assertFalse(harForcedModal(endring(date = null, expiryDate = iDag.minusDays(1)), iDag))
    }

    @Test
    fun `endring uten dato og utløpsdato er ikke tvungen`() {
        assertFalse(harForcedModal(endring(date = null), iDag))
    }
}
