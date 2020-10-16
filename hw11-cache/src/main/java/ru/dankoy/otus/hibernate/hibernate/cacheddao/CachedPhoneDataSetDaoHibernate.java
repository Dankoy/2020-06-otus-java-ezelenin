package ru.dankoy.otus.hibernate.hibernate.cacheddao;

import ru.dankoy.otus.hibernate.cache.CustomCache;
import ru.dankoy.otus.hibernate.cache.CustomCacheImpl;
import ru.dankoy.otus.hibernate.cache.CustomCacheListenerImpl;
import ru.dankoy.otus.hibernate.core.dao.PhoneDataSetDao;
import ru.dankoy.otus.hibernate.core.model.PhoneDataSet;
import ru.dankoy.otus.hibernate.core.sessionmanager.SessionManager;

import java.util.Optional;

public class CachedPhoneDataSetDaoHibernate implements PhoneDataSetDao {

    private final PhoneDataSetDao phoneDataSetDao;
    private final CustomCache<Long, Optional<PhoneDataSet>> cache;

    public CachedPhoneDataSetDaoHibernate(PhoneDataSetDao phoneDataSetDao) {
        this.phoneDataSetDao = phoneDataSetDao;
        this.cache = new CustomCacheImpl<>();
        this.cache.addListener(new CustomCacheListenerImpl<>());
    }


    @Override
    public Optional<PhoneDataSet> findById(long id) {

        Optional<PhoneDataSet> userFromCache = cache.get(id);
        if (userFromCache.isPresent()) {
            return phoneDataSetDao.findById(id);
        } else {
            return userFromCache;
        }

    }

    @Override
    public long insertPhone(PhoneDataSet phoneDataSet) {
        long addressId = phoneDataSetDao.insertPhone(phoneDataSet);
        cache.put(addressId, Optional.ofNullable(phoneDataSet));

        return addressId;
    }

    @Override
    public void updatePhone(PhoneDataSet phoneDataSet) {

    }

    @Override
    public void insertOrUpdate(PhoneDataSet phoneDataSet) {

    }


    @Override
    public SessionManager getSessionManager() {
        return phoneDataSetDao.getSessionManager();
    }
}
