package com.pjh.client.thread;

import com.pjh.client.database.DataHandler;
import com.pjh.client.message.MessageEntry;
import lombok.Getter;

import java.util.concurrent.ScheduledExecutorService;

@Getter
public class ServiceData {

    private final ScheduledExecutorService scheduledExecutorService;
    private final MessageEntry messageEntry;
    private final DataHandler dataHandler;

    public ServiceData(ScheduledExecutorService scheduledExecutorService, MessageEntry messageEntry, DataHandler dataHandler) {

        this.scheduledExecutorService = scheduledExecutorService;
        this.messageEntry = messageEntry;
        this.dataHandler = dataHandler;
    }
}
