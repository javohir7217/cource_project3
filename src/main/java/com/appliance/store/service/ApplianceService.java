package com.appliance.store.service;

import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.service.exception.ServiceException;

import java.util.List;

public interface ApplianceService {
    <T extends BaseEntity> List<T> find(SearchCriteria<T> criteria) throws ServiceException;

    List<BaseEntity> findAll(List<SearchCriteria<? extends BaseEntity>> criteriaList) throws ServiceException;

    long calculateCost(SearchCriteria<? extends BaseEntity> criteria) throws ServiceException;

    long calculateCost(List<SearchCriteria<? extends BaseEntity>> criteriaList) throws ServiceException;
}
