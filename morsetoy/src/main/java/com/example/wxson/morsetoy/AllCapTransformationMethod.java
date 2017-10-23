package com.example.wxson.morsetoy;

import android.text.method.ReplacementTransformationMethod;

/**
 * Created by wxson on 2017/8/22.
 *
 */

class AllCapTransformationMethod extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] aa;
        aa = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        return aa;
    }

    @Override
    protected char[] getReplacement() {
        char[] cc;
        cc = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        return cc;
    }
}
