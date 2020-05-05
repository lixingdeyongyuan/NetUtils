package com.aixue.netutilslib;

import com.aixue.netutilslib.http.HttpMgr;
import com.aixue.netutilslib.log.INetLogCallBack;
import com.aixue.netutilslib.log.NetLogProxy;

public class NetUtlisBuilder {

    private static class SingletonHolder {
        private static final NetUtlisBuilder INSTANCE = new NetUtlisBuilder();
    }

    public static NetUtlisBuilder instance() {
        return NetUtlisBuilder.SingletonHolder.INSTANCE;
    }


    public NetUtlisBuilder initLog(INetLogCallBack iNetLogCallBack) {
        NetLogProxy.getInstance().setINetLogCallBack(iNetLogCallBack);
        return this;
    }

    public NetUtlisBuilder initHttpMgr(String cacheDirPath, String baseUrl, boolean isPrintHttpLog){
        HttpMgr.init(cacheDirPath, baseUrl, isPrintHttpLog);
        return this;
    }


}
