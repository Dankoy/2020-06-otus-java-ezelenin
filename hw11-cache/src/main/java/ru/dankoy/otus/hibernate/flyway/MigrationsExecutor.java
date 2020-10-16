package ru.dankoy.otus.hibernate.flyway;

public interface MigrationsExecutor {

    void cleanDb();
    void executeMigrations();

}
