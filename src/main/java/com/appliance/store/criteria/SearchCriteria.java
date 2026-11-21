package com.appliance.store.criteria;

import com.appliance.store.constant.CriteriaConstants;
import com.appliance.store.entity.BaseEntity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class SearchCriteria<T extends BaseEntity> {
    private final Class<T> entityType;
    private final Map<String, Criterion<T>> criteriaMap = new LinkedHashMap<>();

    public SearchCriteria(Class<T> entityType) {
        if (entityType == null) {
            throw new IllegalArgumentException(CriteriaConstants.ERROR_ENTITY_TYPE_NULL);
        }
        this.entityType = entityType;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    public SearchCriteria<T> addExactMatch(String fieldName, Function<T, ?> extractor, Object expectedValue) {
        validateInputs(fieldName, extractor);
        criteriaMap.put(fieldName, entity -> {
            if (entity == null) {
                return false;
            }
            Object actual = extractor.apply(entity);
            return Objects.equals(actual, expectedValue);
        });
        return this;
    }

    public SearchCriteria<T> addCaseInsensitiveStringMatch(String fieldName, Function<T, String> extractor, String expectedValue) {
        validateInputs(fieldName, extractor);
        criteriaMap.put(fieldName, entity -> {
            if (entity == null || expectedValue == null) {
                return false;
            }
            String actual = extractor.apply(entity);
            return actual != null && actual.equalsIgnoreCase(expectedValue);
        });
        return this;
    }

    public <V extends Comparable<V>> SearchCriteria<T> addRangeFilter(String fieldName, Function<T, V> extractor, Range<V> range) {
        validateInputs(fieldName, extractor);
        if (range == null) {
            throw new IllegalArgumentException(CriteriaConstants.ERROR_RANGE_NULL);
        }
        criteriaMap.put(fieldName, entity -> {
            if (entity == null) {
                return false;
            }
            V actual = extractor.apply(entity);
            return range.contains(actual);
        });
        return this;
    }

    public boolean matches(T entity) {
        if (entity == null) {
            return false;
        }
        for (Criterion<T> criterion : criteriaMap.values()) {
            if (!criterion.matches(entity)) {
                return false;
            }
        }
        return true;
    }

    public Set<String> getFieldNames() {
        return Collections.unmodifiableSet(criteriaMap.keySet());
    }

    public int getCriteriaCount() {
        return criteriaMap.size();
    }

    public boolean hasCriteria() {
        return !criteriaMap.isEmpty();
    }

    private void validateInputs(String fieldName, Function<T, ?> extractor) {
        if (fieldName == null) {
            throw new IllegalArgumentException(CriteriaConstants.ERROR_FIELD_NAME_NULL);
        }
        if (extractor == null) {
            throw new IllegalArgumentException(CriteriaConstants.ERROR_EXTRACTOR_NULL);
        }
    }
}

