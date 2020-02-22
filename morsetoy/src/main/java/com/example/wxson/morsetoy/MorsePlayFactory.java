package com.example.wxson.morsetoy;

import android.content.Context;

/**
 * Created by wxson on 2017/8/12.
 * 2020/2/18  refactoring
 */
@SuppressWarnings("unchecked")
class MorsePlayFactory {
    static <T extends IMorsePlay> T createMorsePlay(Context _context){
        //定义一个生产对象MorseCode
        IMorsePlay morsePlay = null;
        try {
            morsePlay = (T)Class.forName(MorsePlay.class.getName()).getDeclaredConstructor(Context.class).newInstance(_context);
        } catch(Exception e) {
            System.out.println("Create MorsePlay error");
        }
        return (T)morsePlay;
    }
}
