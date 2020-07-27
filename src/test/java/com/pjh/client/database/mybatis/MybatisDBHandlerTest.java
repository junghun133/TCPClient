package com.pjh.client.database.mybatis;

import com.pjh.client.configuration.ColumnNameMapper;
import com.pjh.client.configuration.DBMSType;
import com.pjh.client.data.DatabaseConfigurationData;
import com.pjh.client.message.Message;
import com.pjh.client.message.Result;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MybatisDBHandlerTest {

    @Disabled
    @Test
    void selectMessageTest() {
        Result[] r = Result.class.getEnumConstants();
        DatabaseConfigurationData config = Mockito.mock(DatabaseConfigurationData.class);
        when(config.getDbmsType()).thenReturn(DBMSType.ORACLE);
        when(config.getIpAddress()).thenReturn("121.167.147.20");
        when(config.getPort()).thenReturn(1521);
        when(config.getDbId()).thenReturn("pjh");
        when(config.getDbPassword()).thenReturn("pjh");
        when(config.getDbName()).thenReturn("orcl");
        ColumnNameMapper mapper = new ColumnNameMapper();
        mapper.putColumnMapping(ColumnNameMapper.Columns.messageKey, "msgkey");
        mapper.putColumnMapping(ColumnNameMapper.Columns.result, "R_RESULT");
        when(config.getMapper()).thenReturn(mapper);
        MybatisDBHandler dbHandler = new MybatisDBHandler("test1", config);
        assertNotNull(dbHandler);

        List<Message> messageList = dbHandler.selectMessage();
        assertNotNull(messageList);
        for (Message m : messageList) {
            System.out.println(m);
        }
    }
}