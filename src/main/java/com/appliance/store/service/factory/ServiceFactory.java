package com.appliance.store.service.factory;

import com.appliance.store.service.ApplianceService;
import com.appliance.store.service.ApplianceServiceImpl;
import com.appliance.store.service.util.SingletonHolder;

public final class ServiceFactory {
    private static final SingletonHolder<ApplianceService> HOLDER =
            new SingletonHolder<>(ApplianceServiceImpl::new);

    private ServiceFactory() {
    }

    public static ApplianceService getInstance() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.reset();
    }
}

