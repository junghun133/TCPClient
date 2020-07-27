package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.database.DataHandler;
import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.message.Result;
import com.pjh.client.message.Status;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageLoader extends MessageDataHandler {

    public MessageLoader(ServiceConfiguration config, MessageEntry entry, DataHandler dataHandler) {
        super(config, entry, dataHandler);
    }

    @Override
    public void run() {
        List<Message> sendList = dataHandler.selectMessage();
        if (sendList == null || sendList.size() == 0)
            return;

        log.info("Message Selected (size:{})", sendList.size());

        for (Message m : sendList) {
            try {
                m.setStatus(Status.Selected);
                dataHandler.updateMessage(m);
                entry.addNewMessage(m);
            } catch (MessageIndexDuplicatedException e) {
                log.info("Message index duplicated, update to [FAIL]");
                m.setStatus(Status.Complete);
                m.setResult(Result.ETC);
                dataHandler.updateMessage(m);
            }
        }
    }
}
