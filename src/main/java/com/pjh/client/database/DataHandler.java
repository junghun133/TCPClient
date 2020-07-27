package com.pjh.client.database;

import com.pjh.client.message.Message;

import java.util.List;

public interface DataHandler {
    List<Message> selectMessage();

    void updateMessage(Message m);

    void deleteMessage(Message m);

    void addLogMessage(Message m);

    boolean connectTest();

    void cleanup();

    enum DataHandlerType {Dummy, Mybatis}
}
