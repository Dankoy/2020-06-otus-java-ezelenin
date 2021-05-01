package ru.dankoy.otus.diploma.flyway;

public interface MigrationsExecutor {

    void cleanDb();

    void executeMigrations();

}
