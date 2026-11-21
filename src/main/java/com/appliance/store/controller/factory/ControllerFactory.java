package com.appliance.store.controller.factory;

import com.appliance.store.controller.Controller;
import com.appliance.store.controller.ControllerImpl;
import com.appliance.store.service.factory.ServiceFactory;
import com.appliance.store.service.util.SingletonHolder;

public final class ControllerFactory {
    private static final SingletonHolder<Controller> HOLDER =
            new SingletonHolder<>(() -> new ControllerImpl(ServiceFactory.getInstance()));

    private ControllerFactory() {
    }

    public static Controller getInstance() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.reset();
    }
}

