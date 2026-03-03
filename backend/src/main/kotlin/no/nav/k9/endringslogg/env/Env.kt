package no.nav.k9.endringslogg.env

val DB_JDBC_URL: String = System.getenv("NAIS_DATABASE_ENDRINGSLOGG_ENDRINGSLOGG_JDBC_URL")
const val SANITY_PROJECT_ID: String = "avzz8jwq"

fun erIDev() = System.getenv("NAIS_CLUSTER_NAME") == "dev-gcp"

fun erIProd() = System.getenv("NAIS_CLUSTER_NAME") == "prod-gcp"
