package ru.dankoy.otus.hibernate.hibernate.cacheddao;

import ru.dankoy.otus.hibernate.cache.CustomCache;
import ru.dankoy.otus.hibernate.cache.CustomCacheImpl;
import ru.dankoy.otus.hibernate.cache.CustomCacheListenerImpl;
import ru.dankoy.otus.hibernate.core.dao.AddressDataSetDao;
import ru.dankoy.otus.hibernate.core.model.AddressDataSet;
import ru.dankoy.otus.hibernate.core.model.User;
import ru.dankoy.otus.hibernate.core.sessionmanager.SessionManager;

import java.util.Optional;

/**
 * Не использовать. Кэш только у юзера
 */
public class CachedAddressDataSetDaoHibernate implements AddressDataSetDao {

    private final AddressDataSetDao addressDataSetDaoHibernate;
    private final CustomCache<Long, Optional<AddressDataSet>> cache;

    public CachedAddressDataSetDaoHibernate(AddressDataSetDao addressDataSetDaoHibernate) {
        this.addressDataSetDaoHibernate = addressDataSetDaoHibernate;
        this.cache = new CustomCacheImpl<>();
        this.cache.addListener(new CustomCacheListenerImpl<>());
    }


    @Override
    public Optional<AddressDataSet> findById(long id) {

        Optional<AddressDataSet> userFromCache = cache.get(id);
        if (userFromCache.isPresent()) {
            return addressDataSetDaoHibernate.findById(id);
        } else {
            return userFromCache;
        }

    }

    @Override
    public long insertAddress(AddressDataSet addressDataSet) {
        long addressId = addressDataSetDaoHibernate.insertAddress(addressDataSet);
        cache.put(addressId, Optional.ofNullable(addressDataSet));

        return addressId;
    }

    @Override
    public void updateAddress(AddressDataSet addressDataSet) {

        Optional<AddressDataSet> addressDataSetFromCache = cache.get(addressDataSet.getId());

        if (addressDataSetFromCache == null) {
            addressDataSetFromCache = findById(addressDataSet.getId());
        }
        if (addressDataSetFromCache.isEmpty()) {
            addressDataSetDaoHibernate.updateAddress(addressDataSet);
        } else {
            addressDataSetDaoHibernate.updateAddress(addressDataSetFromCache.get());
        }

    }

    @Override
    public void insertOrUpdate(AddressDataSet addressDataSet) {

    }

    @Override
    public SessionManager getSessionManager() {
        return addressDataSetDaoHibernate.getSessionManager();
    }
}
