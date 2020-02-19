package com.example.wxson.morsetoy;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by wxson on 2017/8/23.
 *
 */

@SuppressWarnings("deprecation")
class MorsePlay implements IMorsePlay<Boolean> {
    private boolean speedFlag = false;  //默认低速模式
    private final SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 5);
    private final int streamIdSlowDa;      //定义streamId
    private final int streamIdSlowDi;      //定义streamId
    private final int streamIdFastDa;     //定义streamId
    private final int streamIdFastDi;     //定义streamId
    private final long delaySlowDa = 210;
    private final long delaySlowDi = 70;
    private final long delayFastDa = 150;
    private final long delayFastDi = 50;

    public MorsePlay() {
        // 设置最多可容纳4个音频流，音频的品质为5
        // load方法加载指定音频文件，并返回所加载的音频ID。
        Context context = MyApplication.getContextObject();
        streamIdSlowDi = soundPool.load(context, R.raw.morse_di_070ms_600hz , 1);
        streamIdSlowDa = soundPool.load(context, R.raw.morse_da_210ms_600hz , 1);
        streamIdFastDi = soundPool.load(context, R.raw.morse_di_050ms_600hz , 1);
        streamIdFastDa = soundPool.load(context, R.raw.morse_da_150ms_600hz , 1);
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
            soundPool.play(streamIdFastDa,1.0f, 1.0f, 0, 0, 1.0f);
            delay(delayFastDa + delayFastDi);
        }
        else{
            soundPool.play(streamIdSlowDa,1.0f, 1.0f, 0, 0, 1.0f);
            delay(delaySlowDa + delaySlowDi);
        }
    }

    public void playDi(){
        if (speedFlag){
            soundPool.play(streamIdFastDi,1.0f, 1.0f, 0, 0, 1.0f);
            delay(delayFastDi + delayFastDi);
        }
        else{
            soundPool.play(streamIdSlowDi,1.0f, 1.0f, 0, 0, 1.0f);
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
            Thread.sleep(_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
