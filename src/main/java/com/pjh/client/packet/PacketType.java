package com.pjh.client.packet;

import java.util.Arrays;
import java.util.List;

public enum PacketType {
    Header(0), Bind(10), BindAck(11), Submit(20), SubmitAck(21), Report(30), ReportAck(31), Link(0), LinkAck(0);

    int packetCategory;

    PacketType(int packetCategory){
        this.packetCategory = packetCategory;
    }

    public int getPacketCategory(){
        return packetCategory;
    }

    public static PacketType findPacketType(int packetType){
        for(PacketType p : PacketType.values()){
            if(p.packetCategory == packetType)
                return p;
        }
        return null;
    }

    List<PacketType> getEnumList(){
        return Arrays.asList(PacketType.values());
    }
}
