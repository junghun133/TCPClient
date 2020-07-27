package com.pjh.client.packet;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubmitAckBody extends Message implements Receive {

    public SubmitAckBody() {
    }

    public SubmitAckBody(Message message) {
        super(message);
    }

    @Override
    public void setData(byte[] receiveData, PacketBuilder packet) throws InvalidPacketException, CloneNotSupportedException {
        if (header == null)
            throw new InvalidPacketException();
        PacketComponentBuildManager packetComponentBuildManager = new PacketComponentBuildManager();
        MessagePacket messagePacket = (MessagePacket) packet;
        //set packet component
        packetComponentBuildManager.setComponentFromStandardPacketRepository(header.getMsgType(), messagePacket);

        int headerSize = packetComponentBuildManager.getPacketComponentHeader().getTotalSize();

        //TODO
        log.info("{} parameters: {}", header.getMsgType().name(), this.toString());
    }
}
