package ru.dankoy.otus.jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jetty.cache.CustomCache;
import ru.dankoy.otus.jetty.cache.CustomCacheImpl;
import ru.dankoy.otus.jetty.cache.CustomCacheListener;
import ru.dankoy.otus.jetty.cache.CustomCacheListenerImpl;
import ru.dankoy.otus.jetty.core.dao.UserDao;
import ru.dankoy.otus.jetty.core.model.AddressDataSet;
import ru.dankoy.otus.jetty.core.model.PhoneDataSet;
import ru.dankoy.otus.jetty.core.model.User;
import ru.dankoy.otus.jetty.hibernate.cacheddao.CachedUserDaoHibernate;
import ru.dankoy.otus.jetty.hibernate.dao.UserDaoHibernate;
import ru.dankoy.otus.jetty.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.jetty.hibernate.utils.HibernateUtils;
import ru.dankoy.otus.jetty.service.FileSystemHelper;
import ru.dankoy.otus.jetty.service.TemplateProcessor;
import ru.dankoy.otus.jetty.service.TemplateProcessorImpl;
import ru.dankoy.otus.jetty.web.server.UsersWebServer;
import ru.dankoy.otus.jetty.web.server.UsersWebServerWithBasicAuth;

import java.util.ArrayList;
import java.util.List;

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
    private static UserDao userDaoWithCache;
    private static CustomCache<Long, User> cache;

    public static void main(String[] args) throws Exception {

        SessionManagerHibernate sessionManagerHibernate = getSessionManager();

        cache = new CustomCacheImpl<>();
        CustomCacheListener<Long, User> listener = new CustomCacheListenerImpl<>();
        cache.addListener(listener);

        // Создание юзеров в базе.
        insertUsersInDb(sessionManagerHibernate, 3);

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);
        UserDao cachedUserDaoHibernate = new CachedUserDaoHibernate(userDao, cache);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
                .create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper
                .localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicAuth(WEB_SERVER_PORT, loginService,
                cachedUserDaoHibernate,
                gson, templateProcessor, sessionManagerHibernate);

        usersWebServer.start();
        usersWebServer.join();


    }

    /**
     * Заполенение базы юзерами
     *
     * @param sessionManagerHibernate
     * @param amountOfUsers
     */
    private static void insertUsersInDb(SessionManagerHibernate sessionManagerHibernate, int amountOfUsers) {

        sessionManagerHibernate.beginSession();

        for (int i = 1; i <= amountOfUsers; i++) {

            UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);
            userDaoWithCache = new CachedUserDaoHibernate(userDao, cache);

            // Запись юзера в базу
            List<PhoneDataSet> phoneDataSets = new ArrayList<>();
            phoneDataSets.add(new PhoneDataSet("user" + i + " phone1"));
            phoneDataSets.add(new PhoneDataSet("user" + i + " phone2"));
            phoneDataSets.add(new PhoneDataSet("user" + i + " phone3"));
            phoneDataSets.add(new PhoneDataSet("user" + i + " phone4"));

            AddressDataSet addressDataSet = new AddressDataSet("user" + i + " nice address");
            User newUser = new User("name" + i, i, addressDataSet, phoneDataSets);
            addressDataSet.setUser(newUser);
            phoneDataSets.forEach(phone -> phone.setUser(newUser));
            var userId = userDaoWithCache.insertUser(newUser);

        }

        sessionManagerHibernate.commitSession();

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

}
