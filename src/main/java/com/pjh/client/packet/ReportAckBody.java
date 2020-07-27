package com.pjh.client.packet;

import com.pjh.client.exception.AdditionalPacketException;
import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.message.Result;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessage;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessageHeader;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportAckBody extends Message implements Submit {
    @Setter
    String taskId;

    public ReportAckBody(Message message) {
        super(message);
    }

    @Override
    public byte[] getData(PacketBuilder packet) throws InvalidPacketException {
        if(header == null)
            throw new InvalidPacketException();

        PacketComponentBuildManager packetComponentBuildManager = new PacketComponentBuildManager();
        MessagePacket messagePacket = (MessagePacket) packet;
        try {
            packetComponentBuildManager.setComponentFromStandardPacketRepository(header.getMsgType(), messagePacket);

            packetComponentBuildManager.packetComponentHeader.setPacketContent(PacketFieldLabelMessageHeader.header.MSG_TYPE, Integer.toString(header.getMsgType().getPacketCategory()).getBytes());
            packetComponentBuildManager.packetComponentBody.setPacketContent(PacketFieldLabelMessage.ReportAck.RESULT, Result.SUCCESS.getStringValue().getBytes());

            //TODO
            packetComponentBuildManager.appendByte();

            log.info("{} parameters: {}", header.getMsgType().name(), this.toString());
            return packetComponentBuildManager.getSendPacket();
        } catch (CloneNotSupportedException | AdditionalPacketException e) {
            log.info("Make packet fail.. type:" + header.getMsgType().getPacketCategory());
            e.printStackTrace();
        }
        throw new InvalidPacketException();
    }
}
