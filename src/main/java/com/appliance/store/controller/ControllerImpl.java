package com.appliance.store.controller;

import com.appliance.store.constant.CommandConstants;
import com.appliance.store.constant.ResponseConstants;
import com.appliance.store.controller.command.Command;
import com.appliance.store.controller.command.CommandContext;
import com.appliance.store.controller.command.CommandProvider;
import com.appliance.store.controller.command.ExitCommand;
import com.appliance.store.controller.command.WrongCommand;
import com.appliance.store.controller.model.ParsedRequest;
import com.appliance.store.controller.model.Request;
import com.appliance.store.controller.model.Response;
import com.appliance.store.controller.model.ResponseImpl;
import com.appliance.store.controller.pipeline.CriteriaBuilder;
import com.appliance.store.controller.pipeline.CriteriaValidator;
import com.appliance.store.controller.pipeline.RequestParser;
import com.appliance.store.controller.pipeline.ValidationPipeline;
import com.appliance.store.service.ApplianceService;
import com.appliance.store.service.exception.ServiceException;

public class ControllerImpl implements Controller {
    private final CommandProvider commandProvider;
    private final RequestParser requestParser;
    private final ValidationPipeline validationPipeline;

    public ControllerImpl(ApplianceService service) {
        this.commandProvider = new CommandProvider(service);
        this.requestParser = new RequestParser();
        this.validationPipeline = new ValidationPipeline(new CriteriaValidator(), new CriteriaBuilder());
    }

    @Override
    public Response execute(Request request) {
        if (request == null || request.requestString() == null || request.requestString().isBlank()) {
            return new ResponseImpl(
                    CommandConstants.WRONG_COMMAND_RESPONSE_MESSAGE,
                    ResponseConstants.IS_OK_FALSE,
                    ResponseConstants.IS_OUT_FALSE
            );
        }

        try {
            ParsedRequest parsedRequest = requestParser.parse(request.requestString());
            Command command = commandProvider.getCommand(parsedRequest.commandName());

            if (command instanceof WrongCommand || command instanceof ExitCommand) {
                return command.execute(CommandContext.empty());
            }

            CommandContext context = validationPipeline.buildContext(parsedRequest);
            return command.execute(context);
        } catch (IllegalArgumentException e) {
            return new ResponseImpl(e.getMessage(), ResponseConstants.IS_OK_FALSE, ResponseConstants.IS_OUT_FALSE);
        } catch (ServiceException e) {
            return new ResponseImpl(e.getMessage(), ResponseConstants.IS_OK_FALSE, ResponseConstants.IS_OUT_TRUE);
        }
    }
}
