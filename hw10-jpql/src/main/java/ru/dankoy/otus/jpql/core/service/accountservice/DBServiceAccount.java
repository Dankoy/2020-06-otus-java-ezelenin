package ru.dankoy.otus.jpql.core.service.accountservice;

import ru.dankoy.otus.jpql.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

    long saveAccount(Account account);

    Optional<Account> getAccount(long no);

}
