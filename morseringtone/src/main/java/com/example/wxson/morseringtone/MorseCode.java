package com.example.wxson.morseringtone;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by wxson on 2017/6/17.
 *
 */

class MorseCode implements IMorseCode<Boolean> {
    private boolean speedFlag = false;  //默认低速模式
    private SoundPool soundPool;
    private int streamIdSlowDa;      //定义streamId
    private int streamIdSlowDi;      //定义streamId
    private int streamIdFastDa;     //定义streamId
    private int streamIdFastDi;     //定义streamId
    private long delaySlowDa = 300;
    private long delaySlowDi = 100;
    private long delayFastDa = 210;
    private long delayFastDi = 70;

    public MorseCode() {
        // 设置最多可容纳4个音频流，音频的品质为5
        soundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 5);
        // load方法加载指定音频文件，并返回所加载的音频ID。
        Context context = MyApplication.getContextObject();
        streamIdSlowDi = soundPool.load(context, R.raw.morse_di_100ms_600hz , 1);
        streamIdSlowDa = soundPool.load(context, R.raw.morse_da_300ms_600hz , 1);
        streamIdFastDi = soundPool.load(context, R.raw.morse_di_070ms_600hz , 1);
        streamIdFastDa = soundPool.load(context, R.raw.morse_da_210ms_600hz , 1);
    }

    public void finalize(){
        soundPool.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setSpeed(Boolean _speedFlag){
        speedFlag = _speedFlag;
    }

    public void playDa(){
        if (speedFlag){
            soundPool.play(streamIdFastDa,1, 1, 0, 0, 1);
            delay(delayFastDa + delayFastDi);
        }
        else{
            soundPool.play(streamIdSlowDa,1, 1, 0, 0, 1);
            delay(delaySlowDa + delaySlowDi);
        }
    }

    public void playDi(){
        if (speedFlag){
            soundPool.play(streamIdFastDi,1, 1, 0, 0, 1);
            delay(delayFastDi + delayFastDi);
        }
        else{
            soundPool.play(streamIdSlowDi,1, 1, 0, 0, 1);
            delay(delaySlowDi + delaySlowDi);
        }
    }

    public void playBlank(){
        if (speedFlag){
            delay(delayFastDa + delayFastDi);
        }
        else{
            delay(delaySlowDa + delaySlowDi);
        }
    }
    private void delay(long _ms){
        try {
            Thread thread = Thread.currentThread();
            thread.sleep(_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
