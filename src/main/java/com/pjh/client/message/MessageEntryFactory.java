package com.pjh.client.message;

public class MessageEntryFactory {
    public static MessageEntry getMessageEntry(MessageEntry.EntryType messageEntryType) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (messageEntryType) {
            case Map:
                return new MessageEntryMap();
        }

        throw new IllegalArgumentException();
    }
}
