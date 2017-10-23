package com.example.wxson.morsetoy;

import android.content.Context;

/**
 * Created by wxson on 2017/8/5.
 *
 */

class ArrayMapForCodingFactory {
    private static String[] charArray;         //字符数组 包含字母、数字、符号
    private static String[] morseCodeArray;   //字符数组对应的莫尔斯码数组
    private static String[] figuresArray;     //数字数组
    private static String[] morseLongCodeArray;    //数字数组对应的莫尔斯长码数组
    private static String[] morseShortCodeArray;    //数字数组对应的莫尔斯短码数组

    //构造函数
    ArrayMapForCodingFactory(){
        //获取字符数组和莫尔斯码数组
        Context context = MyApplication.getContextObject();
        charArray = context.getResources().getStringArray(R.array.characters);
        morseCodeArray = context.getResources().getStringArray(R.array.morse_code);
        figuresArray = context.getResources().getStringArray(R.array.figures);
        morseLongCodeArray = context.getResources().getStringArray(R.array.morse_long_code);
        morseShortCodeArray = context.getResources().getStringArray(R.array.morse_short_code);
    }

    //生产标准编码类的工厂方法
    IArrayMapForCoding createArrayMapCodingNormal(){
        //定义一个工厂生产的ArrayMapForCoding类
        IArrayMapForCoding arrayMapForCoding = null;
        try {
            //生产ArrayMapForCoding类
            arrayMapForCoding = new ArrayMapForCoding(charArray, morseCodeArray);
        } catch (Exception e){
            System.out.println("ArrayMapCodingNormal类生成错误！");
        }
        return arrayMapForCoding;
    }

    //生产长码电报编码类的工厂方法
    IArrayMapForCoding createArrayMapCodingLong(){
        //定义一个工厂生产的ArrayMapForCoding类
        IArrayMapForCoding arrayMapForCoding = null;
        try {
            //生产ArrayMapForCoding类
            arrayMapForCoding = new ArrayMapForCoding(figuresArray, morseLongCodeArray);
        } catch (Exception e){
            System.out.println("ArrayMapCodingLong类生成错误！");
        }
        return arrayMapForCoding;
    }

    //生产短码电报编码类的工厂方法
    IArrayMapForCoding createArrayMapCodingShort(){
        //定义一个工厂生产的ArrayMapForCoding类
        IArrayMapForCoding arrayMapForCoding = null;
        try {
            //生产ArrayMapForCoding类
            arrayMapForCoding = new ArrayMapForCoding(figuresArray, morseShortCodeArray);
        } catch (Exception e){
            System.out.println("ArrayMapCodingShort类生成错误！");
        }
        return arrayMapForCoding;
    }
}
