package com.pjh.client.connection;

import com.pjh.client.data.ServiceConfigurationData;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

@Slf4j
public class SocketManager {
    final Object sendLock = new Object();
    final Object recvLock = new Object();
    final Object closeLock = new Object();

    private long sendSleepTime = 0;
    private int socketTimeout = 0;
    //final Object submitCheckLock = new Object();

    private InputStream in;
    private OutputStream out;

    @Getter
    @Setter
    private Socket socket;
    private ServiceConfigurationData serviceConfigurationData;

    public SocketManager(ServiceConfigurationData serviceConfigurationData){
        this.serviceConfigurationData = serviceConfigurationData;
    }

    public void connect() throws IOException {
        SocketAddress socketAddress = new InetSocketAddress(serviceConfigurationData.getIpAddress(), serviceConfigurationData.getPort());
        socket = new Socket();
        socket.setKeepAlive(true);
        socket.setSoTimeout(serviceConfigurationData.getSocketTimeout());
        socket.setReceiveBufferSize(serviceConfigurationData.getReceiveBufferSize());
        socket.setSendBufferSize(serviceConfigurationData.getSendBufferSize());
        socket.connect(socketAddress, serviceConfigurationData.getConnectTimeout());
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    public boolean send(byte sendData[]) {
        synchronized(sendLock){
            try {
                int startLen = 0, index=512, len=0, writeLne=0;
                len = sendData.length;

                while(len > 0){
                    if(len < index){
                        writeLne = len;
                    }else{
                        writeLne = index;
                    }
                    out.write(sendData, startLen, writeLne);
                    out.flush();
                    len -= writeLne;
                    startLen += writeLne;
                }
            }catch(IOException e) {
                log.info("Socket Send Fail... \n" + e.getMessage());
                socketClose();
                return false;
            }
            return true;
        }
    }

    public boolean recv(byte[] readData, int readLen) throws Exception {
        synchronized(recvLock){
            int realReadLen = 0;
            int timeOutCnt = 0;
            int tmp = 0;
            try {
                while (readLen > realReadLen) {
                    try {
                        tmp = in.read(readData, realReadLen, (readLen - realReadLen));
                    } catch (SocketTimeoutException e) {

                        log.info("Read timeOut read reqLen:" + readLen + ", realReadLen:" + realReadLen);
                        log.info("Socket timeout setting = " + socket.getSoTimeout() + "ms");
                        log.info(e.toString());
                        return false;
                    }
                    if (tmp <= 0) {
                        return false;
                    } else {
                        realReadLen += tmp;
                    }
                }
                return true;
            }catch (NullPointerException npe){
                return false;
            }
        }
    }

    public boolean socketClose() {
        synchronized(closeLock){
            try {
                if(socket != null){
                    int i=0;
                    while((socket.isOutputShutdown() == false || socket.isInputShutdown() == false) && i++ < 3){
                        if(socket.isInputShutdown() == false)
                            socket.shutdownInput();
                        if(socket.isOutputShutdown() == false)
                            socket.shutdownOutput();
                        Thread.sleep(200);
                    }
                    in.close();
                    out.close();
                    socket.close();
                    socket = null;
                }
            }catch(Exception e) {
                log.info("Socket Close Fail... \n" + e.getMessage());
                return false;
            }
            return true;
        }
    }

}
