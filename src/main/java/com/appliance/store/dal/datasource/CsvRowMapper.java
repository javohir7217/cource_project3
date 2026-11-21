package com.appliance.store.dal.datasource;

import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.BaseEntity;

@FunctionalInterface
public interface CsvRowMapper<T extends BaseEntity> {
    T mapRow(String[] columns) throws DaoException;
}

