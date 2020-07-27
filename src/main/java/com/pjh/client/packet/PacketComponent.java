package com.pjh.client.packet;

public abstract class PacketComponent {
    public abstract byte[] getData();
    public abstract int getTotalSize();
    public abstract void setPacketContent(PacketFieldLabel packetContent, byte[] data);
    public abstract PacketField getFieldObject(PacketFieldLabel packetFieldLabel);
}