package com.pjh.client.packet;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.SocketManager;
import com.pjh.client.exception.AdditionalPacketException;
import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessage;
import com.pjh.client.packet.messagePacket.PacketFieldLabelMessageHeader;
import com.pjh.client.packet.messagePacket.MessagePacket;
import com.pjh.client.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class PacketComponentTest {
    static PacketComponentBuildManager packetComponentBuildManager;
    static MessagePacket messagePacket;
    static SocketManager socketManagerMock;
    static ServiceConfiguration serviceConfiguration;

    static Header bindHeader;
    static Header submitHeader;
    static Header reportAckHeader;

    @BeforeAll
    public static void setUp() throws Exception {
        messagePacket = new MessagePacket();
        messagePacket.build();
        socketManagerMock = Mockito.mock(SocketManager.class);
        serviceConfiguration = Mockito.mock(ServiceConfiguration.class);
        when(socketManagerMock.recv(null, 0)).thenReturn(true);

        int randomNum = (int) (Math.random() * 10);
        bindHeader = new Header();
        bindHeader.setMsgType(PacketType.Bind);
        bindHeader.setReqTime(DateTimeUtil.getLocalDateTimeNowToString());
        bindHeader.setSessionId(DateTimeUtil.getLocalDateTimeNowToString(Integer.toString(randomNum)));


        randomNum = (int) (Math.random() * 10);
        submitHeader = new Header();
        submitHeader.setMsgType(PacketType.Submit);
        submitHeader.setReqTime(DateTimeUtil.getLocalDateTimeNowToString());
        submitHeader.setSessionId(DateTimeUtil.getLocalDateTimeNowToString(Integer.toString(randomNum)));

        randomNum = (int) (Math.random() * 10);
        reportAckHeader = new Header();
        reportAckHeader.setMsgType(PacketType.ReportAck);
        reportAckHeader.setReqTime(DateTimeUtil.getLocalDateTimeNowToString());
        reportAckHeader.setSessionId(DateTimeUtil.getLocalDateTimeNowToString(Integer.toString(randomNum)));
    }

    @Test
    public void bindPacketBuildTest() throws AdditionalPacketException, CloneNotSupportedException {
        packetComponentBuildManager = new PacketComponentBuildManager();
        packetComponentBuildManager.setComponentFromStandardPacketRepository(PacketType.Bind, messagePacket);
        int totalSize = packetComponentBuildManager.packetComponentHeader.getTotalSize();

        packetComponentBuildManager.packetComponentHeader.setPacketContent(PacketFieldLabelMessageHeader.header.MSG_TYPE, Integer.toString(PacketType.Bind.getPacketCategory()).getBytes());
        byte[] headerResultBytes = packetComponentBuildManager.packetComponentHeader.getData();

        byte[] bodyResultBytes = packetComponentBuildManager.packetComponentBody.getData();

        String header = new String(headerResultBytes);
        String bindBody = new String(bodyResultBytes);

        assertEquals(packetComponentBuildManager.packetComponentHeader.getTotalSize(), header.length());
        assertEquals(packetComponentBuildManager.packetComponentBody.getTotalSize(), bindBody.length());

        packetComponentBuildManager.appendByte();
        assertEquals(packetComponentBuildManager.getSendPacket().length, 90);
    }

    @Test
    public void bindBodyBuildTest() throws InvalidPacketException {
        BindBody bindBody = new BindBody();
        bindBody.setAuthId("123456789");
        bindBody.setCid("pjh");
        bindBody.setHeader(bindHeader);

        byte[] bindPacketData = bindBody.getData(messagePacket);
        System.out.println(new String(bindPacketData));
        assertNotNull(bindPacketData);
    }

    @Test
    public void bindAckBodyConvertTest() throws CloneNotSupportedException, InvalidPacketException {
        BindAckBody bindAckBody = new BindAckBody();
        Header header = new Header();
        header.setMsgType(PacketType.BindAck);
        header.setReqTime(DateTimeUtil.getLocalDateTimeNowToString());
        header.setSessionId("17");
        bindAckBody.setHeader(header);

        byte[] bindAckData = "1017                  2020071514174448    1100 ".getBytes();
        bindAckBody.setData(bindAckData, messagePacket);
        //Message message = bindAckBody.setData(bindAckData);
    }
}