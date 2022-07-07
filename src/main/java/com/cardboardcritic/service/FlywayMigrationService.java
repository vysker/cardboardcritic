package com.cardboardcritic.service;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class FlywayMigrationService {
    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    String url;
    @ConfigProperty(name = "quarkus.datasource.username")
    String username;
    @ConfigProperty(name = "quarkus.datasource.password")
    String password;

    public void migrate(@Observes StartupEvent event) {
        final Flyway flyway = Flyway.configure().dataSource("jdbc:" + url, username, password).load();
        flyway.baseline();
        flyway.migrate();
    }
}
