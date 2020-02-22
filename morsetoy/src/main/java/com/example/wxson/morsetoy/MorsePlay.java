package com.example.wxson.morsetoy;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by wxson on 2017/8/23.
 * 2020/2/20  refactoring
 */

class MorsePlay implements IMorsePlay<Boolean> {
    private boolean isFast = false;  //默认低速模式
    private final SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 5);
    private final int streamIdSlowDa;      //定义streamId
    private final int streamIdSlowDi;      //定义streamId
    private final int streamIdFastDa;     //定义streamId
    private final int streamIdFastDi;     //定义streamId
    private static final long delaySlowDa = 210;
    private static final long delaySlowDi = 70;
    private static final long delayFastDa = 150;
    private static final long delayFastDi = 50;

    public MorsePlay(Context _context) {
        // 设置最多可容纳4个音频流，音频的品质为5
        // load方法加载指定音频文件，并返回所加载的音频ID。
        streamIdSlowDi = soundPool.load(_context, R.raw.morse_di_070ms_600hz , 1);
        streamIdSlowDa = soundPool.load(_context, R.raw.morse_da_210ms_600hz , 1);
        streamIdFastDi = soundPool.load(_context, R.raw.morse_di_050ms_600hz , 1);
        streamIdFastDa = soundPool.load(_context, R.raw.morse_da_150ms_600hz , 1);
    }

    public void finalize(){
        soundPool.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setSpeed(Boolean _isFast){
        isFast = _isFast;
    }

    public void playDa(){
        soundPool.play(isFast ? streamIdFastDa : streamIdSlowDa,1.0f, 1.0f, 0, 0, 1.0f);
        delay(isFast ? delayFastDa + delayFastDi : delaySlowDa + delaySlowDi);
    }

    public void playDi(){
        soundPool.play(isFast ? streamIdFastDi : streamIdSlowDi,1.0f, 1.0f, 0, 0, 1.0f);
        delay(isFast ? delayFastDi + delayFastDi : delaySlowDi + delaySlowDi);
    }

    public void playBlank(){
        delay(isFast ? delayFastDa + delayFastDi : delaySlowDa + delaySlowDi);
    }
    private void delay(long _ms){
        try {
            Thread.sleep(_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
