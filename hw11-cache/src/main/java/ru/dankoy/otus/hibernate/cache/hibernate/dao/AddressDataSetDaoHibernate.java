package ru.dankoy.otus.hibernate.cache.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.hibernate.cache.core.dao.AddressDataSetDao;
import ru.dankoy.otus.hibernate.cache.core.dao.UserDaoException;
import ru.dankoy.otus.hibernate.cache.core.model.AddressDataSet;
import ru.dankoy.otus.hibernate.cache.core.sessionmanager.SessionManager;
import ru.dankoy.otus.hibernate.cache.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.dankoy.otus.hibernate.cache.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class AddressDataSetDaoHibernate implements AddressDataSetDao {

    private static Logger logger = LoggerFactory.getLogger(AddressDataSetDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public AddressDataSetDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<AddressDataSet> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(AddressDataSet.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertAddress(AddressDataSet addressDataSet) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(addressDataSet);
            hibernateSession.flush();
            return addressDataSet.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateAddress(AddressDataSet addressDataSet) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(addressDataSet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(AddressDataSet addressDataSet) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(addressDataSet);
            hibernateSession.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
