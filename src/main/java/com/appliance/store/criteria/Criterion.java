package com.appliance.store.criteria;

@FunctionalInterface
public interface Criterion<T> {
    boolean matches(T entity);
}

