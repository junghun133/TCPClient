package com.pjh.client.configuration;

import com.pjh.client.exception.ConfigurationFileLoadException;
import com.pjh.client.util.PathUtil;
import com.pjh.client.util.URLUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ConfigurationFileReader {
    public static ConfigurationExtensionType findConfigExtension() throws IOException {
        File directory = FileUtils.getFile(URLUtil.parsStringToURL(PathUtil.getInstance().getPath(PathUtil.Folders.CONFIG)).getPath());
        List<File> fileList = (List<File>) FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        if(fileList.size() <= 0)
            throw new IOException();

        return ConfigurationExtensionType.valueOf(FilenameUtils.getExtension(fileList.get(0).getName()).toUpperCase());
    }

    public File getFile(Configuration.ServiceType serviceType, ConfigurationExtensionType configurationExtensionType) throws ConfigurationFileLoadException, IOException {
        ConfigurationProperties.ConfigurationFileName configurationFileName = null;
        switch (serviceType){
            case SVC:
                configurationFileName = ConfigurationProperties.ConfigurationFileName.SERVER;
                break;
            case DB:
                configurationFileName = ConfigurationProperties.ConfigurationFileName.DATABASE;
                break;
        }
        if(configurationFileName == null) return null;

        URL url = URLUtil.parsStringToURL(PathUtil.getInstance().getPath(PathUtil.Folders.CONFIG) + configurationFileName.getFileName(configurationExtensionType));
        return FileUtils.getFile(url.getPath());
    }
}
