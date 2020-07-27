package com.pjh.client.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class URLUtil {
    public static URL getResourceURL(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath))
            throw new IOException();

        return Thread.currentThread().getContextClassLoader().getResource(filePath);
    }

    public static URL parsStringToURL(String filePath) throws IOException{
        if(filePath == null)
            return null;

        return new File(filePath).toURI().toURL();
    }
}
