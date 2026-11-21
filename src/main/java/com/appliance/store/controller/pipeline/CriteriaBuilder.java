package com.appliance.store.controller.pipeline;

import com.appliance.store.constant.DelimiterConstants;
import com.appliance.store.constant.EntityConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.controller.model.EntityTarget;
import com.appliance.store.criteria.Range;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.entity.Laptop;
import com.appliance.store.entity.Oven;

import java.util.List;

public class CriteriaBuilder {

    public List<SearchCriteria<? extends BaseEntity>> build(EntityTarget target, List<String> paramTokens) {
        if (target == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.MISSING_ENTITY_TARGET);
        }

        if (target == EntityTarget.LAPTOPS) {
            SearchCriteria<Laptop> laptopCriteria = new SearchCriteria<>(Laptop.class);
            populateLaptopCriteria(laptopCriteria, paramTokens);
            return List.of(laptopCriteria);
        } else if (target == EntityTarget.OVENS) {
            SearchCriteria<Oven> ovenCriteria = new SearchCriteria<>(Oven.class);
            populateOvenCriteria(ovenCriteria, paramTokens);
            return List.of(ovenCriteria);
        } else {
            // Target is ALL: generate criteria for both Laptop and Oven with common fields
            SearchCriteria<Laptop> laptopCriteria = new SearchCriteria<>(Laptop.class);
            populateLaptopCriteria(laptopCriteria, paramTokens);

            SearchCriteria<Oven> ovenCriteria = new SearchCriteria<>(Oven.class);
            populateOvenCriteria(ovenCriteria, paramTokens);

            return List.of(laptopCriteria, ovenCriteria);
        }
    }

    private void populateLaptopCriteria(SearchCriteria<Laptop> criteria, List<String> paramTokens) {
        if (paramTokens == null) {
            return;
        }
        for (String param : paramTokens) {
            String[] kv = splitKeyValue(param);
            String key = kv[DelimiterConstants.ZERO_INDEX].toLowerCase();
            String value = kv[DelimiterConstants.ONE_INDEX];

            switch (key) {
                case EntityConstants.FIELD_ID -> {
                    Range<Long> idRange = parseLongRange(key, value);
                    criteria.addRangeFilter(key, BaseEntity::getId, idRange);
                }
                case EntityConstants.FIELD_PRICE -> {
                    Range<Long> priceRange = parseLongRange(key, value);
                    criteria.addRangeFilter(key, BaseEntity::getPrice, priceRange);
                }
                case EntityConstants.FIELD_NAME -> criteria.addCaseInsensitiveStringMatch(key, BaseEntity::getName, value);
                case EntityConstants.FIELD_OS -> criteria.addCaseInsensitiveStringMatch(key, Laptop::getOs, value);
                case EntityConstants.FIELD_CPU -> criteria.addCaseInsensitiveStringMatch(key, Laptop::getCpu, value);
                default -> throw new IllegalArgumentException(ErrorMessageConstants.INVALID_FIELD_PREFIX + key + ErrorMessageConstants.CLOSING_QUOTE);
            }
        }
    }

    private void populateOvenCriteria(SearchCriteria<Oven> criteria, List<String> paramTokens) {
        if (paramTokens == null) {
            return;
        }
        for (String param : paramTokens) {
            String[] kv = splitKeyValue(param);
            String key = kv[DelimiterConstants.ZERO_INDEX].toLowerCase();
            String value = kv[DelimiterConstants.ONE_INDEX];

            switch (key) {
                case EntityConstants.FIELD_ID -> {
                    Range<Long> idRange = parseLongRange(key, value);
                    criteria.addRangeFilter(key, BaseEntity::getId, idRange);
                }
                case EntityConstants.FIELD_PRICE -> {
                    Range<Long> priceRange = parseLongRange(key, value);
                    criteria.addRangeFilter(key, BaseEntity::getPrice, priceRange);
                }
                case EntityConstants.FIELD_NAME -> criteria.addCaseInsensitiveStringMatch(key, BaseEntity::getName, value);
                case EntityConstants.FIELD_POWER -> {
                    Range<Integer> powerRange = parseIntegerRange(key, value);
                    criteria.addRangeFilter(key, Oven::getPower, powerRange);
                }
                case EntityConstants.FIELD_CAPACITY -> {
                    Range<Integer> capacityRange = parseIntegerRange(key, value);
                    criteria.addRangeFilter(key, Oven::getCapacity, capacityRange);
                }
                default -> throw new IllegalArgumentException(ErrorMessageConstants.INVALID_FIELD_PREFIX + key + ErrorMessageConstants.CLOSING_QUOTE);
            }
        }
    }

    private String[] splitKeyValue(String param) {
        int eqIndex = param.indexOf(DelimiterConstants.KEY_VALUE_DELIMITER);
        if (eqIndex <= DelimiterConstants.ZERO_INDEX || eqIndex == param.length() - DelimiterConstants.ONE_OFFSET) {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_PARAMETER_FORMAT_PREFIX + param);
        }
        String key = param.substring(DelimiterConstants.ZERO_INDEX, eqIndex).trim();
        String value = param.substring(eqIndex + DelimiterConstants.ONE_OFFSET).trim();
        if (value.startsWith(DelimiterConstants.QUOTE) && value.endsWith(DelimiterConstants.QUOTE)
                && value.length() >= DelimiterConstants.MIN_QUOTED_LENGTH) {
            value = value.substring(DelimiterConstants.ONE_OFFSET, value.length() - DelimiterConstants.ONE_OFFSET).trim();
        }
        return new String[]{key, value};
    }

    private Range<Long> parseLongRange(String fieldName, String value) {
        try {
            if (value.contains(DelimiterConstants.RANGE_DELIMITER)) {
                String[] parts = value.split(DelimiterConstants.RANGE_DELIMITER, -1);
                if (parts.length != DelimiterConstants.EXPECTED_RANGE_PARTS) {
                    throw new IllegalArgumentException(ErrorMessageConstants.INVALID_PARAMETER_FORMAT_PREFIX + value);
                }
                long low = Long.parseLong(parts[DelimiterConstants.RANGE_LOW_INDEX].trim());
                long high = Long.parseLong(parts[DelimiterConstants.RANGE_HIGH_INDEX].trim());
                return new Range<>(low, high);
            } else {
                long exact = Long.parseLong(value.trim());
                return new Range<>(exact, exact);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_NUMBER_FORMAT_PREFIX + fieldName + ": " + value, e);
        }
    }

    private Range<Integer> parseIntegerRange(String fieldName, String value) {
        try {
            if (value.contains(DelimiterConstants.RANGE_DELIMITER)) {
                String[] parts = value.split(DelimiterConstants.RANGE_DELIMITER, -1);
                if (parts.length != DelimiterConstants.EXPECTED_RANGE_PARTS) {
                    throw new IllegalArgumentException(ErrorMessageConstants.INVALID_PARAMETER_FORMAT_PREFIX + value);
                }
                int low = Integer.parseInt(parts[DelimiterConstants.RANGE_LOW_INDEX].trim());
                int high = Integer.parseInt(parts[DelimiterConstants.RANGE_HIGH_INDEX].trim());
                return new Range<>(low, high);
            } else {
                int exact = Integer.parseInt(value.trim());
                return new Range<>(exact, exact);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_NUMBER_FORMAT_PREFIX + fieldName + ": " + value, e);
        }
    }
}
