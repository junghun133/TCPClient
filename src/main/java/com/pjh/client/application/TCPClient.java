package com.pjh.client.application;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.connection.DummyConnection;
import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.exception.InvalidConfigurationException;
import com.pjh.client.exception.NotSupportedPlatformException;
import com.pjh.client.exception.NotSupportedServiceTypeException;
import com.pjh.client.thread.ThreadManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TCPClient {
    public static void main(String[] args) {
        ServiceConfiguration config = new ServiceConfiguration("messageClient");
        try {
            config.setConfiguration();
        } catch (IOException | NotSupportedPlatformException | NotSupportedServiceTypeException | ConfigurationFileLoadException e) {
            log.info("Configuration load error : {}" + e.toString());
            return;
        }

        log.info("|||||||||||CONFIGURATION||||||||||");
        log.info(config.toString());
        log.info("||||||||||||||||||||||||||||||||||");
        Connection c = new DummyConnection();
        c.init(config);
        ThreadManager tm = new ThreadManager();
        ServiceManager sm = new ServiceManager(c, tm);
        sm.addService(config);

        try {
            AppTestResult result = sm.test();
            if (result.getResult() != AppTestResult.ResultCode.Success) {
                log.info("Application init fail");
                log.info("Service name : {}", result.getCauseServiceName());
                log.info("Fail reason : {}", result.getResult().name());
            } else {
                log.info("Application init test success");
            }
        } catch (InvalidConfigurationException e) {
            log.error("Configuration Error!!");
            return;
        }
        try {
            sm.startServices();
        } catch (InvalidConfigurationException e) {
            log.error("Invalid config error!!");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(sm)));
    }
}
