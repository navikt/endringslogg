package no.nav.k9.endringslogg

import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DbConnectorTest {
    @BeforeTest
    fun setup() {
        TestDatabase.reset()
    }

    @Test
    fun `henter bare sette endringer for riktig bruker`() {
        val doc1 = UUID.randomUUID()
        val doc2 = UUID.randomUUID()

        insertSeenEntries("Z111111", "K9", listOf(doc1, doc2))
        insertSeenEntries("Z222222", "K9", listOf(doc2))

        assertEquals(setOf(doc1, doc2), getSeenEntriesForUser("Z111111").toSet())
        assertEquals(listOf(doc2), getSeenEntriesForUser("Z222222"))
    }

    @Test
    fun `ignorerer duplikate sette endringer`() {
        val doc = UUID.randomUUID()

        insertSeenEntries("Z111111", "K9", listOf(doc))
        insertSeenEntries("Z111111", "K9", listOf(doc))

        assertEquals(listOf(doc), getSeenEntriesForUser("Z111111"))
    }

    @Test
    fun `ignorerer duplikate sette tvungne modaler`() {
        val doc = UUID.randomUUID()

        insertSeenForcedEntries("Z111111", listOf(doc))
        insertSeenForcedEntries("Z111111", listOf(doc))

        assertEquals(listOf(doc), getSeenForcedEntriesForUser("Z111111"))
        assertEquals(emptyList(), getSeenForcedEntriesForUser("Z222222"))
    }

    @Test
    fun `lagrer aldri bruker-ID i klartekst`() {
        insertSeenEntries("Z111111", "K9", listOf(UUID.randomUUID()))
        insertSeenForcedEntries("Z111111", listOf(UUID.randomUUID()))
        insertSessionDuration(SessionDuration("Z111111", "K9", duration = 10, unseenFields = 2))

        val lagredeIder =
            transaction {
                Seen.selectAll().map { it[Seen.userId] } +
                    SeenForced.selectAll().map { it[SeenForced.userId] } +
                    UserSession.selectAll().map { it[UserSession.userId] }
            }

        assertEquals(3, lagredeIder.size)
        assertEquals(setOf(sha256("Z111111")), lagredeIder.toSet())
    }
}
