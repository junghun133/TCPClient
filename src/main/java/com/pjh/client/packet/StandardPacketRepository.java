package com.pjh.client.packet;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StandardPacketRepository implements Cloneable{
    public final Map<PacketType, List<PacketField>> packetInstance = new HashMap<>();

    @Override
    public StandardPacketRepository clone() throws CloneNotSupportedException {
        return (StandardPacketRepository) super.clone();
    }
}