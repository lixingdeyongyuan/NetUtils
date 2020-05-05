package com.aixue.netutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aixue.netutilslib.NetUtlisBuilder
import com.aixue.netutilslib.http.HttpMgr
import com.aixue.netutilslib.http.api.CommonApi
import com.aixue.netutilslib.log.INetLogCallBack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetUtlisBuilder.instance().initLog(object : INetLogCallBack {
            override fun debug(tag: String?, msg: String?) {
                Log.d(tag, msg)
            }

            override fun info(tag: String?, msg: String?) {
            }

            override fun error(tag: String?, msg: String?) {
            }

        }).initHttpMgr("/sdcard/", "http://test", true)
        val param = HashMap<String, String>()
        param.put("type", "lsjz")
        param.put("code", "164809")
        param.put("page", "1")
        param.put("per", "100")
        HttpMgr.instance().getApi(CommonApi::class.java)
            .get("http://test", param)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                Log.d("rlog", it.string())
            },{

            })


    }
}
