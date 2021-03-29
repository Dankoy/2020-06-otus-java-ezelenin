package ru.dankoy.otus.diploma.core.hibernate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.diploma.core.dao.CrashDao;
import ru.dankoy.otus.diploma.core.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.dankoy.otus.diploma.core.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.diploma.core.model.Crash;
import ru.dankoy.otus.diploma.core.sessionmanager.SessionManager;
import ru.dankoy.otus.diploma.core.sessionmanager.SessionManagerException;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CrashDaoHibernateImpl implements CrashDao {

    private static Logger logger = LoggerFactory.getLogger(CrashDaoHibernateImpl.class);

    private final SessionManagerHibernate sessionManager;

    public CrashDaoHibernateImpl(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    /**
     * Получает все записи в таблице
     *
     * @return
     * @throws SessionManagerException
     */
    @Override
    public List<Crash> getAllCrashes() {

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();

        CriteriaQuery<Crash> criteriaQuery = criteriaBuilder.createQuery(Crash.class);
        Root<Crash> rootEntry = criteriaQuery.from(Crash.class);
        CriteriaQuery<Crash> all = criteriaQuery.select(rootEntry);

        TypedQuery<Crash> allQuery = currentSession.getHibernateSession().createQuery(all);

        return allQuery.getResultList();

    }

    /**
     * Получает записи у которых в столбце related_non_motorist значение не равно null. Таким образом в результат
     * попадают все ДТП связанные с пешеходами и прочими НЕ водителями.
     *
     * @return
     * @throws SessionManagerException
     */
    @Override
    public List<Crash> getAllCrashesWithNonMotorists() {

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();

        CriteriaQuery<Crash> criteriaQuery = criteriaBuilder.createQuery(Crash.class);
        Root<Crash> rootEntry = criteriaQuery.from(Crash.class);
        CriteriaQuery<Crash> relatedNonMotorist =
                criteriaQuery.select(rootEntry).where(criteriaBuilder.isNotNull(rootEntry.get("relatedNonMotorist")));

        TypedQuery<Crash> crashTypedQuery = currentSession.getHibernateSession().createQuery(relatedNonMotorist);

        return crashTypedQuery.getResultList();

    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
