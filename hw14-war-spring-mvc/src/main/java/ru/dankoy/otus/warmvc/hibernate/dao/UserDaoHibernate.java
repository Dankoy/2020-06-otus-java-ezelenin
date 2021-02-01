package ru.dankoy.otus.warmvc.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.dankoy.otus.warmvc.core.dao.UserDao;
import ru.dankoy.otus.warmvc.core.dao.UserDaoException;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.core.sessionmanager.SessionManager;
import ru.dankoy.otus.warmvc.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.dankoy.otus.warmvc.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoHibernate implements UserDao {

    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    @Autowired
    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> rootEntry = criteriaQuery.from(User.class);
        CriteriaQuery<User> all = criteriaQuery.select(rootEntry);

        TypedQuery<User> allQuery = currentSession.getHibernateSession().createQuery(all);

        return allQuery.getResultList();

    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
