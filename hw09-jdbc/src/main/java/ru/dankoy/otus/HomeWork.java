package ru.dankoy.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.core.dao.AccountDao;
import ru.dankoy.otus.core.dao.UserDao;
import ru.dankoy.otus.core.model.Account;
import ru.dankoy.otus.core.model.User;
import ru.dankoy.otus.core.service.accountservice.DbServiceAccountImpl;
import ru.dankoy.otus.core.service.userservice.DbServiceUserImpl;
import ru.dankoy.otus.h2.DataSourceH2;
import ru.dankoy.otus.jdbc.DbExecutorImpl;
import ru.dankoy.otus.jdbc.dao.AccountDaoJdbcMapper;
import ru.dankoy.otus.jdbc.dao.UserDaoJdbcMapper;
import ru.dankoy.otus.jdbc.mapper.*;
import ru.dankoy.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

        EntityClassMetaData<User> userClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        EntitySQLMetaData userSQLMetaData = new EntitySQLMetaDataImpl(userClassMetaData);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl(dbExecutor, userClassMetaData, sessionManager); //
        UserDao userDao = new UserDaoJdbcMapper(sessionManager, jdbcMapperUser);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(1, "dbServiceUser", 2));
        logger.info("Saved user id: {}", id);

        logger.info("Trying to get user by Id: {}", id);
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
// Работа со счетом

        EntityClassMetaData<Account> accountClassMetaData = new EntityClassMetaDataImpl<>(Account.class);
        EntitySQLMetaData accountSQLMetaData = new EntitySQLMetaDataImpl(accountClassMetaData);

        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl(dbExecutor, accountClassMetaData, sessionManager);
        AccountDao accountDao = new AccountDaoJdbcMapper(sessionManager, jdbcMapperAccount);

        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        var no = dbServiceAccount.saveAccount(new Account(1234, "account", BigDecimal.valueOf(23)));
        logger.info("Saved accound id: {}", no);

        logger.info("Trying to get accound by Id: {}", no);
        Optional<Account> account = dbServiceAccount.getAccount(no);

        account.ifPresentOrElse(
                crAccount -> logger.info("created account, name:{}", crAccount.getNo()),
                () -> logger.info("account was not created")
        );


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
