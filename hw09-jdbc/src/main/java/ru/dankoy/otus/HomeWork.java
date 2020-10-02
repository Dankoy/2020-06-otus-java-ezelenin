package ru.dankoy.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.core.dao.UserDao;
import ru.dankoy.otus.core.model.User;
import ru.dankoy.otus.core.service.DbServiceUserImpl;
import ru.dankoy.otus.h2.DataSourceH2;
import ru.dankoy.otus.jdbc.DbExecutorImpl;
import ru.dankoy.otus.jdbc.dao.UserDaoJdbc;
import ru.dankoy.otus.jdbc.dao.UserDaoJdbcMapper;
import ru.dankoy.otus.jdbc.mapper.*;
import ru.dankoy.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

        EntityClassMetaData<User> entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);

        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl(dbExecutor, entityClassMetaData, sessionManager); //
//        UserDao userDao = null; // = new UserDaoJdbcMapper(sessionManager, dbExecutor);
        UserDao userDao = new UserDaoJdbcMapper(sessionManager, jdbcMapperUser);
//        UserDao userDao = new UserDaoJdbc(sessionManager, dbExecutor);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(1, "dbServiceUser"));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
// Работа со счетом


    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
