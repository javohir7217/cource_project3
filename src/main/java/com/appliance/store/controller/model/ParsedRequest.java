package com.appliance.store.controller.model;

import java.util.List;

public record ParsedRequest(String commandName, String targetName, List<String> paramTokens) {
}

