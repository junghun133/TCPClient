package com.pjh.client.connection;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.message.Message;
import com.pjh.client.message.Result;
import com.pjh.client.packet.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Random;

@Slf4j
public class DummyConnection implements Connection {
    int minRandom = 2000;
    int maxRandom = 5000;
    Random r = new Random(LocalTime.now().getNano());
    Message message = null;
    Header packetType = null;

    private void sleepRandom() {
        try {
            Thread.sleep(r.nextInt(maxRandom - minRandom) + minRandom);
        } catch (InterruptedException e) {
            log.error("dummy connection sleep error!");
        }
    }

    @Override
    public void init(ServiceConfiguration serviceConfiguration) {
        log.info("INIT");
    }

    @Override
    public boolean connect() {
        log.info("CONNECT");
        return true;
    }

    @Override
    public void disconnect() {

        log.info("DISCONNECT");
    }

    @Override
    public boolean bind(BindBody bindBody) {
        log.info("BIND : " + bindBody);
        return true;
    }

    @Override
    public void sendLink() {
        log.info("LINK SEND");
    }

    @Override
    public boolean sendMessage(SubmitBody message) {
        log.info("SUBMIT : " + message);
        this.message = message;
        this.packetType = new Header();
        this.packetType.setMsgType(PacketType.Submit);
        return true;
    }

    @Override
    public boolean sendReportAck(ReportAckBody reportAck) {
        log.info("REPORT_ACK : " + reportAck);
        message = null;
        packetType = null;
        return true;
    }

    @Override
    public SubmitAckBody receiveSubmitAckBody(Header header) {
        if (message == null)
            return null;

        sleepRandom();

        SubmitAckBody ack = new SubmitAckBody(message);
        ack.setMessageKey(message.getMessageKey());
        ack.setResult(Result.SUCCESS);
        log.info("SUBMIT_ACK : " + ack);
        this.packetType.setMsgType(PacketType.SubmitAck);
        return ack;
    }

    @Override
    public ReportBody receiveReportBody(Header header) {
        if (message == null)
            return null;

        sleepRandom();

        ReportBody report = new ReportBody(message);
        report.setResult(Result.SUCCESS);
        log.info("REPORT : " + report);
        packetType.setMsgType(PacketType.ReportAck);
        return report;

    }

    @Override
    public BindAckBody receiveBindAckBody(Header header) {
        return null;
    }

    @Override
    public Header receiveHeader() {
        if (packetType == null)
            return null;

        switch (packetType.getMsgType()) {
            case Submit:
                packetType.setMsgType(PacketType.SubmitAck);
                break;
            case SubmitAck:
                packetType.setMsgType(PacketType.Report);
                break;
        }
        return packetType;
    }
}
