package ru.dankoy.otus.jetty.flyway;

public interface MigrationsExecutor {

    void cleanDb();
    void executeMigrations();

}
