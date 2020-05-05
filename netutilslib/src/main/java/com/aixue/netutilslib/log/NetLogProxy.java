package com.aixue.netutilslib.log;

/**
 *网络日志代理类
 */
public class NetLogProxy implements INetLogCallBack {

    private INetLogCallBack mINetLogCallBack = null;

    @Override
    public void debug(String tag, String msg) {
        if (mINetLogCallBack == null) {
            return;
        }
        mINetLogCallBack.debug(tag, msg);
    }

    @Override
    public void info(String tag, String msg) {
        if (mINetLogCallBack == null) {
            return;
        }
        mINetLogCallBack.debug(tag, msg);
    }

    @Override
    public void error(String tag, String msg) {
        if (mINetLogCallBack == null) {
            return;
        }
        mINetLogCallBack.debug(tag, msg);
    }

    public static class SingletonHolder {
        private static NetLogProxy sNetLogProxy = new NetLogProxy();
    }

    public static NetLogProxy getInstance() {
        return SingletonHolder.sNetLogProxy;
    }


    public void setINetLogCallBack(INetLogCallBack iNetLogCallBack) {
        mINetLogCallBack = iNetLogCallBack;
    }

}
