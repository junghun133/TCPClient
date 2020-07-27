package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.database.DataHandler;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.message.Status;

import java.util.List;

public class MessageSaver extends MessageDataHandler {
    public MessageSaver(ServiceConfiguration config, MessageEntry entry, DataHandler dataHandler) {
        super(config, entry, dataHandler);
    }

    @Override
    public void run() {
        List<Message> updateList = entry.getUpdateList();
        for (Message m : updateList) {
            dataHandler.updateMessage(m);
        }
        updateList.clear();
        List<Message> completeList = entry.getStatusList(Status.Complete);
        for (Message m : completeList) {
            //No면 대기테이블 업데이트
//            dataHandler.updateMessage(m);
            //FIX, 기타면 로그테이블 이관
//            dataHandler.deleteMessage(m);
//            dataHandler.addLogMessage(m);

            entry.deleteMessage(m.getMessageKey());
        }
    }
}
