package com.appliance.store.dal.dao;

import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.BaseEntity;

import java.util.List;

public interface Dao<T extends BaseEntity> {
    List<T> findAll() throws DaoException;
    List<T> find(SearchCriteria<T> criteria) throws DaoException;
}

