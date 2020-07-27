package com.pjh.client.data;

import com.pjh.client.database.DataHandler;
import com.pjh.client.message.MessageEntry;
import lombok.Builder;
import lombok.ToString;

@lombok.Data
@Builder
@ToString
public class ServiceConfigurationData implements Data {
    private String cpId;
    private String cpPassword;
    //private String callbackAuth;
    private String serviceType;
    private String ipAddress;
    private int port;
    private int connectTimeout;
    private int socketTimeout;
    private int sendBufferSize;
    private int receiveBufferSize;

    private long linkSendIntervalMs;
    private MessageEntry.EntryType messageEntryType;
    private DataHandler.DataHandlerType dataHandlerType;
}
