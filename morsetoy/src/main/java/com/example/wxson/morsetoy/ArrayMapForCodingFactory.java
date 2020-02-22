package com.example.wxson.morsetoy;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by wxson on 2017/8/5.
 * 2020/2/18  refactoring
 */

class ArrayMapForCodingFactory {
    private static String[] charArray;         //字符数组 包含字母、数字、符号
    private static String[] morseCodeArray;   //字符数组对应的莫尔斯码数组
    private static String[] figuresArray;     //数字数组
    private static String[] morseLongCodeArray;    //数字数组对应的莫尔斯长码数组
    private static String[] morseShortCodeArray;    //数字数组对应的莫尔斯短码数组

    //构造函数
    ArrayMapForCodingFactory(Resources _resources){
        //获取字符数组和莫尔斯码数组
        charArray = _resources.getStringArray(R.array.characters);
        morseCodeArray = _resources.getStringArray(R.array.morse_code);
        figuresArray = _resources.getStringArray(R.array.figures);
        morseLongCodeArray = _resources.getStringArray(R.array.morse_long_code);
        morseShortCodeArray = _resources.getStringArray(R.array.morse_short_code);
    }

    //生产标准编码类的工厂方法
    IArrayMapForCoding createArrayMapCodingNormal(){
        return new ArrayMapForCoding(charArray, morseCodeArray);
    }

    //生产长码电报编码类的工厂方法
    IArrayMapForCoding createArrayMapCodingLong(){
        return new ArrayMapForCoding(figuresArray, morseLongCodeArray);
    }

    //生产短码电报编码类的工厂方法
    IArrayMapForCoding createArrayMapCodingShort(){
        return new ArrayMapForCoding(figuresArray, morseShortCodeArray);
    }
}
