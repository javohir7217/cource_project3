package com.appliance.store.controller.command;

import com.appliance.store.constant.DelimiterConstants;
import com.appliance.store.controller.model.EntityTarget;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.entity.BaseEntity;

import java.util.Collections;
import java.util.List;

public class CommandContext {
    private final EntityTarget target;
    private final List<SearchCriteria<? extends BaseEntity>> criteriaList;

    public CommandContext(EntityTarget target, List<SearchCriteria<? extends BaseEntity>> criteriaList) {
        this.target = target;
        this.criteriaList = criteriaList != null ? Collections.unmodifiableList(criteriaList) : Collections.emptyList();
    }

    public static CommandContext empty() {
        return new CommandContext(null, Collections.emptyList());
    }

    public EntityTarget getTarget() {
        return target;
    }

    public List<SearchCriteria<? extends BaseEntity>> getCriteriaList() {
        return criteriaList;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> SearchCriteria<T> getSingleCriteria() {
        if (criteriaList.isEmpty()) {
            return null;
        }
        return (SearchCriteria<T>) criteriaList.get(DelimiterConstants.ZERO_INDEX);
    }

    public boolean isAll() {
        return target == EntityTarget.ALL;
    }
}

