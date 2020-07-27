package com.pjh.client.packet;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class BindBody extends Message implements Submit{
    @Setter
    private String authId;
    @Setter
    private String cid;

    @Override
    public byte[] getData(PacketBuilder packet) throws InvalidPacketException {
        if(header == null)
            throw new InvalidPacketException();

        PacketComponentBuildManager packetComponentBuildManager = new PacketComponentBuildManager();
        MessagePacket messagePacket = (MessagePacket) packet;
        try {
            packetComponentBuildManager.setComponentFromStandardPacketRepository(header.getMsgType(), messagePacket);

            //TODO
            log.info("{} parameters: {}", header.getMsgType().name(), this.toString());
            return packetComponentBuildManager.getSendPacket();
        } catch (CloneNotSupportedException e) {
            log.info("Make packet fail.. type:" + header.getMsgType().getPacketCategory());
            e.printStackTrace();
        }
        throw new InvalidPacketException();
    }
}