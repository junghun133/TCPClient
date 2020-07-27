package com.pjh.client.data;

import com.pjh.client.configuration.ColumnNameMapper;
import com.pjh.client.configuration.DBMSType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class DatabaseConfigurationData implements com.pjh.client.data.Data {
    private String jdbcUrl;
    private String ipAddress;
    private int port;
    private String dbId;
    private String dbPassword;
    private DBMSType dbmsType;
    private String dbName;
    private String queryName;

    private String messageTableName;
    private String logTableName;

    private ColumnNameMapper mapper;

    public void addColumn(ColumnNameMapper.Columns columns, String name) {
        if (mapper == null)
            mapper = new ColumnNameMapper();
        mapper.putColumnMapping(columns, name);
    }

    public ColumnNameMapper getMapper() {
        if (mapper == null)
            mapper = new ColumnNameMapper();
        return mapper;
    }
}
