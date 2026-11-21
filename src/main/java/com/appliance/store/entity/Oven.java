package com.appliance.store.entity;

import com.appliance.store.constant.EntityConstants;

import java.util.Objects;

public class Oven extends BaseEntity {
    private final int power;
    private final int capacity;

    public Oven(long id, String name, long price, int power, int capacity) {
        super(id, name, price);
        this.power = power;
        this.capacity = capacity;
    }

    public int getPower() {
        return power;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Oven oven)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return power == oven.power && capacity == oven.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), power, capacity);
    }

    @Override
    public String toString() {
        return EntityConstants.OVEN_CLASS_NAME
                + EntityConstants.TO_STRING_PREFIX
                + EntityConstants.PROP_ID + getId()
                + EntityConstants.PROP_NAME + getName() + EntityConstants.SINGLE_QUOTE
                + EntityConstants.PROP_PRICE + getPrice()
                + EntityConstants.PROP_POWER + power
                + EntityConstants.PROP_CAPACITY + capacity
                + EntityConstants.TO_STRING_SUFFIX;
    }
}

