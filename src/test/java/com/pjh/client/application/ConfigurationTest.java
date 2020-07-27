package com.pjh.client.application;

import com.pjh.client.configuration.Configuration;
import com.pjh.client.configuration.ConfigurationExtensionType;
import com.pjh.client.configuration.ConfigurationFileReader;
import com.pjh.client.configuration.ConfigurationLoaderFactory;
import com.pjh.client.configuration.transfer.TransferDataFromYAML;
import com.pjh.client.data.DatabaseConfigurationData;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.exception.NotSupportedPlatformException;
import com.pjh.client.exception.NotSupportedServiceTypeException;
import com.pjh.client.util.URLUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    @BeforeAll
    public static void setUp(){
    }

    private static Stream<Arguments> successReturnExtensionArgumentForConfigurationFactory() {
        return Stream.of(
                Arguments.of(Configuration.ServiceType.SVC),
                Arguments.of(Configuration.ServiceType.DB)
        );
    }

    private static Stream<Arguments> invalidExtensionArgumentForConfigurationFactory(){
        return Stream.of(
                Arguments.of(Configuration.ServiceType.SVC, ConfigurationExtensionType.ETC),
                Arguments.of(Configuration.ServiceType.SVC, null),
                Arguments.of(Configuration.ServiceType.SVC, null)
        );
    }

    private static Stream<Arguments> invalidServiceTypeArgumentForConfigurationFactory(){
        return Stream.of(
                Arguments.of(Configuration.ServiceType.ETC, ConfigurationExtensionType.YML),
                Arguments.of(null, ConfigurationExtensionType.YML)
        );
    }


    @ParameterizedTest
    @MethodSource("invalidExtensionArgumentForConfigurationFactory")
    void invalidPlatformArgTest(Configuration.ServiceType serviceType, ConfigurationExtensionType configurationExtensionType){
        ConfigurationLoaderFactory cf = new ConfigurationLoaderFactory();
        assertThrows(NotSupportedPlatformException.class, () -> cf.getConfig(serviceType, configurationExtensionType));
    }

    @ParameterizedTest
    @MethodSource("invalidServiceTypeArgumentForConfigurationFactory")
    void invalidServiceArgTest(Configuration.ServiceType serviceType, ConfigurationExtensionType configurationExtensionType){
        ConfigurationLoaderFactory cf = new ConfigurationLoaderFactory();
        assertThrows(NotSupportedServiceTypeException.class, () -> cf.getConfig(serviceType, configurationExtensionType));
    }

    @Test
    @Disabled
    public void UrlTest() throws IOException {
        // YML Test
        String serverConfigPath = "config/server.yml";
        String databaseConfigPath = "config/database.yml";
        assertThrows(IOException.class, () -> URLUtil.getResourceURL(""));
        assertThrows(IOException.class, () -> URLUtil.getResourceURL(null));
    }


    @Test
    @Disabled
    public void ConfigurationFileReaderTest() throws IOException, ConfigurationFileLoadException {
        ConfigurationFileReader configurationFileReader = new ConfigurationFileReader();
        assertNotNull(configurationFileReader.getFile(Configuration.ServiceType.SVC, ConfigurationExtensionType.YML));
    }

    @ParameterizedTest
    @Disabled
    @MethodSource("successReturnExtensionArgumentForConfigurationFactory")
    public void SuccTransferDataReturnTest(Configuration.ServiceType serviceType) throws ConfigurationFileLoadException {
        TransferDataFromYAML transferDataFromYAML = new TransferDataFromYAML(serviceType);
        switch (serviceType){
            case SVC:
                assertEquals(ServiceConfigurationData.class, transferDataFromYAML.getData().getClass()) ;
                break;
            case DB:
                assertEquals(DatabaseConfigurationData.class, transferDataFromYAML.getData().getClass());
                break;
        }
    }

    @Test
    @Disabled
    public void configurationExtensionTypeCheckTest() throws IOException {
        ConfigurationFileReader configurationFileReader = new ConfigurationFileReader();
        //yml
        assertEquals(configurationFileReader.findConfigExtension(), ConfigurationExtensionType.YML);

        //xml
        //assertEquals(configurationFileReader.findConfigExtension(), ConfigurationExtensionType.XML);
        //json
        //assertEquals(configurationFileReader.findConfigExtension(), ConfigurationExtensionType.JSON);
       }
}
