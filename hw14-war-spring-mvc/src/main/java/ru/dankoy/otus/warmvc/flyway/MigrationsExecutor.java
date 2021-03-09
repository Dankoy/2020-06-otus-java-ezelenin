package ru.dankoy.otus.warmvc.flyway;

public interface MigrationsExecutor {

    void cleanDb();
    void executeMigrations();

}
