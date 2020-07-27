package com.pjh.client.database;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.data.DatabaseConfigurationData;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.database.mybatis.MybatisDBHandler;

public class DataHandlerFactory {
    public static DataHandler getDataHandler(ServiceConfiguration serviceConfiguration) {
        switch (((ServiceConfigurationData) serviceConfiguration.getServiceData()).getDataHandlerType()) {
            case Dummy:
                return new DummyDataHandler();
            case Mybatis:
                return new MybatisDBHandler(serviceConfiguration.getServiceName(), (DatabaseConfigurationData) serviceConfiguration.getDatabaseData());
        }
        return null;
    }

}
