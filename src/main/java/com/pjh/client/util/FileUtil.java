package com.pjh.client.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static boolean existFile(String file) {
        if (file == null)
            return false;
        return new File(file).exists();
    }

    public static String getExtension(String file) {
        if (file == null)
            return "";
        String name = new File(file).getName();
        int idx = name.lastIndexOf('.');
        if (idx == -1)
            return "";

        return name.substring(idx + 1);
    }

    public static String getFileName(String file) {
        if (file == null)
            return "";
        File f = new File(file);
        String name = f.getName();
        int idx = name.lastIndexOf('.');
        if (idx == -1)
            return name;
        else if (idx == 0)
            return "";

        return name.substring(0, idx);
    }

    public static void makeDirectory(String path) throws IOException {
        if(path == null || path.isEmpty())
            return;

        File dir = new File(path);
        if(!dir.exists()) {
            boolean result = dir.mkdir();
            if(!result)
                throw new IOException();
        }
    }
}
