package com.example.wxson.morsetoy;

import android.content.Context;

/**
 * Created by wxson on 2017/8/12.
 * 2020/2/18  refactoring
 */
@SuppressWarnings("unchecked")
class MorsePlayerFactory {
    static <T extends IMorsePlayer> T createMorsePlay(Context _context){
        //定义一个生产对象MorseCode
        IMorsePlayer morsePlayer = null;
        try {
            morsePlayer = (T)Class.forName(MorsePlayer.class.getName()).getDeclaredConstructor(Context.class).newInstance(_context);
        } catch(Exception e) {
            System.out.println("Create MorsePlayer error");
        }
        return (T)morsePlayer;
    }
}
