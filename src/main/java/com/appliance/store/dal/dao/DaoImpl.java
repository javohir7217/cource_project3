package com.appliance.store.dal.dao;

import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.dal.datasource.DataSource;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaoImpl<T extends BaseEntity> implements Dao<T> {
    private final DataSource<T> dataSource;

    public DaoImpl(DataSource<T> dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.DATA_SOURCE_CANNOT_BE_NULL);
        }
        this.dataSource = dataSource;
    }

    @Override
    public List<T> findAll() throws DaoException {
        return dataSource.readAll();
    }

    @Override
    public List<T> find(SearchCriteria<T> criteria) throws DaoException {
        List<T> allItems = dataSource.readAll();
        if (criteria == null) {
            return allItems;
        }
        List<T> matchedItems = new ArrayList<>();
        for (T item : allItems) {
            if (criteria.matches(item)) {
                matchedItems.add(item);
            }
        }
        return Collections.unmodifiableList(matchedItems);
    }
}

