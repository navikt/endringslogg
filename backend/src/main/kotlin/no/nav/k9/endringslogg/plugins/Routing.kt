package no.nav.k9.endringslogg.plugins

import Err
import Ok
import SanityClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.encodeBase64
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import lagGroqQueryString
import no.nav.k9.endringslogg.BildeData
import no.nav.k9.endringslogg.BrukerData
import no.nav.k9.endringslogg.Endring
import no.nav.k9.endringslogg.SeenForcedStatus
import no.nav.k9.endringslogg.SeenStatus
import no.nav.k9.endringslogg.SessionDuration
import no.nav.k9.endringslogg.env.erIDev
import no.nav.k9.endringslogg.getSeenEntriesForUser
import no.nav.k9.endringslogg.getSeenForcedEntriesForUser
import no.nav.k9.endringslogg.insertSeenEntries
import no.nav.k9.endringslogg.insertSeenForcedEntries
import no.nav.k9.endringslogg.insertSessionDuration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

val logger: Logger = LoggerFactory.getLogger("no.nav.k9.endringslogg.routing")

// Må holdes i synk med schemaTypes i sanity/schemas/schema.ts.
private val GYLDIGE_APP_IDER = setOf("K9_SAK", "AAP")
private val GYLDIGE_DATASETT = setOf("production")
private const val MAKS_ANTALL_ENDRINGER = 200

private fun validerDatasett(dataset: String) = require(dataset in GYLDIGE_DATASETT) { "Ukjent dataset" }

internal fun lagEndringsloggQueryString(
    appId: String,
    dataset: String,
    maxEntries: Int,
    visUpubliserte: Boolean,
): String {
    require(appId in GYLDIGE_APP_IDER) { "Ukjent appId" }
    validerDatasett(dataset)
    require(maxEntries in 1..MAKS_ANTALL_ENDRINGER) { "maxEntries må være mellom 1 og $MAKS_ANTALL_ENDRINGER" }

    val filter = if (visUpubliserte) "_type == \$appId" else "_type == \$appId && publisert == true"
    return lagGroqQueryString(
        "*[$filter] | order(_createdAt desc)[0...\$maxEntries]",
        mapOf("appId" to JsonPrimitive(appId), "maxEntries" to JsonPrimitive(maxEntries)),
    )
}

fun Application.configureRouting(client: SanityClient) {
    routing {
        get("/isAlive") {
            call.respond(HttpStatusCode.OK)
        }
        get("/isReady") {
            call.respond(HttpStatusCode.OK)
        }
        post("/endringslogg") { // TODO: Deprecated
            fetchEndringslogger(client, withImages = true)
        }
        post("/endringslogg/text") {
            fetchEndringslogger(client, withImages = false)
        }
        post("/endringslogg/image") {
            fetchImage(client)
        }
        post("/analytics/sett-endringer") {
            val seen = call.receive<SeenStatus>()
            insertSeenEntries(seen.userId, seen.appId, seen.documentIds.map(UUID::fromString))
            call.respond(HttpStatusCode.OK)
        }

        post("/analytics/seen-forced-modal") {
            val seen = call.receive<SeenForcedStatus>()
            insertSeenForcedEntries(seen.userId, seen.documentIds.map(UUID::fromString))
            call.respond(HttpStatusCode.OK)
        }

        post("/analytics/session-duration") {
            val duration = call.receive<SessionDuration>()
            insertSessionDuration(duration)
            call.respond(HttpStatusCode.OK)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.fetchImage(client: SanityClient) {
    val (slideImageRef, dataSet) = call.receive<BildeData>()
    validerDatasett(dataSet)

    when (val result = client.fetchImage(slideImageRef, dataSet)) {
        is Ok -> call.respond(mapOf("slideImage" to Json.encodeToJsonElement(result.value.slideImage.encodeBase64())))

        is Err -> {
            val statuskode = result.error.response.status.value
            val feilmelding = result.error.message
            logger.info("Feil ved uthenting av bilde: $statuskode - $feilmelding")
            call.response.status(HttpStatusCode(statuskode, "Feilmelding: $feilmelding"))
        }

        else -> {
            logger.error("Noe uventet skjedde ved uthenting av bilde")
            call.response.status(HttpStatusCode(500, "Uventet feil ved uthenting av bilde"))
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.fetchEndringslogger(
    client: SanityClient,
    withImages: Boolean,
) {
    val (userId, appId, dataset, maxEntries) = call.receive<BrukerData>()
    val queryString = lagEndringsloggQueryString(appId, dataset, maxEntries, visUpubliserte = erIDev())
    val seenEntryIds = getSeenEntriesForUser(userId).map(UUID::toString).toSet()
    val seenForcedEntryIds = getSeenForcedEntriesForUser(userId).map(UUID::toString).toSet()

    when (val endringslogger = client.query(queryString, dataset, withImages)) {
        is Ok -> {
            if (endringslogger.value.result.isEmpty()) {
                call.response.status(HttpStatusCode(204, "Data for app $appId doesn't exist."))
            } else {
                call.respond(
                    endringslogger.value.result.map {
                        val erForcedModal = harForcedModal(it)

                        it.copy(
                            seen = it.id in seenEntryIds,
                            seenForced = it.id in seenForcedEntryIds,
                            forcedModal = erForcedModal,
                        )
                    },
                )
            }
        }

        is Err -> {
            val statuskode = endringslogger.error.response.status.value
            val feilmelding = endringslogger.error.message
            logger.info("Feil ved uthenting av endringslogg: $statuskode - $feilmelding")
            call.response.status(HttpStatusCode(statuskode, "Feilmelding: $feilmelding"))
        }

        else -> {
            logger.error("Noe uventet skjedde ved uthenting av endringslogg")
            call.response.status(HttpStatusCode(500, "Uventet feil ved uthenting av endringslogg"))
        }
    }
}

private fun harForcedModal(endring: Endring): Boolean {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val utløpsdatoSattISanity = endring.expiryDate?.let { datoString -> LocalDate.parse(datoString, dateTimeFormatter) }
    val endringsloggLansertDato = LocalDate.parse(endring.date, dateTimeFormatter)

    val utløpsdato = utløpsdatoSattISanity ?: endringsloggLansertDato.plusMonths(1)

    val utløpt = LocalDate.now().isAfter(utløpsdato)

    val forcedModal = endring.modal?.forcedModal ?: false
    return forcedModal && !utløpt
}
