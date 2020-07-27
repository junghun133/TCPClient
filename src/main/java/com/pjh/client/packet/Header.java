package com.pjh.client.packet;

import com.pjh.client.packet.messagePacket.PacketFieldLabelMessageHeader;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Data;

import java.util.List;

@Data
public class Header {
    MessagePacket packet;

    private PacketType msgType;
    private String sessionId;
    private String reqTime;
    private String bodyLen;

    public void setData(byte[] headerByteArray) {
        List<PacketField> headerList = packet.getStandardPacketRepository().getPacketInstance().get(PacketType.Header);
        PacketComponentRepository packetComponentRepository = new PacketComponentRepository(headerList);

        PacketField packetFieldMsgType = packetComponentRepository.getFieldObject(PacketFieldLabelMessageHeader.header.MSG_TYPE);

        int sourcePos = 0;
        System.arraycopy(headerByteArray, sourcePos, packetFieldMsgType.content, 0, packetFieldMsgType.size); sourcePos += packetFieldMsgType.size;

        msgType = PacketType.findPacketType(Integer.parseInt(packetFieldMsgType.contentToString()));
    }
}
