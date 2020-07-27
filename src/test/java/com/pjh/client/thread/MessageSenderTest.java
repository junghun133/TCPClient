package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.database.DataHandler;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.message.Status;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class MessageSenderTest {

    private static ServiceConfiguration scNormal;
    private static MessageEntry entry;
    private static Connection connection;

    private static Message m1;
    private static Message m2;
    private static DataHandler dataHandler;

    @BeforeAll
    public static void setup() {
        scNormal = Mockito.mock(ServiceConfiguration.class);
        entry = Mockito.mock(MessageEntry.class);
        connection = Mockito.mock(Connection.class);
        m1 = Mockito.mock(Message.class);
        m2 = Mockito.mock(Message.class);
        when(m1.getMessageKey()).thenReturn(1L);
        when(m2.getMessageKey()).thenReturn(2L);
        when(m1.getStatus()).thenReturn(Status.Initial);
        when(m2.getStatus()).thenReturn(Status.Initial);

        when(entry.getStatusList(Status.Initial)).thenReturn(Arrays.asList(m1, m2));

        dataHandler = Mockito.mock(DataHandler.class);
    }
/*
    @Test
    public void senderRunTest() {
        MessageSender sender = new MessageSender(scNormal, entry, connection);
        for (PacketType t : PacketType.values()) {
            when(connection.receiveType()).thenReturn(t);
            sender.run();
        }

    }

    @Test
    void updateMessageErrorTest() throws MessageIndexDuplicatedException, NoSuchMessageException {
        MessageSender sender = new MessageSender(scNormal, entry, connection);
        doThrow(NoSuchMessageException.class).when(entry).updateMessage(any(Message.class));
        for (PacketType t : PacketType.values()) {
            when(connection.receiveType()).thenReturn(t);
            sender.run();
        }
    }
 */
}