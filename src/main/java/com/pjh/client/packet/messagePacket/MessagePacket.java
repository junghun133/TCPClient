package com.pjh.client.packet.messagePacket;

import com.pjh.client.packet.PacketField;
import com.pjh.client.packet.PacketType;
import com.pjh.client.packet.StandardPacketRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessagePacket extends PacketBuilder {
    @Getter
    StandardPacketRepository standardPacketRepository;

    @Override
    public void build(){
        standardPacketRepository = init();
    }

    List<PacketField> getPacketField(PacketType packetType) {
        List<PacketField> packetFields = new ArrayList<>();
        switch (packetType){
            case Bind:
                break;
            case BindAck:
                break;
            case Submit:
                break;
            case SubmitAck:
                break;
            case Report:
                break;
            case ReportAck:
                break;
        }
        return packetFields;
    }

    //header
    List<PacketField> getPacketHeader() {
        List<PacketField> packetFields = new ArrayList<>();

        return packetFields;
    }

    @Override
    public int getHeaderSize() {
        int headerSize = 0;
        for(PacketField p : standardPacketRepository.getPacketInstance().get(PacketType.Header)){
            headerSize += p.getSize();
        }
        return headerSize;
    }
}
