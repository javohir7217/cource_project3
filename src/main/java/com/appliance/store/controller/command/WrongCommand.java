package com.appliance.store.controller.command;

import com.appliance.store.constant.CommandConstants;
import com.appliance.store.constant.ResponseConstants;
import com.appliance.store.controller.model.Response;
import com.appliance.store.controller.model.ResponseImpl;

public class WrongCommand implements Command {
    @Override
    public Response execute(CommandContext context) {
        return new ResponseImpl(CommandConstants.WRONG_COMMAND_RESPONSE_MESSAGE, ResponseConstants.IS_OK_FALSE, ResponseConstants.IS_OUT_FALSE);
    }
}

