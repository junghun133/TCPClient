package com.pjh.client.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PathUtil {
    public enum Folders {
        LOG("logs"), CONFIG("config"), BIN("bin");
        private final String folderName;

        Folders(String folderName) {
            this.folderName = folderName;
        }

        public String getFolderName() {
            return folderName;
        }
    }

    private String homePath;
    private Map<Folders, String> folderNames = new HashMap<>();

    private PathUtil() {
        homePath = System.getProperty("BasePath");
        if (homePath == null)
            homePath = ".";
        if (!homePath.endsWith(File.separator))
            homePath += File.separator;
        for (Folders f : Folders.values()) {
            try {
                FileUtil.makeDirectory(homePath + f.getFolderName());
            } catch (IOException e) {
                log.info("Unavailable to make directory!");
            }
            folderNames.put(f, homePath + f.getFolderName());
        }
    }

    public String getPath(Folders f) {
        return folderNames.get(f);
    }

    public static PathUtil getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        public static PathUtil INSTANCE = new PathUtil();
    }
}
