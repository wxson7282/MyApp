package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/12.
 *
 */

abstract class AbstractMorsePlayFactory {
    //createMorsePlay的参数必须是IMorsePlay的实现类
    public abstract <T extends IMorsePlay> T createMorsePlay(Class<T> c);
}
