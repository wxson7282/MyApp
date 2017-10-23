package com.example.wxson.morsetoy;

import android.content.Context;
import android.content.res.Resources;
import java.io.InputStream;

/**
 * Created by wxson on 2017/8/31.
 *
 */

class MakeMorseAudioFile implements IMakeMorseAudioFile<Boolean> {
    private boolean speedFlag = false;  //速度标识 默认低速模式
    final private InputStream streamFastBlank;        //空音频文件
    final private InputStream streamFastDa;           //长音音频文件
    final private InputStream streamFastDi;           //短音音频文件
    final private InputStream streamSlowBlank;        //空音频文件
    final private InputStream streamSlowDa;           //长音音频文件
    final private InputStream streamSlowDi;           //短音音频文件
    private static final boolean FAST = true;         //快速
    final private IMakeAudioFile makeAudioFile;       //音频文件类

    /**
     * 构造函数 指定外部资源：构成莫尔斯信号的音频单元文件
     */
    MakeMorseAudioFile(){
        //指定外部资源：构成莫尔斯信号的音频单元文件
        Context context = MyApplication.getContextObject();
        Resources res = context.getResources();
        streamFastBlank = res.openRawResource(R.raw.morse_blank_050ms_600hz);
        streamFastDa = res.openRawResource(R.raw.morse_da_150ms_600hz);
        streamFastDi = res.openRawResource(R.raw.morse_di_050ms_600hz);
        streamSlowBlank = res.openRawResource(R.raw.morse_blank_070ms_600hz);
        streamSlowDa = res.openRawResource(R.raw.morse_da_210ms_600hz);
        streamSlowDi = res.openRawResource(R.raw.morse_di_070ms_600hz);
        //实例化音频文件类
        makeAudioFile = new MakeAudioFile();
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
     * @param _outputFile 输出音频文件名
     * @param _morseCodes 输入莫尔斯码
     * @return 成功标识
     */
    public Boolean makeMorseAudioFile(String _outputFile, String _morseCodes){
        boolean returnValue;
        if (speedFlag == FAST){
            //合成高速音频文件
            returnValue = makeAudioFile.mergeAudioFiles(_outputFile, _morseCodes.toCharArray(), streamFastBlank, streamFastDa, streamFastDi);
        }
        else {
            //合成低速音频文件
            returnValue = makeAudioFile.mergeAudioFiles(_outputFile, _morseCodes.toCharArray(), streamSlowBlank, streamSlowDa, streamSlowDi);
        }
        return returnValue;
    }

    /**
     * 关闭InputStream
     * @throws java.lang.Throwable error
     */
    protected void finalize() throws java.lang.Throwable {
        streamFastBlank.close();
        streamFastDa.close();
        streamFastDi.close();
        streamSlowBlank.close();
        streamSlowDa.close();
        streamSlowDi.close();
        super.finalize();
    }
}
