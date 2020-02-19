package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/2.
 * 电报用莫尔斯编码器
 * 参数：电文
 * 返回值：莫尔斯码
 * 说明：电文中只有数字和空格，可以得到长码或短码
 */

class TelegramCoder implements ICoder {
    //数字长码标识
    private final boolean IsLongCode;

    //构造函数 指定长短码
    TelegramCoder(boolean _isLongCode){ IsLongCode = _isLongCode; }

    //获得莫尔斯码
    //把数字和空格翻译成莫尔斯码 数字表示取决于长码标识IsLongCode
    public String code(String _text){
        String returnValue = "";
        for (char chr : _text.toUpperCase().toCharArray()){
            if (chr == ' '){
                returnValue += " ";
            }
            else {
                if (IsLongCode){
                    returnValue += MainActivity.arrayMapForCodingLong.GetMorseCode(chr);
                }
                else {
                    returnValue += MainActivity.arrayMapForCodingShort.GetMorseCode(chr);
                }
            }
            returnValue += " ";
        }
        return returnValue;
    }
}
