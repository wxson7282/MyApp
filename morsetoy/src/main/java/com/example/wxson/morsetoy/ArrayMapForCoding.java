package com.example.wxson.morsetoy;

import android.support.v4.util.ArrayMap;

/**
 * Created by wxson on 2017/8/17.
 * 莫尔斯译码用ArrayMap类
 */

class ArrayMapForCoding implements IArrayMapForCoding{
    private final ArrayMap<String, String> codingArrayMap;   //编码用ArrayMap
    private final String[] charArray;         //字符数组
    private final String[] morseCodeArray;   //莫尔斯码数组

    //构造函数 参数1：字符数组  参数2：莫尔斯码数组
    ArrayMapForCoding(String[] _charArray, String[] _morseCodeArray) {
        codingArrayMap = new ArrayMap<>();
        charArray = _charArray;
        morseCodeArray = _morseCodeArray;
        //字符数组和莫尔斯码数组的成员数必须相等
        if (charArray.length != morseCodeArray.length){
            //输出异常信息
            System.out.println("codingArrayMap error: charArray.length != morseCodeArray.length");
        }
        else{
            //全体item加入到arrayMap
            AllItemAdd();
        }
    }

    //将字符翻译成莫尔斯电码 如果字符不存在，返回*
    public String GetMorseCode(char _char) {
        String chr = String.valueOf(_char);
        if (codingArrayMap.containsKey(chr)){
            return codingArrayMap.get(chr);
        }
        else{
            return "*";
        }
    }

    //全体item加入到arrayMap
    private void AllItemAdd() {
        for (int i = 0; i < charArray.length; i++){
            codingArrayMap.put(charArray[i], morseCodeArray[i]);
        }
    }

}
