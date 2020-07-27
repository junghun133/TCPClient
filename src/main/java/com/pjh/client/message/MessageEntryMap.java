package com.pjh.client.message;

import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.exception.NoSuchMessageException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MessageEntryMap implements MessageEntry {
    ConcurrentHashMap<Long, Message> messages = new ConcurrentHashMap<>();
    private List<Message> updateList = new ArrayList<>();
//    ConcurrentHashMap<status, List<Long>> statusMap = new ConcurrentHashMap<>();

    public MessageEntryMap() {
//        for (status s : status.values()) {
//            statusMap.put(s, new ArrayList<>());
//        }
    }

    @Override
    public void addNewMessage(Message message) throws MessageIndexDuplicatedException {
        if (messages.containsKey(message.getMessageKey()))
            throw new MessageIndexDuplicatedException();
        messages.put(message.getMessageKey(), message);
//        statusMap.get(message.getStatus()).add(message.getMessageKey());
    }

    @Override
    public void deleteMessage(Long index) {
        if (!messages.containsKey(index))
            return;
        messages.remove(index);
    }

    @Override
    public int getTotalMessageCount() {
        return messages.size();
    }

    @Override
    public List<Message> getStatusList(Status status) {
        List<Message> ret = new ArrayList<>();
//        for (Long index : statusMap.get(status)) {
//            ret.add(messages.get(index));
//        }
        for (Message m : messages.values()) {
            if (m.getStatus() == status) {
                ret.add(m);
            }
        }
        return ret;
    }

    @Override
    public List<Message> getAllMessageList() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Message getMessage(Long index) throws NoSuchMessageException {
        if (!messages.containsKey(index))
            throw new NoSuchMessageException();
        return messages.get(index);
    }

    @Override
    public void backup() {
        log.info("Message Entry backup : size [{}]", messages.size());
        for (Message m : messages.values()) {
            //some backup codes
            log.debug(m.toString());
        }
    }

    @Override
    public void updateMessage(Message message) throws NoSuchMessageException {
        if (message == null || !messages.containsKey(message.getMessageKey()))
            throw new NoSuchMessageException();

        Message originalMessage = messages.get(message.getMessageKey());
        updateList.remove(originalMessage);
        messages.put(message.getMessageKey(), message);
        updateList.add(message);
    }

    @Override
    public List<Message> getUpdateList() {
        return updateList;
    }
}
