package com.appliance.store.controller.command;

import com.appliance.store.constant.ResponseConstants;
import com.appliance.store.controller.model.Response;
import com.appliance.store.controller.model.ResponseImpl;
import com.appliance.store.service.ApplianceService;
import com.appliance.store.service.exception.ServiceException;

public class CostCommand implements Command {
    private final ApplianceService service;

    public CostCommand(ApplianceService service) {
        this.service = service;
    }

    @Override
    public Response execute(CommandContext context) throws ServiceException {
        long totalCost;
        if (context.isAll()) {
            totalCost = service.calculateCost(context.getCriteriaList());
        } else {
            totalCost = service.calculateCost(context.getSingleCriteria());
        }
        return new ResponseImpl(String.valueOf(totalCost), ResponseConstants.IS_OK_TRUE, ResponseConstants.IS_OUT_FALSE);
    }
}

