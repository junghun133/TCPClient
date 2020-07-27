package com.pjh.client.configuration.transfer;

import com.pjh.client.data.Data;
import com.pjh.client.data.DatabaseConfigurationData;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.database.DataHandler;
import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.configuration.*;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransferDataFromYAML extends TransferData{
    public TransferDataFromYAML(Configuration.ServiceType serviceType){
        super(serviceType, ConfigurationExtensionType.YML);
    }

    @Override
    public Data getData() throws ConfigurationFileLoadException {
        try {
            List<Map<String, Object>> documents = (List<Map<String, Object>>) readConfigurationFiles();
            String activeKey = null;
            for(Map<String, Object> map : documents){
                if(map.containsKey(ConfigurationProperties.PropertyNames.application.name())) {
                    Map<String, Object> profiles = (Map<String, Object>) getConfigHeadProperty(map).get(ConfigurationProperties.PropertyNames.profiles.name());
                    activeKey = (String) profiles.get(ConfigurationProperties.PropertyNames.active.name());
                    if(activeKey == null || activeKey.isEmpty())
                        throw new Exception();
                    continue;
                }

                Map<String, Object> profiles = getConfigHeadProperty(map);
                if(activeKey.contentEquals((String) profiles.get(ConfigurationProperties.PropertyNames.profiles.name()))){
                    switch (serviceType){
                        case SVC:
                            List<Map<String, Object>> servers = (List<Map<String, Object>>) profiles.get(ConfigurationProperties.ServicePropertyNames.servers.name());
                            Map<String, Object> server = (Map<String, Object>) servers.get(0).get(ConfigurationProperties.ServicePropertyNames.server.name());
                            validProperty(serviceType, server);

                            return ServiceConfigurationData.builder()
                                    .ipAddress(getMapValueWithNullCheck(server, ConfigurationProperties.ServicePropertyNames.ipAddress))
                                    .port(getMapValueWithNullCheckForIntValue(server, ConfigurationProperties.ServicePropertyNames.port))
                                    .cpId(getMapValueWithNullCheck(server, ConfigurationProperties.ServicePropertyNames.cpId))
                                    .cpPassword(getMapValueWithNullCheck(server, ConfigurationProperties.ServicePropertyNames.cpPassword))
                                    .connectTimeout(getMapValueWithNullCheckForIntValue(server, ConfigurationProperties.ServicePropertyNames.connectTimeout))
                                    .socketTimeout(getMapValueWithNullCheckForIntValue(server, ConfigurationProperties.ServicePropertyNames.socketTimeout))
                                    .sendBufferSize(getMapValueWithNullCheckForIntValue(server, ConfigurationProperties.ServicePropertyNames.sendBufferSize))
                                    .receiveBufferSize(getMapValueWithNullCheckForIntValue(server, ConfigurationProperties.ServicePropertyNames.receiveBufferSize))
                                    .linkSendIntervalMs(getMapValueWithNullCheckForIntValue(server, ConfigurationProperties.ServicePropertyNames.linkSendIntervalMs))

                                    .messageEntryType(MessageEntry.EntryType.valueOf(getMapValueWithNullCheck(server, ConfigurationProperties.ServicePropertyNames.entryType)))
                                    .dataHandlerType(DataHandler.DataHandlerType.valueOf(getMapValueWithNullCheck(server, ConfigurationProperties.ServicePropertyNames.dataHandlerType)))
                                    .build();
                        case DB:
                            List<Map<String, Object>> databases = (List<Map<String, Object>>) profiles.get(ConfigurationProperties.DatabasePropertyNames.databases.name());

                            Map<String, Object> source = (Map<String, Object>) databases.get(0).get(ConfigurationProperties.DatabasePropertyNames.source.name());
                            Map<String, Object> columns = (Map<String, Object>) databases.get(1).get(ConfigurationProperties.DatabasePropertyNames.columns.name());
                            DatabaseConfigurationData databaseConfigurationData = DatabaseConfigurationData.builder()
                                    .jdbcUrl(getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.jdbcUrl))
                                    .ipAddress(getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.ipAddress))
                                    .port(getMapValueWithNullCheckForIntValue(source, ConfigurationProperties.DatabasePropertyNames.port))
                                    .dbmsType(DBMSType.valueOf(getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.dbmsType).toUpperCase()))
                                    .dbName(getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.dbName))
                                    .messageTableName((getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.messageTableName)))
                                    .dbId((getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.dbId)))
                                    .dbPassword((getMapValueWithNullCheck(source, ConfigurationProperties.DatabasePropertyNames.dbPassword)))
                                    .build();

                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.messageKey, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.messageKey));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.status, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.status));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.result, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.result));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.cause, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.cause));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.phone, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.phone));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.callback, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.callback));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.filePath, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.filePath));
                            databaseConfigurationData.addColumn(ColumnNameMapper.Columns.requestDate, getMapValueWithNullCheck(columns, ColumnNameMapper.Columns.requestDate));

                            validProperty(serviceType, source);
                            return databaseConfigurationData;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConfigurationFileLoadException();
        }
        return null;
    }

    @Override
    public List<?> readConfigurationFiles() throws ConfigurationFileLoadException, IOException {
        List<Map<String, Object>> documents = new ArrayList<>();
        Yaml yaml = new Yaml();

        Iterable<Object> yamls = yaml.loadAll(FileUtils.readFileToString(readConfigFile(serviceType, configurationExtensionType)));
        if(yamls == null)
            throw new ConfigurationFileLoadException();

        for(Object o : yamls){
            documents.add((Map<String, Object>) o);
        }
        return documents;
    }

    private Map<String, Object> getConfigHeadProperty(Map<String, Object> map){
        Map<String, Object> rootMap = null;
        if(map.containsKey(ConfigurationProperties.PropertyNames.application.name()))
            rootMap = (Map<String, Object>) map.get(ConfigurationProperties.PropertyNames.application.name());
        else if(map.containsKey(ConfigurationProperties.PropertyNames.configuration.name()))
            rootMap = (Map<String, Object>) map.get(ConfigurationProperties.PropertyNames.configuration.name());

        return rootMap;
    }

    private void validProperty(Configuration.ServiceType serviceType, Map<String, Object> map) throws Exception {
        boolean propertyValid = false;
        switch (serviceType){
            case SVC:
                for (String value : map.keySet()) {
                    for (ConfigurationProperties.ServicePropertyNames s : ConfigurationProperties.ServicePropertyNames.values()) {
                        if (value.contentEquals(s.name())) {
                            propertyValid = true;
                            break;
                        }
                    }
                    if(!propertyValid)
                        throw new Exception();
                    propertyValid = false;
                } break;

            case DB:
                for (String value : map.keySet()) {
                    for (ConfigurationProperties.DatabasePropertyNames d : ConfigurationProperties.DatabasePropertyNames.values()) {
                        if (value.contentEquals(d.name())) {
                            propertyValid = true;
                            break;
                        }
                    }
                    if(!propertyValid)
                        throw new Exception();
                    propertyValid = false;
                } break;

        }
    }
}