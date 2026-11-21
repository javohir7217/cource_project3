package com.appliance.store.controller.command;

import com.appliance.store.constant.CommandConstants;
import com.appliance.store.service.ApplianceService;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private final Map<String, Command> commands = new HashMap<>();
    private final Command wrongCommand = new WrongCommand();

    public CommandProvider(ApplianceService service) {
        commands.put(CommandConstants.COMMAND_FIND, new FindCommand(service));
        commands.put(CommandConstants.COMMAND_COST, new CostCommand(service));
        commands.put(CommandConstants.COMMAND_EXIT, new ExitCommand());
    }

    public Command getCommand(String commandName) {
        if (commandName == null) {
            return wrongCommand;
        }
        Command command = commands.get(commandName.toLowerCase().trim());
        return command != null ? command : wrongCommand;
    }
}

