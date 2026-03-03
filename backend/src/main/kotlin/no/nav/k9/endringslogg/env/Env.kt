package no.nav.k9.endringslogg.env

val DB_URL: String = System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_URL")
val DB_USERNAME: String = System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_USERNAME")
val DB_PASSWORD: String = System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_PASSWORD")
const val SANITY_PROJECT_ID: String = "avzz8jwq"

fun erIDev() = System.getenv("NAIS_CLUSTER_NAME") == "dev-gcp"

fun erIProd() = System.getenv("NAIS_CLUSTER_NAME") == "prod-gcp"
