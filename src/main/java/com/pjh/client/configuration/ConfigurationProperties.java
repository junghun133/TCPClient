package com.pjh.client.configuration;

import com.pjh.client.exception.ConfigurationFileLoadException;

import java.io.File;

public interface ConfigurationProperties {
    enum PropertyNames {
        application, profiles, active, configuration
    }

    enum ServicePropertyNames {
        servers("")
        ,server("")
        ,ipAddress("")
        ,port("")
        ,cpId("")
        ,cpPassword("")
        ,databaseConfig("database.yml")
        ,connectTimeout("5000")
        ,socketTimeout("5000")
        ,receiveBufferSize("10240")
        ,sendBufferSize("10240")
        ,linkSendIntervalMs("10000")
        ,entryType("Map")
        ,dataHandlerType("Mybatis");

        String defaultValue;

        ServicePropertyNames(String defaultValue){
            this.defaultValue = defaultValue;
        }
        public String getDefaultValue(){ return defaultValue; }
        public int getDefaulValueToInt(){ return Integer.parseInt(defaultValue); }
    }

    enum DatabasePropertyNames{
        jdbcUrl(null)
        ,databases(null)
        ,source(null)
        ,columns(null)
        ,dbmsType("")
        ,dbId(null)
        ,dbPassword(null)
        ,ipAddress("")
        ,port("")
        ,dbName("")
        ,messageTableName("TBL_MSG")
        ,logTableName("TBL_LOG");

        String defaultValue;
        DatabasePropertyNames(String defaultValue){
            this.defaultValue = defaultValue;
        }
        public String getDefaultValue(){ return defaultValue; }
        public int getDefaulValueToInt(){ return Integer.parseInt(defaultValue); }
    }

    enum ConfigurationFileName{
        SERVER(File.separator + "server"),
        DATABASE(File.separator + "database");

        String fileName;
        ConfigurationFileName(String fileName){
            this.fileName = fileName;
        }

        protected String getFileName(ConfigurationExtensionType configurationExtensionType) throws ConfigurationFileLoadException {
            switch (configurationExtensionType){
                case YML:
                    return fileName + ".yml";
                case JSON:
                    return fileName + ".json";
                case XML:
                    return fileName + ".xml";
            }
            throw new ConfigurationFileLoadException();
        }

        public String getConfigPath(){
            return fileName;
        }
    }
}
