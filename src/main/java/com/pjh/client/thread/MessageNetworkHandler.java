package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.message.MessageEntry;

public abstract class MessageNetworkHandler extends TCPRunnable {
    protected Connection connection;

    public MessageNetworkHandler(ServiceConfiguration config, MessageEntry entry, Connection connection) {
        super(config, entry);
        this.connection = connection;
    }
}
