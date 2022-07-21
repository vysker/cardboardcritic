package com.cardboardcritic.db;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * This is a workaround for the fact that Quarkus Flyway does not support reactive database connections.
 */
@ApplicationScoped
public class FlywayMigrationService {
    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    String url;
    @ConfigProperty(name = "quarkus.datasource.username")
    String username;
    @ConfigProperty(name = "quarkus.datasource.password")
    String password;
    @ConfigProperty(name = "quarkus.flyway.baseline-on-migrate")
    boolean baselineOnMigrate;
    @ConfigProperty(name = "quarkus.flyway.migrate-at-start")
    boolean migrateAtStart;

    public void migrate(@Observes StartupEvent event) {
        final Flyway flyway = Flyway.configure().dataSource("jdbc:" + url, username, password).load();

        if (baselineOnMigrate)
            flyway.baseline();
        if (migrateAtStart)
            flyway.migrate();
    }
}
