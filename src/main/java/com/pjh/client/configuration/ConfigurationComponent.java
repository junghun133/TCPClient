package com.pjh.client.configuration;

import com.pjh.client.data.Data;

public interface ConfigurationComponent {
    void setData(Data data);
    Data getData();
}
