package com.pjh.client.configuration.transfer;

import com.pjh.client.configuration.Configuration;
import com.pjh.client.configuration.ConfigurationExtensionType;
import com.pjh.client.data.Data;
import com.pjh.client.exception.ConfigurationFileLoadException;

import java.io.IOException;
import java.util.List;

public class TransferDataFromJson extends TransferData{
    public TransferDataFromJson(Configuration.ServiceType serviceType){
        super(serviceType, ConfigurationExtensionType.JSON);
    }

    @Override
    public Data getData() throws ConfigurationFileLoadException {
        return null;
    }

    @Override
    public List<?> readConfigurationFiles() throws IOException {
        return null;
    }
}
