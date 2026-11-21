package com.appliance.store.constant;

public final class RangeConstants {
    public static final int ZERO_COMPARISON = 0;
    public static final String PREFIX = "[";
    public static final String SUFFIX = "]";
    public static final String DELIMITER = "..";

    public static final String ERROR_NULL_BOUND = "Range bounds cannot be null";
    public static final String ERROR_LOW_GREATER_THAN_HIGH = "Range low bound cannot be greater than high bound";

    private RangeConstants() {
    }
}

