package com.example.wxson.morsetoy;

import android.app.Application;
import android.content.Context;

/**
 * Created by wxson on 2017/6/22.
 *
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();
    }

    //返回
    public static Context getContextObject(){
        return context;
    }
}
