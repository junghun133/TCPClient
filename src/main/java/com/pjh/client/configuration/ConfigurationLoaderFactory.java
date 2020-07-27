package com.pjh.client.configuration;

import com.pjh.client.configuration.transfer.TransferDataFromJson;
import com.pjh.client.configuration.transfer.TransferDataFromXML;
import com.pjh.client.configuration.transfer.TransferDataFromYAML;
import com.pjh.client.data.Data;
import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.exception.NotSupportedPlatformException;
import com.pjh.client.exception.NotSupportedServiceTypeException;

public class ConfigurationLoaderFactory {

    public Data getConfig(Configuration.ServiceType serviceType, ConfigurationExtensionType configurationExtensionType) throws NotSupportedPlatformException, NotSupportedServiceTypeException, ConfigurationFileLoadException {
        if(serviceType == null)
            throw new NotSupportedServiceTypeException();
        if(configurationExtensionType == null)
            throw new NotSupportedPlatformException();

        switch (serviceType){
            case DB:
            case SVC:
                break;

            case ETC:
            default:
                throw new NotSupportedServiceTypeException();
        }

        switch (configurationExtensionType){
            case YML:
                    //yaml pars
                return new TransferDataFromYAML(serviceType).getData();
            case XML:
                //xml pars
                return new TransferDataFromXML(serviceType).getData();
            case JSON:
                //json pars
                return new TransferDataFromJson(serviceType).getData();

            case ETC:
            default:
                throw new NotSupportedPlatformException();
        }
    }
}
