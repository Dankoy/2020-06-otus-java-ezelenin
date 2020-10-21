package ru.dankoy.otus.jetty.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.dankoy.otus.jetty.core.sessionmanager.DatabaseSession;

import javax.persistence.criteria.CriteriaBuilder;


public class DatabaseSessionHibernate implements DatabaseSession {
    private final Session session;
    private final Transaction transaction;

    DatabaseSessionHibernate(Session session) {
        this.session = session;
        this.transaction = session.beginTransaction();
    }

    public Session getHibernateSession() {
        return session;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void close() {
        if (transaction.isActive()) {
            transaction.commit();
        }
        session.close();
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return session.getCriteriaBuilder();
    }

}
