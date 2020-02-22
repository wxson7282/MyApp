package com.example.wxson.morsetoy;

public class MorseCoder implements ICoder {
    private IArrayMapForCoding arrayMapForCoding;
    //构造函数，注入ArrayMapForCoding

    MorseCoder(IArrayMapForCoding _arrayMapForCoding) {
        arrayMapForCoding = _arrayMapForCoding;
    }

    //获得莫尔斯码
    @Override
    public String code(String _text) {
        StringBuilder stringBuilder = new StringBuilder();
        //把字母、数字和空格翻译成莫尔斯码
        for (char chr : _text.toUpperCase().toCharArray()) {
            if (chr == ' ') {
                stringBuilder.append(" ");
            } else {
                stringBuilder.append(arrayMapForCoding.GetMorseCode(chr));
            }
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
