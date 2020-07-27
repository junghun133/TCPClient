package com.pjh.client.application;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.exception.InvalidConfigurationException;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.thread.ThreadManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ServiceManagerTest {
    private static Connection cmMock;
    private static Connection failConnectCmMock;
    private static Connection failBindCmMock;
    private static ThreadManager tmMock;
    private static ServiceConfiguration scSuccess;
    private static ServiceConfiguration scSuccess2;
    private static ServiceConfiguration scConnectFail;
    private static ServiceConfiguration scBindFail;
    private static MessageEntry meSuccess;
    private static MessageEntry meSuccess2;
    private static MessageEntry meConnectFail;
    private static MessageEntry meBindFail;

    @BeforeAll
    public static void setUp() {
        scSuccess = Mockito.mock(ServiceConfiguration.class);
        scSuccess2 = Mockito.mock(ServiceConfiguration.class);
        scConnectFail = Mockito.mock(ServiceConfiguration.class);
        scBindFail = Mockito.mock(ServiceConfiguration.class);
        cmMock = Mockito.mock(Connection.class);
        failConnectCmMock = Mockito.mock(Connection.class);
        failBindCmMock = Mockito.mock(Connection.class);

        when(cmMock.connect()).thenReturn(true);
        when(cmMock.bind(null)).thenReturn(true);
        when(failConnectCmMock.connect()).thenReturn(false);
        when(failBindCmMock.connect()).thenReturn(true);
        when(failBindCmMock.bind(null)).thenReturn(false);

        when(scSuccess.getServiceName()).thenReturn("SUCCESS");
        when(scSuccess2.getServiceName()).thenReturn("SUCCESS2");
        when(scConnectFail.getServiceName()).thenReturn("FAIL");
        when(scConnectFail.getServiceName()).thenReturn("BIND_FAIL");

        tmMock = Mockito.mock(ThreadManager.class);
        when(tmMock.check(scSuccess)).thenReturn(ThreadManager.ServiceStatus.Running);
        when(tmMock.check(scSuccess2)).thenReturn(ThreadManager.ServiceStatus.Running);
        when(tmMock.check(scConnectFail)).thenReturn(ThreadManager.ServiceStatus.Exit);
        when(tmMock.check(scBindFail)).thenReturn(ThreadManager.ServiceStatus.Exit);
        meSuccess = Mockito.mock(MessageEntry.class);
        meSuccess2 = Mockito.mock(MessageEntry.class);
        meConnectFail = Mockito.mock(MessageEntry.class);
        meBindFail = Mockito.mock(MessageEntry.class);
        when(tmMock.getMessageEntry(scSuccess)).thenReturn(meSuccess);
        when(tmMock.getMessageEntry(scSuccess2)).thenReturn(meSuccess2);
        when(tmMock.getMessageEntry(scConnectFail)).thenReturn(null);
        when(tmMock.getMessageEntry(scBindFail)).thenReturn(null);
    }

    @Test
    public void runWithoutConfigTest() {
        ServiceManager app = new ServiceManager(cmMock, tmMock);
        assertThrows(InvalidConfigurationException.class, app::test);
    }

    @Test
    public void diagnosisTest() throws InvalidConfigurationException {
        ServiceManager app = new ServiceManager(cmMock, tmMock);
        app.addService(scSuccess);
        AppTestResult appTestResult = app.test();
        assertNotNull(appTestResult);
        assertEquals(AppTestResult.ResultCode.Success, appTestResult.getResult());
    }

    @Test
    public void diagnosisFailTest() throws InvalidConfigurationException {
        ServiceManager app = new ServiceManager(failConnectCmMock, tmMock);
        ServiceManager appBindFail = new ServiceManager(failBindCmMock, tmMock);

        app.addService(scConnectFail);
        AppTestResult appTestResult = app.test();
        assertNotNull(appTestResult);
        assertEquals(AppTestResult.ResultCode.ConnectFail, appTestResult.getResult());
        assertEquals(scConnectFail.getServiceName(), appTestResult.getCauseServiceName());
        //app.deleteService(scConnectFail);

        appBindFail.addService(scBindFail);
        appTestResult = appBindFail.test();
        assertNotNull(appTestResult);
        assertEquals(AppTestResult.ResultCode.BindFail, appTestResult.getResult());
        assertEquals(scBindFail.getServiceName(), appTestResult.getCauseServiceName());
    }

    @Test
    public void deleteInvalidConfigTest() {
        ServiceManager app = new ServiceManager(cmMock, tmMock);
        assertEquals(0, app.getServiceCount());
        app.deleteService(null);
        app.deleteService(scSuccess);
        assertEquals(0, app.getServiceCount());
    }

    @Test
    public void multipleServiceTest() throws InvalidConfigurationException {
        ServiceManager app = new ServiceManager(cmMock, tmMock);
        //ServiceManager failApp = new ServiceManager(failConnectCmMock, tmMock);
        app.addService(scSuccess);
        assertEquals(1, app.getServiceCount());
        assertEquals(AppTestResult.ResultCode.Success, app.test().getResult());

        app.addService(scConnectFail);
        assertEquals(2, app.getServiceCount());

        app.deleteService(scConnectFail);
        assertEquals(1, app.getServiceCount());
        assertEquals(AppTestResult.ResultCode.Success, app.test().getResult());
        app.addService(scSuccess2);
        assertEquals(2, app.getServiceCount());
        assertEquals(AppTestResult.ResultCode.Success, app.test().getResult());
    }

    @Test
    public void startServiceTest() throws InvalidConfigurationException {
        ServiceManager app = new ServiceManager(cmMock, tmMock);
        app.addService(scSuccess);
        app.addService(scSuccess2);
        app.addService(scConnectFail);

        app.startServices();
        assertEquals(ThreadManager.ServiceStatus.Running, app.checkService(scSuccess));
        assertEquals(ThreadManager.ServiceStatus.Running, app.checkService(scSuccess2));
        assertEquals(ThreadManager.ServiceStatus.Exit, app.checkService(scConnectFail));
        assertNotNull(app.getMessageEntry(scSuccess));
        assertNotNull(app.getMessageEntry(scSuccess2));
        assertNull(app.getMessageEntry(scConnectFail));
        assertNull(app.getMessageEntry(scBindFail));
    }
}
