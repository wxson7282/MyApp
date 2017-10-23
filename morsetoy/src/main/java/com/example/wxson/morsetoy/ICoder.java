package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/2.
 * 莫尔斯编码器接口
 * 使用策略模式
 * 参数：电文
 * 返回值：莫尔斯码
 */

interface ICoder {
    String code(String _text);
}
