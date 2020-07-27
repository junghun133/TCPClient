package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.database.DataHandler;
import com.pjh.client.exception.InvalidConfigurationException;
import com.pjh.client.message.MessageEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ThreadManagerTest {
    private static ServiceConfiguration scSuccess;
    private static ServiceConfiguration scFailConnect;
    private static ServiceConfiguration scFailBind;
    private static ServiceConfiguration scNullName;
    private static ServiceConfiguration scEmptyName;
    private static ServiceConfigurationData scdMap;
    private static Connection cm;
    private static Connection cmConnectFail;
    private static Connection cmBindFail;

    @BeforeAll
    public static void setup() {
        scdMap = Mockito.mock(ServiceConfigurationData.class);
        when(scdMap.getMessageEntryType()).thenReturn(MessageEntry.EntryType.Map);
        when(scdMap.getDataHandlerType()).thenReturn(DataHandler.DataHandlerType.Dummy);
        scSuccess = Mockito.mock(ServiceConfiguration.class);
        when(scSuccess.getServiceName()).thenReturn("S1");
        when(scSuccess.getServiceData()).thenReturn(scdMap);
        scFailConnect = Mockito.mock(ServiceConfiguration.class);
        when(scFailConnect.getServiceName()).thenReturn("F1");
        when(scFailConnect.getServiceData()).thenReturn(scdMap);
        scFailBind = Mockito.mock(ServiceConfiguration.class);
        when(scFailBind.getServiceName()).thenReturn("F2");
        when(scFailBind.getServiceData()).thenReturn(scdMap);
        scNullName = Mockito.mock(ServiceConfiguration.class);
        when(scNullName.getServiceName()).thenReturn(null);
        when(scFailConnect.getServiceData()).thenReturn(scdMap);
        scEmptyName = Mockito.mock(ServiceConfiguration.class);
        when(scEmptyName.getServiceName()).thenReturn("");
        when(scFailConnect.getServiceData()).thenReturn(scdMap);

        cm = Mockito.mock(Connection.class);
        when(cm.connect()).thenReturn(true);
        when(cm.bind(null)).thenReturn(true);
        cmConnectFail = Mockito.mock(Connection.class);
        when(cmConnectFail.connect()).thenReturn(false);
        when(cmConnectFail.bind(null)).thenReturn(false);
        cmBindFail = Mockito.mock(Connection.class);
        when(cmBindFail.connect()).thenReturn(true);
        when(cmBindFail.bind(null)).thenReturn(false);
    }

    private static Stream<Arguments> provideInvalidArgumentForStartThread() { // argument source method
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(cm, null),
                Arguments.of(null, scSuccess),
                Arguments.of(null, scNullName),
                Arguments.of(null, scEmptyName),
                Arguments.of(cm, scNullName),
                Arguments.of(cm, scEmptyName)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentForStartThread")
    public void startThreadWithInvalidParameterTest(Connection connectionManager, ServiceConfiguration serviceConfiguration) {
        ThreadManager tm = new ThreadManager();
        assertThrows(InvalidConfigurationException.class, () -> tm.startThread(connectionManager, serviceConfiguration));
    }

    @Test
    public void startThreadTest() throws InvalidConfigurationException, InterruptedException {
        ThreadManager tm = new ThreadManager();
        assertTrue(tm.startThread(cm, scSuccess));
        Thread.sleep(100);
        assertEquals(ThreadManager.ServiceStatus.Running, tm.check(scSuccess));
    }

    @Test
    public void startThreadWithInvalidConfig() throws InvalidConfigurationException {
        ThreadManager tm = new ThreadManager();
        assertFalse(tm.startThread(cmConnectFail, scFailConnect));
        assertEquals(ThreadManager.ServiceStatus.Exit, tm.check(scFailConnect));
        assertFalse(tm.startThread(cmBindFail, scFailBind));
        assertEquals(ThreadManager.ServiceStatus.Exit, tm.check(scFailBind));
    }

    @Test
    public void stopThreadTest() throws InvalidConfigurationException, InterruptedException {
        ThreadManager tm = new ThreadManager();
        assertTrue(tm.startThread(cm, scSuccess));
        Thread.sleep(200);
        assertEquals(ThreadManager.ServiceStatus.Running, tm.check(scSuccess));
        tm.stopThread(scSuccess);
        Thread.sleep(200);
        assertEquals(ThreadManager.ServiceStatus.Exit, tm.check(scSuccess));
    }
}