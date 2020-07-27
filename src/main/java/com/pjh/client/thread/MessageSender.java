package com.pjh.client.thread;

import com.pjh.client.configuration.ServiceConfiguration;
import com.pjh.client.connection.Connection;
import com.pjh.client.data.ServiceConfigurationData;
import com.pjh.client.exception.MessageIndexDuplicatedException;
import com.pjh.client.exception.NoSuchMessageException;
import com.pjh.client.message.Message;
import com.pjh.client.message.MessageEntry;
import com.pjh.client.message.Result;
import com.pjh.client.message.Status;
import com.pjh.client.packet.ReportAckBody;
import com.pjh.client.packet.SubmitBody;
import com.pjh.client.thread.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class MessageSender extends MessageNetworkHandler {

    TimeUtil timeUtil;
    Map<Status, Function<Message, Boolean>> sendFunc;
    Map<Status, Consumer<Message>> updateFunc;

    public MessageSender(ServiceConfiguration config, MessageEntry entry, Connection connection) {
        super(config, entry, connection);
        timeUtil = new TimeUtil();

        sendFunc = new HashMap<>();
        sendFunc.put(Status.Selected, (m) -> connection.sendMessage(new SubmitBody(m)));
        sendFunc.put(Status.Reported, (m) -> connection.sendReportAck(new ReportAckBody(m)));
        updateFunc = new HashMap<>();
        updateFunc.put(Status.Selected, (m) -> {
            m.setStatus(Status.Submitted);
            m.setResult(Result.SUCCESS);
        });
        updateFunc.put(Status.Reported, (m) -> m.setStatus(Status.Complete));
    }

    @Override
    public void run() {
        Stream.of(Status.Selected, Status.Reported).forEachOrdered(
                s -> entry.getStatusList(s).forEach(
                        message -> {
                            boolean sendResult = sendFunc.get(s).apply(message);
                            if (!sendResult) {
                                log.info("send packet error!");
                                return;
                            }
                            timeUtil.updateLastTime();
                            updateFunc.get(s).accept(message);
                            try {
                                entry.updateMessage(message);
                            } catch (NoSuchMessageException | MessageIndexDuplicatedException e) {
                                log.error("Message update fail : " + e.toString());
                            }
                        }
                )
        );

        if (timeUtil.isExpired(((ServiceConfigurationData) config.getServiceData()).getLinkSendIntervalMs())) {

            connection.sendLink();
            timeUtil.updateLastTime();
        }
    }
}
