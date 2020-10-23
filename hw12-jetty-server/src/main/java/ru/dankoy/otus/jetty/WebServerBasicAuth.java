package ru.dankoy.otus.jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jetty.cache.CustomCache;
import ru.dankoy.otus.jetty.cache.CustomCacheImpl;
import ru.dankoy.otus.jetty.cache.CustomCacheListener;
import ru.dankoy.otus.jetty.cache.CustomCacheListenerImpl;
import ru.dankoy.otus.jetty.core.model.AddressDataSet;
import ru.dankoy.otus.jetty.core.model.PhoneDataSet;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.core.service.userservice.DbServiceUserCacheImpl;
import ru.dankoy.otus.jetty.core.service.userservice.DbServiceUserImpl;
import ru.dankoy.otus.jetty.h2.DataSourceH2;
import ru.dankoy.otus.jetty.hibernate.dao.UserDaoHibernate;
import ru.dankoy.otus.jetty.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.jetty.hibernate.utils.HibernateUtils;
import ru.dankoy.otus.jetty.service.FileSystemHelper;
import ru.dankoy.otus.jetty.service.TemplateProcessor;
import ru.dankoy.otus.jetty.service.TemplateProcessorImpl;
import ru.dankoy.otus.jetty.web.server.UsersWebServer;
import ru.dankoy.otus.jetty.web.server.UsersWebServerWithBasicAuth;

import javax.sql.DataSource;

/*

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/

public class WebServerBasicAuth {
    private static final Logger logger = LoggerFactory.getLogger(WebServerBasicAuth.class);

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "admin";
    public static final int MAX_INACTIVE_INTERVAL = 10;
    private static final CustomCache<Long, User> cache = new CustomCacheImpl<>();

    public static void main(String[] args) throws Exception {

        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);

        SessionManagerHibernate sessionManagerHibernate = getSessionManager();

        CustomCacheListener<Long, User> listener = new CustomCacheListenerImpl<>();
        cache.addListener(listener);

        var userDao = new UserDaoHibernate(sessionManagerHibernate);
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var cachedDbServiceUser = new DbServiceUserCacheImpl(dbServiceUser, cache);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
                .create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper
                .localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicAuth(WEB_SERVER_PORT, loginService,
                cachedDbServiceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();


    }

    /**
     * Получение менеджера сессий hibernate
     *
     * @return
     */
    private static SessionManagerHibernate getSessionManager() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class,
                AddressDataSet.class, PhoneDataSet.class);
        return new SessionManagerHibernate(sessionFactory);
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
