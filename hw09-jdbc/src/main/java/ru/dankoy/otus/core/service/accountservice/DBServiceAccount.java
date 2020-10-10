package ru.dankoy.otus.core.service.accountservice;

import ru.dankoy.otus.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

    long saveAccount(Account account);

    Optional<Account> getAccount(long no);

}
