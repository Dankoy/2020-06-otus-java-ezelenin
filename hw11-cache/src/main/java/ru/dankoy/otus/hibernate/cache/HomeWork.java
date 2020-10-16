package ru.dankoy.otus.hibernate.cache;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.hibernate.cache.core.dao.AddressDataSetDao;
import ru.dankoy.otus.hibernate.cache.core.dao.PhoneDataSetDao;
import ru.dankoy.otus.hibernate.cache.core.dao.UserDao;
import ru.dankoy.otus.hibernate.cache.core.model.AddressDataSet;
import ru.dankoy.otus.hibernate.cache.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.cache.core.model.User;
import ru.dankoy.otus.hibernate.cache.hibernate.dao.AddressDataSetDaoHibernate;
import ru.dankoy.otus.hibernate.cache.hibernate.dao.PhoneDataSetDaoHibernate;
import ru.dankoy.otus.hibernate.cache.hibernate.dao.UserDaoHibernate;
import ru.dankoy.otus.hibernate.cache.hibernate.sessionmanager.SessionManagerHibernate;
import ru.dankoy.otus.hibernate.cache.hibernate.utils.HibernateUtils;

import java.util.List;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

        SessionManagerHibernate sessionManagerHibernate = getSessionManager();

        var userId = insertUser(sessionManagerHibernate);

        updateAddress(sessionManagerHibernate, userId);
        updatePhone(sessionManagerHibernate, userId);


    }


    /**
     * обавление нового телефона к юзеру
     *
     * @param sessionManagerHibernate
     * @param userId
     */
    private static void updatePhone(SessionManagerHibernate sessionManagerHibernate, long userId) {

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
    private static void updateAddress(SessionManagerHibernate sessionManagerHibernate, long userId) {

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
     * Запись юзера в базу и получение записанного юзера из базы
     *
     * @param sessionManagerHibernate
     */
    private static long insertUser(SessionManagerHibernate sessionManagerHibernate) {

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
