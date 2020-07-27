package com.pjh.client.connection;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.packet.*;

public interface Connection {
    void init(ServiceConfiguration serviceConfiguration);

    boolean connect();

    void disconnect();

    boolean bind(BindBody bindBody);

    void sendLink();

    boolean sendMessage(SubmitBody message);

    boolean sendReportAck(ReportAckBody reportAck);

    SubmitAckBody receiveSubmitAckBody(Header header);

    ReportBody receiveReportBody(Header header);

    BindAckBody receiveBindAckBody(Header header);

    Header receiveHeader();
}
