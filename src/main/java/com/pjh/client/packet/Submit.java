package com.pjh.client.packet;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.packet.messagePacket.PacketBuilder;

public interface Submit {
    byte[] getData(PacketBuilder packet) throws InvalidPacketException;
}
