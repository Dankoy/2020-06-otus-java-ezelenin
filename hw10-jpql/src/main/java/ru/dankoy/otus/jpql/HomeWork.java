package ru.dankoy.otus.jpql;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jpql.core.model.AddressDataSet;
import ru.dankoy.otus.jpql.core.model.PhoneDataSet;
import ru.dankoy.otus.jpql.core.model.User;
import ru.dankoy.otus.jpql.core.sessionmanager.SessionManager;
import ru.dankoy.otus.jpql.flyway.MigrationsExecutor;
import ru.dankoy.otus.jpql.flyway.MigrationsExecutorFlyway;
import ru.dankoy.otus.jpql.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.jpql.hibernate.utils.HibernateUtils;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

//        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
//        migrationsExecutor.cleanDb();
//        migrationsExecutor.executeMigrations();


        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        SessionManager sessionManager = new SessionManagerHibernate(sessionFactory);



    }
}
