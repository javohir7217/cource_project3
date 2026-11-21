package com.appliance.store.controller.pipeline;

import com.appliance.store.controller.command.CommandContext;
import com.appliance.store.controller.model.EntityTarget;
import com.appliance.store.controller.model.ParsedRequest;
import com.appliance.store.criteria.SearchCriteria;
import com.appliance.store.entity.BaseEntity;

import java.util.List;

public class ValidationPipeline {
    private final CriteriaValidator validator;
    private final CriteriaBuilder builder;

    public ValidationPipeline(CriteriaValidator validator, CriteriaBuilder builder) {
        this.validator = validator;
        this.builder = builder;
    }

    public CommandContext buildContext(ParsedRequest parsedRequest) {
        EntityTarget target = EntityTarget.fromTargetName(parsedRequest.targetName());
        validator.validate(target, parsedRequest.paramTokens());
        List<SearchCriteria<? extends BaseEntity>> criteriaList = builder.build(target, parsedRequest.paramTokens());
        return new CommandContext(target, criteriaList);
    }
}

