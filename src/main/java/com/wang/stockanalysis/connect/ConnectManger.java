package com.wang.stockanalysis.connect;

import com.futu.openapi.FTAPI_Conn;
import com.futu.openapi.FTAPI_Conn_Qot;
import com.futu.openapi.pb.QotCommon;
import com.futu.openapi.pb.QotSub;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ConnectManger {

    private static final Map<String, FuTuConnectQot> pool= new HashMap<>();

    public boolean createPool(int size){

        for (int i = 0 ;i < size;i++){
            FuTuConnect fuTuConnect = new FuTuConnect(i);
            fuTuConnect.start();
        }
        return true;
    }

    public static void initConnectSetPool(FuTuConnectQot qot){
        System.out.println("回调");
        FTAPI_Conn conn = qot.getConn();
        long connectID = conn.getConnectID();
        pool.put(String.valueOf(connectID),qot);
    }

    public FTAPI_Conn_Qot getConnect(){

        while (true){
            for (FuTuConnectQot qot : pool.values()){
                if(qot.lock()){
                    return qot.getConn();
                }
            }
         }
    }

    public static void main(String[] args) throws InterruptedException {
        ConnectManger connectManger = new ConnectManger();
        connectManger.createPool(3);
        Thread.sleep(1000);
        FTAPI_Conn_Qot connect = connectManger.getConnect();
        QotCommon.Security build = QotCommon.Security.newBuilder().setMarket(QotCommon.QotMarket.QotMarket_CNSH_Security_VALUE).setCode("600760").build();
        QotSub.C2S.Builder s2c = QotSub.C2S.newBuilder().addSecurityList(build).addSubTypeList(QotCommon.SubType.SubType_Basic_VALUE).setIsSubOrUnSub(true);
        QotSub.Request request = QotSub.Request.newBuilder().setC2S(s2c).build();
        int sub = connect.sub(request);
        System.out.println(sub);
        while (true){

        }
    }

}
