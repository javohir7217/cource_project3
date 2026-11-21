package com.appliance.store.controller.command;

import com.appliance.store.controller.model.Response;
import com.appliance.store.service.exception.ServiceException;

public interface Command {
    Response execute(CommandContext context) throws ServiceException;
}

