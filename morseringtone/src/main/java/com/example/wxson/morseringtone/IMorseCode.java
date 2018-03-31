package com.example.wxson.morseringtone;

/**
 * Created by wxson on 2017/6/17.
 *
 */

interface IMorseCode<T> {
    void setSpeed(T _speedFlag);
    void playDa();
    void playDi();
    void playBlank();
}
