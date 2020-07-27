package com.pjh.client.message;

import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.exception.NoSuchMessageException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class MessageEntryMapTest {
    static Message m1;
    static Message m2;
    MessageEntryMap entry;

    @BeforeAll
    static void allSetup() {
        m1 = Mockito.mock(Message.class);
        when(m1.getMessageKey()).thenReturn(1L);
        when(m1.getStatus()).thenReturn(Status.Initial);
        m2 = Mockito.mock(Message.class);
        when(m2.getMessageKey()).thenReturn(2L);
        when(m2.getStatus()).thenReturn(Status.Initial);
    }

    @BeforeEach
    void setup() {
        entry = new MessageEntryMap();
    }

    @Test
    void messageAddTest() throws MessageIndexDuplicatedException {
        entry.addNewMessage(m1);
        assertEquals(1, entry.getTotalMessageCount());
        entry.addNewMessage(m2);
        assertEquals(2, entry.getTotalMessageCount());
    }

    @Test
    void duplicatedIndexAddTest() throws MessageIndexDuplicatedException {
        entry.addNewMessage(m1);
        assertThrows(MessageIndexDuplicatedException.class, () -> entry.addNewMessage(m1));
    }

    @Test
    void getMessageListTest() throws MessageIndexDuplicatedException, NoSuchMessageException {
        int initialCnt = 100;
        int submittedCnt = 200;
        int reportedCnt = 300;
        int completedCnt = 123;
        List<Message> iList = new ArrayList<>();
        List<Message> sList = new ArrayList<>();
        List<Message> rList = new ArrayList<>();
        List<Message> cList = new ArrayList<>();
        List<Message> allList = new ArrayList<>();

        long index = 0L;
        for (int i = 0; i < initialCnt; i++) {
            Message m = Mockito.mock(Message.class);
            when(m.getMessageKey()).thenReturn(index++);
            when(m.getStatus()).thenReturn(Status.Initial);
            entry.addNewMessage(m);
            iList.add(m);
            allList.add(m);
            assertEquals(m, entry.getMessage(m.getMessageKey()));
        }
        for (int i = 0; i < submittedCnt; i++) {
            Message m = Mockito.mock(Message.class);
            when(m.getMessageKey()).thenReturn(index++);
            when(m.getStatus()).thenReturn(Status.Submitted);
            entry.addNewMessage(m);
            sList.add(m);
            allList.add(m);
            assertEquals(m, entry.getMessage(m.getMessageKey()));
        }
        for (int i = 0; i < reportedCnt; i++) {
            Message m = Mockito.mock(Message.class);
            when(m.getMessageKey()).thenReturn(index++);
            when(m.getStatus()).thenReturn(Status.Reported);
            entry.addNewMessage(m);
            rList.add(m);
            allList.add(m);
            assertEquals(m, entry.getMessage(m.getMessageKey()));
        }
        for (int i = 0; i < completedCnt; i++) {
            Message m = Mockito.mock(Message.class);
            when(m.getMessageKey()).thenReturn(index++);
            when(m.getStatus()).thenReturn(Status.Complete);
            entry.addNewMessage(m);
            cList.add(m);
            allList.add(m);
            assertEquals(m, entry.getMessage(m.getMessageKey()));
        }

        assertEquals(initialCnt + submittedCnt + reportedCnt + completedCnt,
                entry.getTotalMessageCount());

        assertEquals(iList, entry.getStatusList(Status.Initial));
        assertEquals(sList, entry.getStatusList(Status.Submitted));
        assertEquals(rList, entry.getStatusList(Status.Reported));
        assertEquals(cList, entry.getStatusList(Status.Complete));

        assertEquals(allList, entry.getAllMessageList());
    }

    @Test
    void getMessageNoIndexTest() {
        assertThrows(NoSuchMessageException.class, () -> entry.getMessage(0L));
        assertThrows(NoSuchMessageException.class, () -> entry.updateMessage(null));
    }

    @Test
    void updateMessageTest() throws MessageIndexDuplicatedException, NoSuchMessageException {
        entry.addNewMessage(m1);
        Message modify = Mockito.mock(Message.class);
        when(modify.getMessageKey()).thenReturn(1L);
        when(modify.getStatus()).thenReturn(Status.Complete);
        entry.updateMessage(modify);
        Message m = entry.getMessage(m1.getMessageKey());
        assertEquals(modify.getStatus(), m.getStatus());
    }
}
