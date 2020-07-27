package com.pjh.client.application;

import lombok.Data;

@Data
public class AppTestResult {
    private ResultCode result;
    private String causeServiceName;

    public enum ResultCode {ConnectFail, BindFail, DBConnectFail, Success}
}
