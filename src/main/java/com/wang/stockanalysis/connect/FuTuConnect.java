package com.wang.stockanalysis.connect;

import com.futu.openapi.*;
import com.futu.openapi.pb.QotCommon;
import com.futu.openapi.pb.QotSub;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class FuTuConnect implements FTSPI_Qot, FTSPI_Conn{

    private static final String host = "127.0.0.1";
    private static final int port = 11111;

    private FTAPI_Conn_Qot qot = new FTAPI_Conn_Qot();


    public FuTuConnect(int Number){
        qot.setClientInfo("java-Client-" + Number,1);
        qot.setConnSpi(this);
        qot.setQotSpi(this);
    }

    public FTAPI_Conn_Qot getQot(){
        return qot;
    }

    public void start(){
        qot.initConnect(host,port,false);
    }



    @Override
    public void onInitConnect(FTAPI_Conn client, long errCode, String desc) {
        System.out.printf("Qot onInitConnect: ret=%b desc=%s connID=%d\n", errCode, desc, client.getConnectID());
        ConnectManger.initConnectSetPool(new FuTuConnectQot(qot));
    }

    @Override
    public void onDisconnect(FTAPI_Conn client, long errCode) {
        System.out.printf("Qot onDisConnect: %d\n", errCode);
    }

    @Override
    public void onReply_Sub(FTAPI_Conn client, int nSerialNo, QotSub.Response rsp) {
        System.out.println("订阅回调");
        System.out.println(rsp);

        FTSPI_Qot.super.onReply_Sub(client, nSerialNo, rsp);
    }

    public static void main(String[] args) throws IOException {
        FTAPI.init();
        FuTuConnect qot = new FuTuConnect(2);
        qot.start();

        while (true) {
            try {
                Thread.sleep(1000 * 600);
            } catch (InterruptedException exc) {

            }
        }
    }
}

