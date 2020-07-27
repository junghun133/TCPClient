package com.pjh.client.message;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.packet.Header;
import com.pjh.client.packet.Receive;
import com.pjh.client.packet.Submit;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class Message implements Submit, Receive {
    public Header header;

    Long messageKey;
    Status status;
    Result result;
    String cause;

    String phone;
    String callback;
    String filePath;

    LocalDateTime requestDate;

    public Message() {
    }

    public Message(Message m) {
        messageKey = m.messageKey;
        status = m.status;
        result = m.result;
        cause = m.cause;
        phone = m.phone;
        callback = m.callback;
        filePath = m.filePath;
        requestDate = m.requestDate;
    }

    @Override
    public void setData(byte[] receiveData, PacketBuilder packet) throws InvalidPacketException, CloneNotSupportedException {

    }

    @Override
    public byte[] getData(PacketBuilder packet) throws InvalidPacketException {
        return new byte[0];
    }
}
