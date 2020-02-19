package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/12.
 *
 */

interface IMorsePlay<T> {
    void setSpeed(T _speedFlag);
    void playDa();
    void playDi();
    void playBlank();
}
