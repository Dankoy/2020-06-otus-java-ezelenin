package ru.dankoy.otus.hibernate.cache.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.hibernate.cache.core.dao.PhoneDataSetDao;
import ru.dankoy.otus.hibernate.cache.core.dao.UserDaoException;
import ru.dankoy.otus.hibernate.cache.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.cache.core.sessionmanager.SessionManager;
import ru.dankoy.otus.hibernate.cache.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.dankoy.otus.hibernate.cache.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class PhoneDataSetDaoHibernate implements PhoneDataSetDao {

    private static Logger logger = LoggerFactory.getLogger(PhoneDataSetDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public PhoneDataSetDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<PhoneDataSet> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(PhoneDataSet.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertPhone(PhoneDataSet phoneDataSet) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(phoneDataSet);
            hibernateSession.flush();
            return phoneDataSet.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updatePhone(PhoneDataSet phoneDataSet) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(phoneDataSet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(PhoneDataSet phoneDataSet) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(phoneDataSet);
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
