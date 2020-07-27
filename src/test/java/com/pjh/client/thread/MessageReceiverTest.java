package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.packet.Header;
import com.pjh.client.packet.ReportBody;
import com.pjh.client.packet.SubmitAckBody;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class MessageReceiverTest {

    private static ServiceConfiguration sc;
    private static MessageEntry entry;
    private static Connection connection;

    private static SubmitAckBody submitAckBody;
    private static ReportBody reportBody;

    @BeforeAll
    public static void setUp() {
        sc = Mockito.mock(ServiceConfiguration.class);
        entry = Mockito.mock(MessageEntry.class);
        connection = Mockito.mock(Connection.class);
        submitAckBody = Mockito.mock(SubmitAckBody.class);
        reportBody = Mockito.mock(ReportBody.class);

        when(connection.receiveSubmitAckBody(any(Header.class))).thenReturn(submitAckBody);
        when(connection.receiveReportBody(any(Header.class))).thenReturn(reportBody);
    }
/*
    @Test
    void runTest() {
        MessageReceiver receiver = new MessageReceiver(sc, entry, connection);
        for (PacketType t : PacketType.values()) {
            when(connection.receiveType()).thenReturn(t);
            receiver.run();
        }
    }
    @Test
    void updateMessageErrorTest() throws MessageIndexDuplicatedException, NoSuchMessageException {
        MessageReceiver receiver = new MessageReceiver(sc, entry, connection);
        doThrow(NoSuchMessageException.class).when(entry).updateMessage(any(Message.class));
        for (PacketType t : PacketType.values()) {
            when(connection.receiveType()).thenReturn(t);
            receiver.run();
        }
    }
*/
}