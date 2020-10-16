package ru.dankoy.otus.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.hibernate.cache.CustomCache;
import ru.dankoy.otus.hibernate.cache.CustomCacheImpl;
import ru.dankoy.otus.hibernate.cache.CustomCacheListener;
import ru.dankoy.otus.hibernate.cache.CustomCacheListenerImpl;
import ru.dankoy.otus.hibernate.core.dao.AddressDataSetDao;
import ru.dankoy.otus.hibernate.core.dao.PhoneDataSetDao;
import ru.dankoy.otus.hibernate.core.dao.UserDao;
import ru.dankoy.otus.hibernate.core.model.AddressDataSet;
import ru.dankoy.otus.hibernate.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.core.model.User;
import ru.dankoy.otus.hibernate.core.service.userservice.DBServiceUser;
import ru.dankoy.otus.hibernate.core.service.userservice.DbServiceUserCacheImpl;
import ru.dankoy.otus.hibernate.core.service.userservice.DbServiceUserImpl;
import ru.dankoy.otus.hibernate.hibernate.cacheddao.CachedUserDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.dao.AddressDataSetDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.dao.PhoneDataSetDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.dao.UserDaoHibernate;
import ru.dankoy.otus.hibernate.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.hibernate.hibernate.utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static UserDao userDaoWithCache;

    public static void main(String[] args) {

        SessionManagerHibernate sessionManagerHibernate = getSessionManager();

        // использование Hibernate с кэшем. Работает только внутри одной транзакции. Можно сохранить юзера и получить
        // его. Обновления не работают так как при операции update вызывается метод persist на объект которого нет в
        // кэше 1 уровня.

        // Пример с использованием проксей ru.dankoy.otus.hibernate.hibernate.cacheddao.CachedUserDaoHibernate
//        var userId = insertUserWithoutCache(sessionManagerHibernate);
//        var userId2 = insertUserWithCache(sessionManagerHibernate);
//        updateAddressWithCache(sessionManagerHibernate, userId2);
//        updatePhoneWithCache(sessionManagerHibernate, userId2);


        // Пример с DBService
        CustomCache<Long, Optional<User>> cache = new CustomCacheImpl<>();
        CustomCacheListener<Long, Optional<User>> listener = new CustomCacheListenerImpl<>();
        cache.addListener(listener);

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        DBServiceUser dbServiceUserCache = new DbServiceUserCacheImpl(dbServiceUser, cache);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3"));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));

        var userId = dbServiceUserCache.saveUser(newUser);

        Optional<User> foundUser = dbServiceUserCache.getUser(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser.getName()),
                () -> logger.info("user was not found")
        );

    }

// Все методы без кэша

    /**
     * обавление нового телефона к юзеру
     *
     * @param sessionManagerHibernate
     * @param userId
     */
    private static void updatePhoneWithoutCache(SessionManagerHibernate sessionManagerHibernate, long userId) {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);

        if (foundUser.isPresent()) {
            var phones = foundUser.get().getPhoneDataSets();

            PhoneDataSet newPhone = new PhoneDataSet("new phone");
            newPhone.setUser(foundUser.get());

            phones.add(newPhone);

            PhoneDataSetDao phoneDataSetDao = new PhoneDataSetDaoHibernate(sessionManagerHibernate);

            phoneDataSetDao.insertPhone(newPhone);

            sessionManagerHibernate.commitSession();
        }

    }

    /**
     * Обновление адреса у существующего юзера
     *
     * @param sessionManagerHibernate
     * @param userId
     */
    private static void updateAddressWithoutCache(SessionManagerHibernate sessionManagerHibernate, long userId) {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);

        if (foundUser.isPresent()) {
            var address = foundUser.get().getAddress();

            address.setStreet("updated street");

            AddressDataSetDao addressDataSetDao = new AddressDataSetDaoHibernate(sessionManagerHibernate);

            addressDataSetDao.updateAddress(address);

            sessionManagerHibernate.commitSession();
        }

    }

    /**
     * Запись юзера в базу и получение записанного юзера из базы используя обычный Hibernate Dao
     *
     * @param sessionManagerHibernate
     */
    private static long insertUserWithoutCache(SessionManagerHibernate sessionManagerHibernate) {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = List.of(new PhoneDataSet("phone1"), new PhoneDataSet("phone2"),
                new PhoneDataSet("phone3"));
        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));


        var userId = userDao.insertUser(newUser);

        logger.info("-------------------------------- ");

        // Получение юзера из базы
        Optional<User> foundUser = userDao.findById(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser.getName()),
                () -> logger.info("user was not found")
        );

        sessionManagerHibernate.commitSession();

        return userId;

    }

    // Все методы с кэшом

    /**
     * Запись юзера в базу и получение записанного юзера из базы используя Dao Hibernate с кастомным кэшем
     *
     * @param sessionManagerHibernate
     */
    private static long insertUserWithCache(SessionManagerHibernate sessionManagerHibernate) {

        sessionManagerHibernate.beginSession();

        UserDao userDao = new UserDaoHibernate(sessionManagerHibernate);
        userDaoWithCache = new CachedUserDaoHibernate(userDao);

        // Запись юзера в базу
        List<PhoneDataSet> phoneDataSets = new ArrayList<>();
        phoneDataSets.add(new PhoneDataSet("phone1"));
        phoneDataSets.add(new PhoneDataSet("phone2"));
        phoneDataSets.add(new PhoneDataSet("phone3"));
        phoneDataSets.add(new PhoneDataSet("phone4"));

        AddressDataSet addressDataSet = new AddressDataSet("nice address");
        User newUser = new User("name", 12, addressDataSet, phoneDataSets);
        addressDataSet.setUser(newUser);
        phoneDataSets.forEach(phone -> phone.setUser(newUser));


        var userId = userDaoWithCache.insertUser(newUser);

        logger.info("-------------------------------- ");

        // Получение юзера из базы
        Optional<User> foundUser = userDaoWithCache.findById(userId);
        foundUser.ifPresentOrElse(
                crUser -> logger.info("Found user, name:{}", crUser.getName()),
                () -> logger.info("user was not found")
        );

        sessionManagerHibernate.commitSession();

        return userId;

    }

    /**
     * обавление нового телефона к юзеру
     *
     * @param sessionManagerHibernate
     * @param userId
     */
    private static void updatePhoneWithCache(SessionManagerHibernate sessionManagerHibernate, long userId) {

        sessionManagerHibernate.beginSession();

        // Получение юзера из базы
        Optional<User> foundUser = userDaoWithCache.findById(userId);

        if (foundUser.isPresent()) {
            var phones = foundUser.get().getPhoneDataSets();

            PhoneDataSet newPhone = new PhoneDataSet("new phone");
            newPhone.setUser(foundUser.get());

            phones.add(newPhone);

            PhoneDataSetDao phoneDataSetDao = new PhoneDataSetDaoHibernate(sessionManagerHibernate);

            phoneDataSetDao.insertPhone(newPhone);

            sessionManagerHibernate.commitSession();
        }

    }

    /**
     * Обновление адреса у существующего юзера
     *
     * @param sessionManagerHibernate
     * @param userId
     */
    private static void updateAddressWithCache(SessionManagerHibernate sessionManagerHibernate, long userId) {

        sessionManagerHibernate.beginSession();

        // Получение юзера из базы
        Optional<User> foundUser = userDaoWithCache.findById(userId);

        if (foundUser.isPresent()) {
            var address = foundUser.get().getAddress();

            address.setStreet("updated street");

            AddressDataSetDao addressDataSetDao = new AddressDataSetDaoHibernate(sessionManagerHibernate);

            addressDataSetDao.updateAddress(address);

            sessionManagerHibernate.commitSession();
        }

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
