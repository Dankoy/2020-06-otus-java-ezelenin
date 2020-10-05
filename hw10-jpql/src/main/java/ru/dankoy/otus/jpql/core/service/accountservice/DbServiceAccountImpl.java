package ru.dankoy.otus.jpql.core.service.accountservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jpql.core.dao.AccountDao;
import ru.dankoy.otus.jpql.core.model.Account;
import ru.dankoy.otus.jpql.core.service.DbServiceException;

import java.util.Optional;

public class DbServiceAccountImpl implements DBServiceAccount {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var accountNo = accountDao.insertAccount(account);
                sessionManager.commitSession();

                logger.info("created account: {}", accountNo);
                return accountNo;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long no) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> userOptional = accountDao.findById(no);

                logger.info("account: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

}
