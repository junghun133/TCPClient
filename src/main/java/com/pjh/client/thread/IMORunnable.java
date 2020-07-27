package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.message.MessageEntry;

public abstract class IMORunnable implements Runnable {
    protected ServiceConfiguration config;
    protected MessageEntry entry;

    public IMORunnable(ServiceConfiguration config, MessageEntry entry) {
        this.config = config;
        this.entry = entry;
    }
}
