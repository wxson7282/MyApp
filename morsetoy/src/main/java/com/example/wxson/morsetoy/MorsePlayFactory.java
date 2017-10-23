package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/12.
 *
 */
@SuppressWarnings("unchecked")
class MorsePlayFactory extends AbstractMorsePlayFactory {
    public <T extends IMorsePlay> T createMorsePlay(Class<T> c){
        //定义一个生产对象MorseCode
        IMorsePlay morsePlay = null;
        try {
            morsePlay = (T)Class.forName(c.getName()).newInstance();
        } catch(Exception e) {
            System.out.println("Create MorseCode error");
        }
        return (T)morsePlay;
    }
}
