package com.appliance.store.dal.datasource;

import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.BaseEntity;

import java.util.List;

public interface DataSource<T extends BaseEntity> {
    List<T> readAll() throws DaoException;
}

