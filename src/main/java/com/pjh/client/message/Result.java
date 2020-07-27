package com.pjh.client.message;

public enum Result implements ValueEnum {
    SUCCESS(100),
    ETC(306);

    private final int result;

    Result(int result) {
        this.result = result;
    }

    public static Result parsEnum(int result) {
        for (Result r : Result.values()) {
            if (r.getValue() == result)
                return r;
        }
        return null;
    }

    public static Result parsEnum(String result) {
        for (Result r : Result.values()) {
            if (r.getValue() == Integer.parseInt(result))
                return r;
        }
        return null;
    }

    @Override
    public int getValue() {
        return result;
    }

    @Override
    public String getStringValue() {
        return Integer.toString(result);
    }

    @Override
    public ValueEnum getDefaultValue() {
        return ETC;
    }
}
