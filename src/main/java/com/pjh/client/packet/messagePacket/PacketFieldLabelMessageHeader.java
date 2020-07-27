package com.pjh.client.packet.messagePacket;

import com.pjh.client.packet.PacketFieldLabel;

public interface PacketFieldLabelMessageHeader {
    enum header implements PacketFieldLabel {
        MSG_TYPE;

        @Override
        public String getName() {
            return name();
        }
    }
}
