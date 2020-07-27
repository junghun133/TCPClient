package com.pjh.client.packet;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.packet.messagePacket.PacketBuilder;

public interface Receive {
    void setData(byte[] receiveData, PacketBuilder packet) throws InvalidPacketException, CloneNotSupportedException;
}
