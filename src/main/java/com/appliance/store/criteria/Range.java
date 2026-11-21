package com.appliance.store.criteria;

import com.appliance.store.constant.RangeConstants;

import java.util.Objects;

public final class Range<T extends Comparable<T>> {
    private final T low;
    private final T high;

    public Range(T low, T high) {
        if (low == null || high == null) {
            throw new IllegalArgumentException(RangeConstants.ERROR_NULL_BOUND);
        }
        if (low.compareTo(high) > RangeConstants.ZERO_COMPARISON) {
            throw new IllegalArgumentException(RangeConstants.ERROR_LOW_GREATER_THAN_HIGH);
        }
        this.low = low;
        this.high = high;
    }

    public T getLow() {
        return low;
    }

    public T getHigh() {
        return high;
    }

    public boolean contains(T value) {
        if (value == null) {
            return false;
        }
        return value.compareTo(low) >= RangeConstants.ZERO_COMPARISON
                && value.compareTo(high) <= RangeConstants.ZERO_COMPARISON;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Range<?> range)) {
            return false;
        }
        return Objects.equals(low, range.low) && Objects.equals(high, range.high);
    }

    @Override
    public int hashCode() {
        return Objects.hash(low, high);
    }

    @Override
    public String toString() {
        return RangeConstants.PREFIX + low + RangeConstants.DELIMITER + high + RangeConstants.SUFFIX;
    }
}

