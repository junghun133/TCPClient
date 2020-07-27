package com.pjh.client.application;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.connection.ConnectionManager;
import com.pjh.client.data.ServiceConfigurationData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectionManagerTest {
    private static Connection cmMock;
    private static ServiceConfiguration serviceMock;
    private static ServiceConfiguration serviceConfiguration;
    private static ConnectionManager connectionManager;

    @BeforeAll
    public static void setUp(){
        ServiceConfigurationData scServiceConfigurationData = ServiceConfigurationData
                .builder()
                .ipAddress("121.167.147.14")
                .port(9111)
                .connectTimeout(5000)
                .socketTimeout(5000)
                .receiveBufferSize(10240)
                .sendBufferSize(10240).build();

        serviceMock = Mockito.mock(ServiceConfiguration.class);
        serviceConfiguration = new ServiceConfiguration("");
        serviceConfiguration.setServiceData(scServiceConfigurationData);
        //when(serviceMock.getServerConnectionConfiguration()).thenReturn((Configuration) scServiceConfigurationData);
    }

    @Test
    public void initailizeConnectionManagerTest() throws InterruptedException {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.init(serviceConfiguration);
        assertNotNull(connectionManager.getSocketManager());
        assertTrue(connectionManager.connect());
    }

    @Test
    public void sendTest()  {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.init(serviceMock);
    }

    @Test
    public void makePacketTest(){

    }
}
