package com.example.wxson.morsetoy;

import java.io.InputStream;

/**
 * Created by wxson on 2017/8/31.
 *
 */

interface IMakeAudioFile {
    /**
     * a.读入空白音频流、长音频流、短音频流，保存到缓存区
     * b.解析输入莫尔斯码，根据莫尔斯码用缓存区的音频流组装输出音频文件
     * @param _outputFile 输出音频文件
     * @param _morseCodes 输入莫尔斯码 由空格、点、划组成
     * @param _streamBlank 空白音频流
     * @param _streamDa    长音频流
     * @param _streamDi    短音频流
     * @return 成功标识
     */
    boolean mergeAudioFiles(String _outputFile, char[] _morseCodes, InputStream _streamBlank, InputStream _streamDa, InputStream _streamDi);
}
