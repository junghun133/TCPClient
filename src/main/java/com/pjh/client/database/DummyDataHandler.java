package com.pjh.client.database;

import com.pjh.client.message.Message;

import java.util.List;

public class DummyDataHandler implements DataHandler {
    @Override
    public List<Message> selectMessage() {
        return null;
    }

    @Override
    public void updateMessage(Message m) {

    }

    @Override
    public void deleteMessage(Message m) {

    }

    @Override
    public void addLogMessage(Message m) {

    }

    @Override
    public boolean connectTest() {
        return true;
    }

    @Override
    public void cleanup() {

    }
}
