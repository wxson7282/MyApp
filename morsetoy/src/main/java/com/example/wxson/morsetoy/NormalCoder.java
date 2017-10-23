package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/3.
 * 标准莫尔斯编码器
 * 参数：电文
 * 返回值：莫尔斯码
 * 说明：电文中有字母、数字和空格，数字用长码表示
 */

class NormalCoder implements ICoder {
    //获得莫尔斯码
    public String code(String _text) {
        String returnValue = "";
        //把字母、数字和空格翻译成莫尔斯码 数字用长码表示
        for (char chr : _text.toUpperCase().toCharArray()){
            if (chr == ' '){
                returnValue += " ";
            }
            else {
                returnValue += MainActivity.arrayMapForCodingNormal.GetMorseCode(chr);
            }
            returnValue += " ";
        }
        return returnValue;
    }
}

