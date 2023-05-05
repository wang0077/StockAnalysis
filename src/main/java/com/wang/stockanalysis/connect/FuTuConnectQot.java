package com.wang.stockanalysis.connect;

import com.futu.openapi.FTAPI_Conn;
import com.futu.openapi.FTAPI_Conn_Qot;

import java.util.concurrent.locks.ReentrantLock;

public class FuTuConnectQot {

    private FTAPI_Conn_Qot conn;

    public FuTuConnectQot(FTAPI_Conn_Qot conn){
        this.conn = conn;
    }

    public FTAPI_Conn_Qot getConn(){
        return conn;
    }

    private ReentrantLock lock = new ReentrantLock();

    public boolean lock(){
        return lock.tryLock();
    }

    public void unLock(){
        lock.unlock();
    }


}
