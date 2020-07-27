package com.pjh.client.packet;

import com.pjh.client.exception.AdditionalPacketException;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Data;

import java.nio.ByteBuffer;


@Data
public class PacketComponentBuildManager {
    PacketComponent packetComponentHeader;
    PacketComponent packetComponentBody;

    byte[] sendPacket;

    public void appendByte() throws AdditionalPacketException {
        if(packetComponentHeader == null|| packetComponentBody == null)
            throw new AdditionalPacketException();

        byte[] header =  packetComponentHeader.getData();
        byte[] body =  packetComponentBody.getData();

        byte[] allByteArray = new byte[header.length + body.length];

        ByteBuffer byteBuffer = ByteBuffer.wrap(allByteArray);
        byteBuffer.put(header);
        byteBuffer.put(body);

        sendPacket = byteBuffer.array();
    }

    public void setComponentFromStandardPacketRepository(PacketType packetType, PacketBuilder packetBuilder) throws CloneNotSupportedException {
        MessagePacket messagePacket = (MessagePacket) packetBuilder;
        StandardPacketRepository standardPacketRepository = messagePacket.getStandardPacketRepository().clone();
        packetComponentHeader = new PacketComponentRepository(standardPacketRepository.getPacketInstance().get(PacketType.Header));
        packetComponentBody = new PacketComponentRepository(standardPacketRepository.getPacketInstance().get(packetType));
    }

}
