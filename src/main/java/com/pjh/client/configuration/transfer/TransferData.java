package com.pjh.client.configuration.transfer;

import com.pjh.client.data.Data;
import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.configuration.*;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public abstract class TransferData {
    public Configuration.ServiceType serviceType;
    public ConfigurationExtensionType configurationExtensionType;

    public File readConfigFile(Configuration.ServiceType serviceType, ConfigurationExtensionType configurationExtensionType) throws ConfigurationFileLoadException, IOException {
        ConfigurationFileReader configurationFileReader = new ConfigurationFileReader();
        return configurationFileReader.getFile(serviceType, configurationExtensionType);
    }

    public String getMapValueWithNullCheck(Map target, ConfigurationProperties.ServicePropertyNames servicePropertyNames) {
       if(!target.containsKey(servicePropertyNames.name()))
           return servicePropertyNames.getDefaultValue();

        if(target.get(servicePropertyNames.name()) != null) {
            return target.get(servicePropertyNames.name()).toString();
        }
        return servicePropertyNames.getDefaultValue();
    }

    public int getMapValueWithNullCheckForIntValue(Map target, ConfigurationProperties.ServicePropertyNames servicePropertyNames) {
        if (!target.containsKey(servicePropertyNames.name()))
            return servicePropertyNames.getDefaulValueToInt();

        int value = (int) target.get(servicePropertyNames.name());
        if (value != 0) {
            return value;
        }
        return servicePropertyNames.getDefaulValueToInt();
    }

    public String getMapValueWithNullCheck(Map target, ConfigurationProperties.DatabasePropertyNames databasePropertyNames ) {
        if(!target.containsKey(databasePropertyNames.name()))
            return databasePropertyNames.getDefaultValue();

        if(target.get(databasePropertyNames.name()) != null) {
            return target.get(databasePropertyNames.name()).toString();
        }
        return databasePropertyNames.getDefaultValue();
    }

    public int getMapValueWithNullCheckForIntValue(Map target, ConfigurationProperties.DatabasePropertyNames databasePropertyNames) {
        int value = (int) target.get(databasePropertyNames.name());
        if(value != 0) {
            return value;
        }
        return databasePropertyNames.getDefaulValueToInt();
    }

    public String getMapValueWithNullCheck(Map target, ColumnNameMapper.Columns columns) {
        if(target.get(columns.name()) != null) {
            return target.get(columns.name()).toString();
        }
        return "";
    }

    public abstract Data getData() throws ConfigurationFileLoadException;
    public abstract List<?> readConfigurationFiles() throws ConfigurationFileLoadException, IOException;
}
