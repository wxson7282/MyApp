package com.example.wxson.morsetoy;

/**
 * Created by wxson on 2017/8/31.
 *
 */

interface IMakeMorseAudioFile<T> {
    /**
     *  设定播放速度
     * @param _speedFlag 速度标志
     */
    void setSpeed(T _speedFlag);

    /**
     * 根据输入莫尔斯码和播放速度，编辑输出相应的音频文件
     * @param _outputFile 输出音频文件名
     * @param _morseCodes 输入莫尔斯码
     * @return 成功标识
     */
    Boolean makeMorseAudioFile(String _outputFile, String _morseCodes);
}
