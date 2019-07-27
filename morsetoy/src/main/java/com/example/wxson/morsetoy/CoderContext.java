package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/9.
 *
 */

class CoderContext {
    private ICoder coder = null;
    CoderContext(ICoder _coder){
        this.coder = _coder;
    }
    String code(String _text){
        return this.coder.code(_text);
    }
}
