package com.pjh.client.message;

import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.exception.NoSuchMessageException;

import java.util.List;

public interface MessageEntry {
    void addNewMessage(Message message) throws MessageIndexDuplicatedException;

    void deleteMessage(Long index);

    int getTotalMessageCount();

    List<Message> getStatusList(Status status);

    void updateMessage(Message message) throws NoSuchMessageException, MessageIndexDuplicatedException;

    List<Message> getUpdateList();

    List<Message> getAllMessageList();

    Message getMessage(Long index) throws NoSuchMessageException;

    void backup();

    enum EntryType {Map}
}
