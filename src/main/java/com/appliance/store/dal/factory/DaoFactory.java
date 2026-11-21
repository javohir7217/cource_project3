package com.appliance.store.dal.factory;

import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.dal.dao.Dao;
import com.appliance.store.dal.dao.DaoImpl;
import com.appliance.store.dal.datasource.CsvDataSource;
import com.appliance.store.dal.mapper.LaptopCsvRowMapper;
import com.appliance.store.dal.mapper.OvenCsvRowMapper;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.entity.Laptop;
import com.appliance.store.entity.Oven;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DaoFactory {
    private static final Map<Class<? extends BaseEntity>, Dao<? extends BaseEntity>> DAO_MAP = new ConcurrentHashMap<>();

    static {
        init();
    }

    private DaoFactory() {
    }

    public static synchronized void init(Map<Class<? extends BaseEntity>, Dao<? extends BaseEntity>> customDaoMap) {
        DAO_MAP.clear();
        if (customDaoMap != null) {
            DAO_MAP.putAll(customDaoMap);
        }
    }

    public static synchronized void init(String laptopCsvPath, String ovenCsvPath) {
        DAO_MAP.clear();
        DAO_MAP.put(Laptop.class, new DaoImpl<>(new CsvDataSource<>(laptopCsvPath, new LaptopCsvRowMapper())));
        DAO_MAP.put(Oven.class, new DaoImpl<>(new CsvDataSource<>(ovenCsvPath, new OvenCsvRowMapper())));
    }

    public static synchronized void init() {
        init(CsvSchemaConstants.DEFAULT_LAPTOPS_CSV, CsvSchemaConstants.DEFAULT_OVENS_CSV);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> Dao<T> getInstance(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.ENTITY_TYPE_CANNOT_BE_NULL);
        }
        Dao<? extends BaseEntity> dao = DAO_MAP.get(type);
        if (dao == null) {
            throw new IllegalStateException(ErrorMessageConstants.DAO_NOT_INITIALIZED_FOR_TYPE + type.getName());
        }
        return (Dao<T>) dao;
    }

    public static Collection<Dao<? extends BaseEntity>> getAllDaos() {
        return Collections.unmodifiableCollection(DAO_MAP.values());
    }

    public static Set<Class<? extends BaseEntity>> getRegisteredTypes() {
        return Collections.unmodifiableSet(DAO_MAP.keySet());
    }
}

