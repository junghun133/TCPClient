package com.pjh.client.packet;

import com.pjh.client.data.AttachmentManager;
import com.pjh.client.exception.AdditionalPacketException;
import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessageHeader;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class SubmitBody extends Message implements Submit{
    @Setter
    String taskId;
    @Setter
    String svcType;
    String message;

    final String DEST_ADDR_CNT = "1";
    AttachmentManager attachmentManager;

    public SubmitBody(Message m) {
        super(m);
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
