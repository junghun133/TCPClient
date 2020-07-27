package com.pjh.client.configuration;

public enum ConfigurationExtensionType {
    XML, YML, JSON, ETC;

    public static String[] names(){
        ConfigurationExtensionType[] configurationExtensionTypes = values();
        String[] names = new String[configurationExtensionTypes.length];

        for(int i = 0; i < configurationExtensionTypes.length; i++){
            names[i] = configurationExtensionTypes[i].name().toLowerCase();
        }
        return names;
    }
}
