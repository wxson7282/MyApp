package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/9.
 * 2020/2/19  refactoring
 */

class CoderContext {
    private ICoder coder;
    CoderContext(ICoder _coder){
        this.coder = _coder;
    }
    String code(String _text){
        return this.coder.code(_text);
    }
}
