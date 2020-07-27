package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.database.DataHandler;
import com.pjh.client.database.DataHandlerFactory;
import com.pjh.client.exception.InvalidConfigurationException;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.message.MessageEntryFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadManager {
    private Map<String, ServiceData> serviceDatas = new HashMap<>();

    public ThreadManager() {
    }

    public boolean startThread(Connection connectionManager, ServiceConfiguration serviceConfiguration) throws InvalidConfigurationException {
        if (serviceConfiguration == null || serviceConfiguration.getServiceName() == null || serviceConfiguration.getServiceName().isEmpty() || connectionManager == null)
            throw new InvalidConfigurationException();

        if (!connectionManager.connect())
            return false;

        if (!connectionManager.bind(null))
            return false;

        if (!serviceDatas.containsKey(serviceConfiguration.getServiceName()))
            serviceDatas.put(
                    serviceConfiguration.getServiceName(),
                    new ServiceData(Executors.newScheduledThreadPool(4),
                            MessageEntryFactory.getMessageEntry(((ServiceConfigurationData) serviceConfiguration.getServiceData()).getMessageEntryType()),
                            DataHandlerFactory.getDataHandler(serviceConfiguration)));

        ServiceData serviceData = serviceDatas.get(serviceConfiguration.getServiceName());
        MessageEntry entry = serviceData.getMessageEntry();
        DataHandler dataHandler = serviceData.getDataHandler();
        ScheduledExecutorService executorService = serviceData.getScheduledExecutorService();
        executorService.scheduleWithFixedDelay(new MessageSender(serviceConfiguration, entry, connectionManager), 0, 200, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(new MessageReceiver(serviceConfiguration, entry, connectionManager), 0, 200, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(new MessageLoader(serviceConfiguration, entry, dataHandler), 0, 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(new MessageSaver(serviceConfiguration, entry, dataHandler), 0, 1000, TimeUnit.MILLISECONDS);

        return true;
    }

    public ServiceStatus check(ServiceConfiguration serviceConfiguration) {
        if (!serviceDatas.containsKey(serviceConfiguration.getServiceName()))
            return ServiceStatus.Exit;
        ScheduledExecutorService s = serviceDatas.get(serviceConfiguration.getServiceName()).getScheduledExecutorService();
        return (s.isShutdown() && s.isTerminated()) ? ServiceStatus.Exit : ServiceStatus.Running;
    }

    public MessageEntry getMessageEntry(ServiceConfiguration serviceConfiguration) {
        if (!serviceDatas.containsKey(serviceConfiguration.getServiceName()))
            return null;

        return serviceDatas.get(serviceConfiguration.getServiceName()).getMessageEntry();
    }

    public void stopThread(ServiceConfiguration serviceConfiguration) {
        if (!serviceDatas.containsKey(serviceConfiguration.getServiceName()))
            return;

        ServiceData sd = serviceDatas.get(serviceConfiguration.getServiceName());
        ExecutorService es = sd.getScheduledExecutorService();
        es.shutdown();
        try {
            es.awaitTermination(30000, TimeUnit.MILLISECONDS);
            log.info("All threads are stop");
        } catch (InterruptedException e) {
            log.error("Stop service interrupted!");
        }
    }

    public DataHandler getDataHandler(ServiceConfiguration serviceConfiguration) {
        if (!serviceDatas.containsKey(serviceConfiguration.getServiceName()))
            return null;

        return serviceDatas.get(serviceConfiguration.getServiceName()).getDataHandler();
    }

    public enum ServiceStatus {Exit, Running}
}
