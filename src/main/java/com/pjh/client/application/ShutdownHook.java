package com.pjh.client.application;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShutdownHook implements Runnable {
    private final ServiceManager serviceManager;

    public ShutdownHook(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Override
    public void run() {
        log.info("Shutdown hook started!");
        serviceManager.stopAllService();
        log.info("Shutdown hook end!");
    }
}
