package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/12.
 * 2020/2/18 refactoring
 */
@SuppressWarnings("unchecked")
class MorsePlayFactory {
    static <T extends IMorsePlay> T createMorsePlay(){
        //定义一个生产对象MorseCode
        IMorsePlay morsePlay = null;
        try {
            morsePlay = (T)Class.forName(MorsePlay.class.getName()).newInstance();
        } catch(Exception e) {
            System.out.println("Create MorseCode error");
        }
        return (T)morsePlay;
    }
}
