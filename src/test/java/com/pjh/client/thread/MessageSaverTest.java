package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.database.DataHandler;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

class MessageSaverTest {

    private static ServiceConfiguration config;
    private static MessageEntry entry;
    private static DataHandler dataHandler;

    @BeforeAll
    static void setUp() {
        config = Mockito.mock(ServiceConfiguration.class);
        entry = Mockito.mock(MessageEntry.class);
        dataHandler = Mockito.mock(DataHandler.class);
    }

    @Test
    void run() {
        MessageSaver saver = new MessageSaver(config, entry, dataHandler);
        when(entry.getStatusList(any())).thenReturn(Arrays.asList(new Message(), new Message()));
//        when(entry.getUpdateList()).thenReturn(Arrays.asList(new Message(), new Message()));
        saver.run();
    }
}