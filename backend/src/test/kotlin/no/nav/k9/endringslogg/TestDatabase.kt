package no.nav.k9.endringslogg

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.testcontainers.postgresql.PostgreSQLContainer

object TestDatabase {
    private val container = PostgreSQLContainer("postgres:17-alpine").apply { start() }

    init {
        Flyway.configure()
            .dataSource(container.jdbcUrl, container.username, container.password)
            .load()
            .migrate()
        Database.connect(container.jdbcUrl, user = container.username, password = container.password)
    }

    fun reset() =
        transaction {
            exec("TRUNCATE seen, seen_forced, user_session")
        }
}
