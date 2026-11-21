package com.appliance.store.controller.command;

import com.appliance.store.constant.CommandConstants;
import com.appliance.store.constant.ResponseConstants;
import com.appliance.store.controller.model.Response;
import com.appliance.store.controller.model.ResponseImpl;

public class ExitCommand implements Command {
    @Override
    public Response execute(CommandContext context) {
        return new ResponseImpl(CommandConstants.EXIT_RESPONSE_MESSAGE, ResponseConstants.IS_OK_TRUE, ResponseConstants.IS_OUT_TRUE);
    }
}

