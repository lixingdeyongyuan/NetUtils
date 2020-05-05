package com.aixue.netutilslib.log;

public interface INetLogCallBack {

    void debug(String tag,String msg);

    void info(String tag,String msg);

    void error(String tag,String msg);

}
