package com.pjh.client.packet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacketField{
    PacketFieldLabel packetFieldLabel;
    int size;
    byte[] content;
    boolean variableLength;

    boolean fieldNameEquals(PacketFieldLabel packetFieldLabel){
        return this.packetFieldLabel == packetFieldLabel;
    }

    public String contentToString(){ return new String(content);}
}