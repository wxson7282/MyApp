package com.example.wxson.morsetoy;

import android.content.res.Resources;
//import java.io.InputStream;

/**
 * Created by wxson on 2017/8/31.
 * 2020/2/22  refactoring
 */

class MorseAudioFileMaker implements IMorseAudioFileMaker<Boolean> {
    private boolean speedFlag = false;  //速度标识 默认低速模式
    private IInputWavStreamCache blankFastStreamCache = new InputWavStreamCache();
    private IInputWavStreamCache longFastStreamCache = new InputWavStreamCache();
    private IInputWavStreamCache shortFastStreamCache = new InputWavStreamCache();
    private IInputWavStreamCache blankSlowStreamCache = new InputWavStreamCache();
    private IInputWavStreamCache longSlowStreamCache = new InputWavStreamCache();
    private IInputWavStreamCache shortSlowStreamCache = new InputWavStreamCache();

    private static final boolean FAST = true;         //快速
    final private IAudioFileMaker makeAudioFile;       //音频文件类

    /**
     * 构造函数 指定外部资源：构成莫尔斯信号的音频单元文件
     */
    MorseAudioFileMaker(Resources _resources) {
        //指定外部资源：构成莫尔斯信号的音频元素文件
        blankFastStreamCache.readStream(_resources.openRawResource(R.raw.morse_blank_050ms_600hz));
        longFastStreamCache.readStream(_resources.openRawResource(R.raw.morse_da_150ms_600hz));
        shortFastStreamCache.readStream(_resources.openRawResource(R.raw.morse_di_050ms_600hz));
        blankSlowStreamCache.readStream(_resources.openRawResource(R.raw.morse_blank_070ms_600hz));
        longSlowStreamCache.readStream(_resources.openRawResource(R.raw.morse_da_210ms_600hz));
        shortSlowStreamCache.readStream(_resources.openRawResource(R.raw.morse_di_070ms_600hz));

        //实例化音频文件类
        makeAudioFile = new AudioFileMaker();
    }

    /**
     *  设定播放速度
     * @param _speedFlag 速度标志
     */
    public void setSpeed(Boolean _speedFlag){
        speedFlag = _speedFlag;
    }

    /**
     * 根据输入莫尔斯码和播放速度，编辑输出相应的音频文件
     *
     * @param _outputFile 输出音频文件名
     * @param _morseCodes 输入莫尔斯码
     * @return 成功标识
     */
    public Boolean makeMorseAudioFile(String _outputFile, String _morseCodes) {
        boolean isOk = true;
        try {
            if (speedFlag == FAST) {
                makeAudioFile.setAudioElement(blankFastStreamCache, longFastStreamCache, shortFastStreamCache);
            } else {
                makeAudioFile.setAudioElement(blankSlowStreamCache, longSlowStreamCache, shortSlowStreamCache);
            }
            makeAudioFile.makeAudioFile(_outputFile, _morseCodes.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    /**
     * @throws java.lang.Throwable error
     */
    protected void finalize() throws java.lang.Throwable {
        super.finalize();
    }
}
