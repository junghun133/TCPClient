package com.pjh.client.packet;

import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.message.Result;
import com.pjh.client.packet.messagePacket.PacketBuilder;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessage;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportBody extends Message implements Receive {
    @Setter
    String taskId;
    @Setter
    String callStartTime;
    @Setter
    String callConnectTime;
    @Setter
    String callEndTime;
    @Setter
    String callDuration;

    public ReportBody() {
    }

    public ReportBody(Message message) {
        super(message);
    }

    @Override
    public void setData(byte[] receiveData, PacketBuilder packet) throws InvalidPacketException, CloneNotSupportedException {
        if (header == null)
            throw new InvalidPacketException();

        PacketComponentBuildManager packetComponentBuildManager = new PacketComponentBuildManager();
        MessagePacket messagePacket = (MessagePacket) packet;
        //set packet component
        packetComponentBuildManager.setComponentFromStandardPacketRepository(header.getMsgType(), messagePacket);

        int headerSize = packetComponentBuildManager.getPacketComponentHeader().getTotalSize();

        //TODO
        PacketField packetFieldResult = packetComponentBuildManager.getPacketComponentBody().getFieldObject(PacketFieldLabelMessage.Report.RESULT);
        System.arraycopy(receiveData, headerSize, packetFieldResult.content, 0, packetFieldResult.size); headerSize += packetFieldResult.size;

        setResult(Result.parsEnum(packetFieldResult.contentToString()));

        log.info("{} parameters: {}", header.getMsgType().name(), this.toString());
    }
}
