package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.exception.InvalidPacketTypeException;
import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.exception.NoSuchMessageException;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.message.Status;
import com.pjh.client.packet.Header;
import com.pjh.client.packet.PacketType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageReceiver extends MessageNetworkHandler {
    public  MessageReceiver(ServiceConfiguration config, MessageEntry entry, Connection connection) {
        super(config, entry, connection);
    }

    @Override
    public void run() {
        Header header = connection.receiveHeader();
        if (header == null)
            return;

        PacketType type = header.getMsgType();

        if (type == null) {
//            log.info("Receive packet header NULL");
            return;
        }
        Message body;
        try {
            body = getBody(type, header);
        } catch (InvalidPacketTypeException e) {
            //error handling
            return;
        }
        if (needDBProcess(type)) {
            try {
                entry.updateMessage(body);
            } catch (NoSuchMessageException | MessageIndexDuplicatedException e) {
                // error handling
                log.info("Message INDEX:{} error! : {}", body.getMessageKey(), e.toString());
            }
        }
    }

    private boolean needDBProcess(PacketType type) {
        switch (type){
            case LinkAck:
            case BindAck:
                return false;
        }
        return true;
    }

    private Message getBody(PacketType type, Header header) throws InvalidPacketTypeException {
        Message m;
        switch (type) {
            case BindAck:
                m = connection.receiveBindAckBody(header);
                break;
            case SubmitAck:
                m = connection.receiveSubmitAckBody(header);
                m.setStatus(Status.Submitted);
                break;
            case Report:
                m = connection.receiveReportBody(header);
                m.setStatus(Status.Reported);
                break;
            default:
                throw new InvalidPacketTypeException();
        }
        return m;
    }
}
