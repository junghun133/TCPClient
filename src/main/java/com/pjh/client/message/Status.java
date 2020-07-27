package com.pjh.client.message;

public enum Status implements ValueEnum {
    Initial(0), Selected(1), Submitted(2), Reported(3), Complete(4);

    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(status);
    }

    @Override
    public ValueEnum getDefaultValue() {
        return Complete;
    }
}
