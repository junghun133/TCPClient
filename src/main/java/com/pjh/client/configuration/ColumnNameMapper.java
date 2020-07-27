package com.pjh.client.configuration;

import java.util.HashMap;
import java.util.Map;

public class ColumnNameMapper {
    Map<Columns, String> map = new HashMap<>();

    public ColumnNameMapper() {
        for (Columns c : Columns.values()) {
            map.put(c, c.name());
        }
    }

    public void putColumnMapping(Columns c, String mappedName) {
        map.put(c, mappedName);
    }

    public String getMappedColumnName(Columns c) {
        return map.get(c);
    }

    public enum Columns {messageKey, status, result, cause, phone, callback, filePath, requestDate}
}
