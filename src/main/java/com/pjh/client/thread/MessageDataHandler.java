package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.database.DataHandler;
import com.pjh.client.message.MessageEntry;

public abstract class MessageDataHandler extends IMORunnable {
    protected DataHandler dataHandler;

    public MessageDataHandler(ServiceConfiguration config, MessageEntry entry, DataHandler dataHandler) {
        super(config, entry);
        this.dataHandler = dataHandler;
    }
}
