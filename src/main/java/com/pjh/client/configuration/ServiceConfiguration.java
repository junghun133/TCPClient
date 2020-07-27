package com.pjh.client.configuration;

import com.pjh.client.data.Data;
import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.exception.NotSupportedPlatformException;
import com.pjh.client.exception.NotSupportedServiceTypeException;
import lombok.ToString;

import java.io.IOException;

@lombok.Data
@ToString
public class ServiceConfiguration implements Configuration {
    public static String testName;
    private String serviceName;
    private Data serviceData;
    private Data databaseData;
    //private MessageEntry.EntryType messageEntryType = MessageEntry.EntryType.Map;
    //private DataHandler.DataHandlerType dataHandlerType = DataHandler.DataHandlerType.Mybatis;
    ConfigurationLoaderFactory configurationLoaderFactory;

    public ServiceConfiguration(String serviceName) {
        this.serviceName = serviceName;
        configurationLoaderFactory = new ConfigurationLoaderFactory();
    }

    public boolean setConfiguration() throws IOException, NotSupportedPlatformException, NotSupportedServiceTypeException, ConfigurationFileLoadException {
        serviceData = configurationLoaderFactory.getConfig(ServiceType.SVC, ConfigurationFileReader.findConfigExtension());
        databaseData = configurationLoaderFactory.getConfig(ServiceType.DB, ConfigurationFileReader.findConfigExtension());

        return serviceData != null && databaseData != null;
    }
}