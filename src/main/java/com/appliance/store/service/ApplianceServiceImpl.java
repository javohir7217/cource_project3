package com.appliance.store.service;

import com.appliance.store.constant.CostConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.dal.dao.Dao;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.dal.factory.DaoFactory;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.service.exception.ServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplianceServiceImpl implements ApplianceService {

    public ApplianceServiceImpl() {
    }

    @Override
    public <T extends BaseEntity> List<T> find(SearchCriteria<T> criteria) throws ServiceException {
        if (criteria == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.CRITERIA_CANNOT_BE_NULL);
        }
        try {
            Dao<T> dao = DaoFactory.getInstance(criteria.getEntityType());
            return dao.find(criteria);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<BaseEntity> findAll(List<SearchCriteria<? extends BaseEntity>> criteriaList) throws ServiceException {
        if (criteriaList == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.CRITERIA_LIST_CANNOT_BE_NULL);
        }
        try {
            List<BaseEntity> combined = new ArrayList<>();
            for (SearchCriteria<? extends BaseEntity> criteria : criteriaList) {
                combined.addAll(queryDaoForCriteria(criteria));
            }
            return Collections.unmodifiableList(combined);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public long calculateCost(SearchCriteria<? extends BaseEntity> criteria) throws ServiceException {
        if (criteria == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.CRITERIA_CANNOT_BE_NULL);
        }
        List<? extends BaseEntity> items = find(criteria);
        return sumCost(items);
    }

    @Override
    public long calculateCost(List<SearchCriteria<? extends BaseEntity>> criteriaList) throws ServiceException {
        if (criteriaList == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.CRITERIA_LIST_CANNOT_BE_NULL);
        }
        List<BaseEntity> items = findAll(criteriaList);
        return sumCost(items);
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseEntity> List<T> queryDaoForCriteria(SearchCriteria<T> criteria) throws DaoException {
        Dao<T> dao = DaoFactory.getInstance(criteria.getEntityType());
        return dao.find(criteria);
    }

    private long sumCost(List<? extends BaseEntity> items) {
        long total = CostConstants.ZERO_COST;
        for (BaseEntity item : items) {
            total += item.getPrice();
        }
        return total;
    }
}

