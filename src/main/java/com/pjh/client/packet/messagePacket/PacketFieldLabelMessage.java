package com.pjh.client.packet.messagePacket;

import com.pjh.client.packet.PacketFieldLabel;

public interface PacketFieldLabelMessage {
    enum BindReq implements PacketFieldLabel {
        RESULT;
        @Override
        public String getName() {
            return name();
        }
    }

    enum BindAck implements PacketFieldLabel{
        RESULT;
        @Override
        public String getName() {
            return name();
        }
    }

    enum Submit implements PacketFieldLabel{
        RESULT;
        @Override
        public String getName() {
            return name();
        }
    }

    enum SubmitAck implements PacketFieldLabel{
        RESULT;
        @Override
        public String getName() {
            return name();
        }
    }

    enum Report implements PacketFieldLabel{
        RESULT;
        @Override
        public String getName() {
            return name();
        }
    }

    enum ReportAck implements PacketFieldLabel{
        RESULT;
        @Override
        public String getName() {
            return name();
        }
    }
}
