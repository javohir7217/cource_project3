package com.appliance.store.controller;

import com.appliance.store.controller.model.Request;
import com.appliance.store.controller.model.Response;

public interface Controller {
    Response execute(Request request);
}

