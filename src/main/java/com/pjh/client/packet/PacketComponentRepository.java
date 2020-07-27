package com.pjh.client.packet;

import java.util.List;

public class PacketComponentRepository extends PacketComponent{
    private List<PacketField> packetFields;

    public PacketComponentRepository(List<PacketField> packetFields){
        this.packetFields = packetFields;
    }

    @Override
    public byte[] getData() {
        if(packetFields.isEmpty())
            return new byte[0];

        byte[] byteBuffer = new byte[getTotalSize()];
        int index = 0;
        for(PacketField p : packetFields){
            System.arraycopy(p.content, 0, byteBuffer, index , p.content.length);
            if(index == 0){ index = p.getSize(); continue;}
            index += p.getSize();
        }
        return byteBuffer;
    }

    @Override
    public void setPacketContent(PacketFieldLabel packetContent, byte[] data) {
        if(data == null || packetContent == null)
            return;

        for(PacketField p : packetFields){
            if(p.fieldNameEquals(packetContent)){
                p.setContent(data);
                return;
            }
        }
    }

    @Override
    public PacketField getFieldObject(PacketFieldLabel packetFieldLabel) {
        for(PacketField p : packetFields){
            if(p.fieldNameEquals(packetFieldLabel))
                return p;
        }
        return null;
    }

    @Override
    public int getTotalSize() {
        int totalSize = 0;
        for(PacketField p : packetFields){
            if(!p.isVariableLength()){
                totalSize += p.getSize();
            }else{
                totalSize += p.getContent().length;
            }
        }
        return totalSize;
    }
}
