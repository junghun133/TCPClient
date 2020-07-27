package com.pjh.client.packet.messagePacket;

import com.pjh.client.packet.PacketField;
import com.pjh.client.packet.PacketType;
import com.pjh.client.packet.StandardPacketRepository;

import java.util.List;

public abstract class PacketBuilder {

    abstract List<PacketField> getPacketField(PacketType packetType);
    abstract List<PacketField> getPacketHeader();
    public abstract int getHeaderSize();
    public abstract void build();
    public abstract StandardPacketRepository getStandardPacketRepository();

    StandardPacketRepository init() {
        StandardPacketRepository standardPacketRepository = new StandardPacketRepository();
        standardPacketRepository.packetInstance.put(PacketType.Header, getPacketHeader());
        for(PacketType p : PacketType.values()){
            if(p == PacketType.Header)
                continue;

            standardPacketRepository.packetInstance.put(p, getPacketField(p));
        }
        return standardPacketRepository;
    }
}
