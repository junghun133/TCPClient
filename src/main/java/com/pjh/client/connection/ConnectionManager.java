package com.pjh.client.connection;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.exception.InvalidPacketException;
import com.pjh.client.message.Message;
import com.pjh.client.packet.*;
import com.pjh.client.packet.messagePacket.MessagePacket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ConnectionManager implements Connection {
    @Getter
    @Setter
    private SocketManager socketManager;
    private MessagePacket packet;

    @Override
    public void init(ServiceConfiguration serviceConfiguration) {
        packet = new MessagePacket();
        packet.build();
        socketManager = new SocketManager((ServiceConfigurationData) serviceConfiguration.getServiceData());
    }

    @Override
    public boolean connect() {
        try {
            socketManager.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void disconnect() {
        socketManager.socketClose();
    }

    @Override
    public boolean bind(BindBody bindBody) {
        try {
            return socketManager.send(bindBody.getData(packet));
        } catch (InvalidPacketException e) {
            log.info("Send fail! " + e.getMessage());
            return false;
        }
    }

    @Override
    public void sendLink() {
    }

    @Override
    public boolean sendMessage(SubmitBody submitBody) {
        try {
            return socketManager.send(submitBody.getData(packet));
        } catch (InvalidPacketException e) {
            log.info("Send fail! " + e.getMessage());
            return false;
        }
    }

    @Override
    public SubmitAckBody receiveSubmitAckBody(Header header) {
        return (SubmitAckBody) receivePacket(header, new SubmitAckBody());
    }

    @Override
    public ReportBody receiveReportBody(Header header) {
        return (ReportBody) receivePacket(header, new ReportBody());
    }

    @Override
    public BindAckBody receiveBindAckBody(Header header) {
        return (BindAckBody) receivePacket(header, new BindAckBody());
    }

    @Override
    public Header receiveHeader() {
        //StandardPacketRepository standardPacketRepository = packet.get;
        Header header = new Header();
        header.setPacket(packet);

        byte[] headerByteArray = new byte[packet.getHeaderSize()];
        try {
            if(socketManager.recv(headerByteArray, packet.getHeaderSize())){
                header.setData(headerByteArray);
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }

    @Override
    public boolean sendReportAck(ReportAckBody reportAck) {
        try {
            return socketManager.send(reportAck.getData(packet));
        } catch (InvalidPacketException e) {
            log.info("Send fail! " + e.getMessage());
            return false;
        }
    }

    private Message receivePacket(Header header, Message message){
        message.setHeader(header);
        int bodyLen = Integer.parseInt(header.getBodyLen());
        byte[] targetBody = new byte[bodyLen];
        try{
            if(socketManager.recv(targetBody,bodyLen )){
                message.setData(targetBody, packet);
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
