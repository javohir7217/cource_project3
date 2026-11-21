package com.appliance.store.entity;

import com.appliance.store.constant.EntityConstants;

import java.util.Objects;

public class Laptop extends BaseEntity {
    private final String os;
    private final String cpu;

    public Laptop(long id, String name, long price, String os, String cpu) {
        super(id, name, price);
        this.os = os;
        this.cpu = cpu;
    }

    public String getOs() {
        return os;
    }

    public String getCpu() {
        return cpu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Laptop laptop)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(os, laptop.os) && Objects.equals(cpu, laptop.cpu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), os, cpu);
    }

    @Override
    public String toString() {
        return EntityConstants.LAPTOP_CLASS_NAME
                + EntityConstants.TO_STRING_PREFIX
                + EntityConstants.PROP_ID + getId()
                + EntityConstants.PROP_NAME + getName() + EntityConstants.SINGLE_QUOTE
                + EntityConstants.PROP_PRICE + getPrice()
                + EntityConstants.PROP_OS + os + EntityConstants.SINGLE_QUOTE
                + EntityConstants.PROP_CPU + cpu + EntityConstants.SINGLE_QUOTE
                + EntityConstants.TO_STRING_SUFFIX;
    }
}

