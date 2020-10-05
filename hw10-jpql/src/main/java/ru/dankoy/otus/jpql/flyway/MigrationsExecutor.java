package ru.dankoy.otus.jpql.flyway;

public interface MigrationsExecutor {

    void cleanDb();
    void executeMigrations();

}
