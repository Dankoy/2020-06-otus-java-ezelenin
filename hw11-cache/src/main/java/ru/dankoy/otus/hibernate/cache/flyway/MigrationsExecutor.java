package ru.dankoy.otus.hibernate.cache.flyway;

public interface MigrationsExecutor {

    void cleanDb();
    void executeMigrations();

}
