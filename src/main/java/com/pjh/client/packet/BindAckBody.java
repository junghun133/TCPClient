package com.pjh.client.packet;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessage;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class BindAckBody extends Message implements Receive{

    @Override
    public void setData(byte[] receiveData, PacketBuilder packet) throws InvalidPacketException, CloneNotSupportedException {
        if(header == null)
            throw new InvalidPacketException();
        PacketComponentBuildManager packetComponentBuildManager = new PacketComponentBuildManager();
        MessagePacket messagePacket = (MessagePacket) packet;
        //set packet component
        packetComponentBuildManager.setComponentFromStandardPacketRepository(header.getMsgType(), messagePacket);

        //TODO
        int headerSize = packetComponentBuildManager.getPacketComponentHeader().getTotalSize();
        PacketField packetFieldResult = packetComponentBuildManager.getPacketComponentBody().getFieldObject(PacketFieldLabelMessage.BindAck.RESULT);
        System.arraycopy(receiveData, headerSize, packetFieldResult.content, 0, packetFieldResult.size); headerSize += packetFieldResult.size;

        log.info("{} parameters: {}", header.getMsgType().name(), this.toString());
    }
}
