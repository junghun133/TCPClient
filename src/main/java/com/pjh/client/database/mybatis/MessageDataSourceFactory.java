package com.pjh.client.database.mybatis;

import com.pjh.client.configuration.DBMSType;
import com.pjh.client.data.DatabaseConfigurationData;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class MessageDataSourceFactory {
    public static DataSource getDataSource(DatabaseConfigurationData config) {
        PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
        Properties dataSourceProperties = new Properties();
        dataSourceProperties.put("driver", getDriver(config.getDbmsType()));
        dataSourceProperties.put("url", getDBUrl(config));
        dataSourceProperties.put("username", config.getDbId());
        dataSourceProperties.put("password", config.getDbPassword());
        pooledDataSourceFactory.setProperties(dataSourceProperties);
        return pooledDataSourceFactory.getDataSource();
    }

    private static String getDriver(DBMSType dbmsType) {
        switch (dbmsType) {
            case MSSQL:
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case MYSQL:
                return "com.mysql.jdbc.Driver";
            case ORACLE:
                return "oracle.jdbc.OracleDriver";
        }
        throw new NullPointerException();
    }

    private static String getDBUrl(DatabaseConfigurationData config) {
        if (!StringUtils.isEmpty(config.getJdbcUrl()))
            return config.getJdbcUrl();

        StringBuilder sb = new StringBuilder();
        switch (config.getDbmsType()) {
            case MSSQL:
                sb.append("jdbc:mysql://");
                sb.append(config.getIpAddress());
                sb.append(":");
                sb.append(config.getPort());
                sb.append(":");
                sb.append(config.getDbName());
            case MYSQL:
                sb.append("jdbc:jtds:sqlserver://");
                sb.append(config.getIpAddress());
                sb.append(":");
                sb.append(config.getPort());
                sb.append(":");
                sb.append(config.getDbName());
                break;
            case ORACLE:
                sb.append("jdbc:oracle:thin:@");
                sb.append(config.getIpAddress());
                sb.append(":");
                sb.append(config.getPort());
                sb.append(":");
                sb.append(config.getDbName());
                break;
        }
        return sb.toString();
    }
}
