package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.database.DataHandler;
import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class MessageLoaderTest {

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
    void runTest() throws MessageIndexDuplicatedException {
        MessageLoader loader = new MessageLoader(config, entry, dataHandler);
        when(dataHandler.selectMessage()).thenReturn(Arrays.asList(new Message(), new Message()));
        loader.run();
        doThrow(MessageIndexDuplicatedException.class).when(entry).addNewMessage(any(Message.class));
        loader.run();
    }
}