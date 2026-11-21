package com.appliance.store.service.util;

import com.appliance.store.constant.ErrorMessageConstants;

import java.util.function.Supplier;

public class SingletonHolder<T> {
    private final Supplier<T> supplier;
    private volatile T instance;

    public SingletonHolder(Supplier<T> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.SUPPLIER_CANNOT_BE_NULL);
        }
        this.supplier = supplier;
    }

    public T get() {
        T localRef = instance;
        if (localRef == null) {
            synchronized (this) {
                localRef = instance;
                if (localRef == null) {
                    instance = localRef = supplier.get();
                }
            }
        }
        return localRef;
    }

    public synchronized void reset() {
        instance = null;
    }
}

