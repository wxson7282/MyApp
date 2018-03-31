package com.example.wxson.morseringtone;

/**
 * Created by wxson on 2017/6/18.
 *
 */

@SuppressWarnings("unchecked")
class MorseCodeFactory extends AbstractMorseCodeFactory {
    public <T extends IMorseCode> T createMorseCode(Class<T> c){
        //定义一个生产对象MorseCode
        IMorseCode morseCode = null;
        try {
            morseCode = (T)Class.forName(c.getName()).newInstance();
        } catch(Exception e) {
            System.out.println("Create MorseCode error");
        }
        return (T)morseCode;
    }
}
