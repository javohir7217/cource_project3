package com.appliance.store.controller.pipeline;

import com.appliance.store.constant.DelimiterConstants;
import com.appliance.store.constant.EntityConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.controller.model.EntityTarget;

import java.util.List;
import java.util.Set;

public class CriteriaValidator {
    private static final Set<String> COMMON_FIELDS = Set.of(
            EntityConstants.FIELD_ID,
            EntityConstants.FIELD_NAME,
            EntityConstants.FIELD_PRICE
    );

    private static final Set<String> LAPTOP_FIELDS = Set.of(
            EntityConstants.FIELD_ID,
            EntityConstants.FIELD_NAME,
            EntityConstants.FIELD_PRICE,
            EntityConstants.FIELD_OS,
            EntityConstants.FIELD_CPU
    );

    private static final Set<String> OVEN_FIELDS = Set.of(
            EntityConstants.FIELD_ID,
            EntityConstants.FIELD_NAME,
            EntityConstants.FIELD_PRICE,
            EntityConstants.FIELD_POWER,
            EntityConstants.FIELD_CAPACITY
    );

    public void validate(EntityTarget target, List<String> paramTokens) {
        if (target == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.MISSING_ENTITY_TARGET);
        }
        if (paramTokens == null || paramTokens.isEmpty()) {
            return;
        }

        for (String param : paramTokens) {
            int eqIndex = param.indexOf(DelimiterConstants.KEY_VALUE_DELIMITER);
            if (eqIndex <= DelimiterConstants.ZERO_INDEX || eqIndex == param.length() - DelimiterConstants.ONE_OFFSET) {
                throw new IllegalArgumentException(ErrorMessageConstants.INVALID_PARAMETER_FORMAT_PREFIX + param);
            }
            String rawKey = param.substring(DelimiterConstants.ZERO_INDEX, eqIndex).trim();
            String normalizedKey = rawKey.toLowerCase();

            if (target == EntityTarget.ALL) {
                if (!COMMON_FIELDS.contains(normalizedKey)) {
                    throw new IllegalArgumentException(ErrorMessageConstants.INVALID_FIELD_PREFIX + rawKey
                            + ErrorMessageConstants.FOR_TARGET_PREFIX + target.getTargetName()
                            + ErrorMessageConstants.CLOSING_QUOTE);
                }
            } else if (target == EntityTarget.LAPTOPS) {
                if (!LAPTOP_FIELDS.contains(normalizedKey)) {
                    throw new IllegalArgumentException(ErrorMessageConstants.INVALID_FIELD_PREFIX + rawKey
                            + ErrorMessageConstants.FOR_ENTITY_PREFIX + target.getTargetName()
                            + ErrorMessageConstants.CLOSING_QUOTE);
                }
            } else if (target == EntityTarget.OVENS) {
                if (!OVEN_FIELDS.contains(normalizedKey)) {
                    throw new IllegalArgumentException(ErrorMessageConstants.INVALID_FIELD_PREFIX + rawKey
                            + ErrorMessageConstants.FOR_ENTITY_PREFIX + target.getTargetName()
                            + ErrorMessageConstants.CLOSING_QUOTE);
                }
            }
        }
    }
}
