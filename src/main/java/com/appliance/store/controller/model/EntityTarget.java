package com.appliance.store.controller.model;

import com.appliance.store.constant.CommandConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.entity.Laptop;
import com.appliance.store.entity.Oven;

public enum EntityTarget {
    LAPTOPS(CommandConstants.TARGET_LAPTOPS, Laptop.class),
    OVENS(CommandConstants.TARGET_OVENS, Oven.class),
    ALL(CommandConstants.TARGET_ALL, BaseEntity.class);

    private final String targetName;
    private final Class<? extends BaseEntity> entityClass;

    EntityTarget(String targetName, Class<? extends BaseEntity> entityClass) {
        this.targetName = targetName;
        this.entityClass = entityClass;
    }

    public String getTargetName() {
        return targetName;
    }

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }

    public static EntityTarget fromTargetName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ErrorMessageConstants.MISSING_ENTITY_TARGET);
        }
        for (EntityTarget target : values()) {
            if (target.targetName.equalsIgnoreCase(name.trim())) {
                return target;
            }
        }
        throw new IllegalArgumentException(ErrorMessageConstants.UNKNOWN_ENTITY_TARGET_PREFIX + name);
    }
}

