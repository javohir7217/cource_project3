package com.appliance.store.controller.model;

public record ResponseImpl(String responseString, boolean isOk, boolean isOut) implements Response {
}

