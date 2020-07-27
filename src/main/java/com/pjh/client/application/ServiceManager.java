package com.pjh.client.application;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.database.DataHandler;
import com.pjh.client.exception.InvalidConfigurationException;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.thread.ThreadManager;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
    private final Connection connectionManager;
    private final ThreadManager threadManager;
    private Map<String, ServiceConfiguration> serviceConfigurations = new HashMap<>();

    public ServiceManager(Connection connectionManager, ThreadManager threadManager) {
        this.connectionManager = connectionManager;
        this.threadManager = threadManager;
    }

    public AppTestResult test() throws InvalidConfigurationException {
        if (serviceConfigurations.isEmpty())
            throw new InvalidConfigurationException();

        AppTestResult appTestResult = new AppTestResult();
        appTestResult.setResult(AppTestResult.ResultCode.Success);
        for(Map.Entry<String, ServiceConfiguration> entry: serviceConfigurations.entrySet()) {
            String serviceName = entry.getKey();
            ServiceConfiguration sc = entry.getValue();
            if (!connectionManager.connect()) {
                appTestResult.setResult(AppTestResult.ResultCode.ConnectFail);
                appTestResult.setCauseServiceName(serviceName);
                break;
            }

            if (!connectionManager.bind(null)) {
                appTestResult.setResult(AppTestResult.ResultCode.BindFail);
                appTestResult.setCauseServiceName(serviceName);
                break;
            }

//            DataHandler dataHandler = threadManager.getDataHandler(sc);
//            if(dataHandler == null || !dataHandler.connectTest()) {
//                appTestResult.setResult(AppTestResult.ResultCode.DBConnectFail);
//                appTestResult.setCauseServiceName(serviceName);
//                break;
//            }

            connectionManager.disconnect();
        }

        return appTestResult;
    }

    public void addService(ServiceConfiguration serviceConfiguration) {
        serviceConfigurations.put(serviceConfiguration.getServiceName(), serviceConfiguration);
    }

    public int getServiceCount() {
        return serviceConfigurations.size();
    }

    public void deleteService(ServiceConfiguration serviceConfiguration) {
        if (serviceConfiguration == null)
            return;
        serviceConfigurations.remove(serviceConfiguration.getServiceName());
    }

    public void startServices() throws InvalidConfigurationException {
        for (Map.Entry<String, ServiceConfiguration> entry : serviceConfigurations.entrySet()) {
            ServiceConfiguration sc = entry.getValue();
            if (!connectionManager.connect())
                continue;
            threadManager.startThread(connectionManager, sc);
        }
    }

    public ThreadManager.ServiceStatus checkService(ServiceConfiguration serviceConfiguration) {
        return threadManager.check(serviceConfiguration);
    }

    public MessageEntry getMessageEntry(ServiceConfiguration serviceConfiguration) {
        return threadManager.getMessageEntry(serviceConfiguration);
    }

    public void stopAllService() {
        for (Map.Entry<String, ServiceConfiguration> entry : serviceConfigurations.entrySet()) {
            ServiceConfiguration sc = entry.getValue();
            threadManager.stopThread(sc);
            MessageEntry messageEntry = threadManager.getMessageEntry(sc);
            messageEntry.backup();
            DataHandler dataHandler = threadManager.getDataHandler(sc);
            dataHandler.cleanup();
            connectionManager.disconnect();

        }
    }
}
