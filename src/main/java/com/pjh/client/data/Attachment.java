package com.pjh.client.data;

import lombok.Data;

import java.io.File;

@Data
public class Attachment {
    File file;
    String extension;
    int size;

    public String getSizeToString(){
        return Integer.toString(size);
    }
}
