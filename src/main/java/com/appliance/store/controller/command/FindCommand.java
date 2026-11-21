package com.appliance.store.controller.command;

import com.appliance.store.constant.ResponseConstants;
import com.appliance.store.controller.model.Response;
import com.appliance.store.controller.model.ResponseImpl;
import com.appliance.store.entity.BaseEntity;
import com.appliance.store.service.ApplianceService;
import com.appliance.store.service.exception.ServiceException;

import java.util.List;

public class FindCommand implements Command {
    private final ApplianceService service;

    public FindCommand(ApplianceService service) {
        this.service = service;
    }

    @Override
    public Response execute(CommandContext context) throws ServiceException {
        if (context.isAll()) {
            List<BaseEntity> result = service.findAll(context.getCriteriaList());
            return new ResponseImpl(result.toString(), ResponseConstants.IS_OK_TRUE, ResponseConstants.IS_OUT_FALSE);
        } else {
            List<? extends BaseEntity> result = service.find(context.getSingleCriteria());
            return new ResponseImpl(result.toString(), ResponseConstants.IS_OK_TRUE, ResponseConstants.IS_OUT_FALSE);
        }
    }
}

