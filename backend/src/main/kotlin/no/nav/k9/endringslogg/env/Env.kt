package no.nav.k9.endringslogg.env

import java.net.URI

val DB_USERNAME: String = System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_USERNAME")
val DB_PASSWORD: String = System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_PASSWORD")

val DB_JDBC_URL: String = run {
    val uri = URI(System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_URL"))
    val query = uri.rawQuery?.let { "?$it" } ?: ""
    "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}$query"
}
const val SANITY_PROJECT_ID: String = "avzz8jwq"

fun erIDev() = System.getenv("NAIS_CLUSTER_NAME") == "dev-gcp"

fun erIProd() = System.getenv("NAIS_CLUSTER_NAME") == "prod-gcp"
