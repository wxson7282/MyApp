package com.example.wxson.morseringtone;

/**
 * Created by wxson on 2017/6/18.
 *
 */

abstract class AbstractMorseCodeFactory {
    //createMorseCode的参数必须是IMorseCode的实现类
    public abstract <T extends IMorseCode> T createMorseCode(Class<T> c);
}
