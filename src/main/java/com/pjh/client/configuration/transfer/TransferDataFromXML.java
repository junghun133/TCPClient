package com.pjh.client.configuration.transfer;

import com.pjh.client.configuration.Configuration;
import com.pjh.client.configuration.ConfigurationExtensionType;
import com.pjh.client.data.Data;
import com.pjh.client.exception.ConfigurationFileLoadException;

import java.util.List;

public class TransferDataFromXML extends TransferData{
    public TransferDataFromXML(Configuration.ServiceType serviceType){
        super(serviceType, ConfigurationExtensionType.XML);
    }

    @Override
    public Data getData() throws ConfigurationFileLoadException {
        return null;
    }

    @Override
    public List<?> readConfigurationFiles() {
        return null;
    }
}
