# MorseToy
在很多间谍军事题材的影视作品中出现过使用莫尔斯电码(morse code)进行通信的场景，随着现代通信技术的发展，莫尔斯电码已经鲜有使用。尽管如此，仍然有一些莫尔斯电码的爱好者出于怀旧或者好奇，想听到莫尔斯电码的声音或者用莫尔斯电码作手机铃声，为此利用闲暇时间做了一款安卓版的莫尔斯编码器。
<img>![用户界面](http://img.blog.csdn.net/20171027165029302?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3hzb24=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
在此不可能把全部代码列出来，仅就开发要点做一点说明。

 - 莫尔斯电码有多种标准，这里实现的是比较常用的标准莫尔斯码和电报码。标准莫尔斯码包括字母和数字，电报码包括仅由数字组成的长码和短码。关于莫尔斯电码的知识请参考互联网上的资料。
 
 - 这个编码器实现两个功能：
1 把输入的电文翻译成用点和划表示的莫尔斯码字符，电文中不包含汉字等全角字符。
2 把莫尔斯码字符转换为音频信号。音频信号可以制成文件，保存在手机里。

 - 电文翻译的实现方法
 在资源中定义字符数组和莫尔斯码数组，字符和莫尔斯码一一对应，[A]对用[._]，[B]对应[_...]   ...   。
 用以下方法把两个数组加入到ArrayMap。
```
    //全体item加入到arrayMap
    private void AllItemAdd() {
        for (int i = 0; i < charArray.length; i++){
            codingArrayMap.put(charArray[i], morseCodeArray[i]);
        }
    }
```
根据字符取出莫尔斯码就容易了。
```
    //将字符翻译成莫尔斯电码 如果字符不存在，返回*
    public String GetMorseCode(char _char) {
        String chr = String.valueOf(_char);
        if (codingArrayMap.containsKey(chr)){
            return codingArrayMap.get(chr);
        }
        else{
            return "*";
        }
    }
```

 - 莫尔斯码转换音频的方法
这里采用了两种手段。在手机上播放莫尔斯码音频时使用SoundPool，制成音频文件时使用InputStream和FileOutputStream。
这两种手段的音频源是同一处，在资源中定义以下六个音频文件分别表示快速点、划、空和慢速点、划、空所对应的音频。
morse_blank_050ms_600hz.wav
morse_blank_070ms_600hz.wav
morse_da_150ms_600hz.wav
morse_da_210ms_600hz.wav
morse_di_050ms_600hz.wav
morse_di_070ms_600hz.wav

 - SoundPool的使用
 定义 SoundPool， 加载音频文件

```
    // 设置最多可容纳4个音频流，音频的品质为5
    private final SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 5);

        // load方法加载指定音频文件，并返回所加载的音频ID。
        Context context = MyApplication.getContextObject();
        streamIdSlowDi = soundPool.load(context, R.raw.morse_di_070ms_600hz , 1);
        streamIdSlowDa = soundPool.load(context, R.raw.morse_da_210ms_600hz , 1);
        streamIdFastDi = soundPool.load(context, R.raw.morse_di_050ms_600hz , 1);
        streamIdFastDa = soundPool.load(context, R.raw.morse_da_150ms_600hz , 1);

```
 
 循环解析莫尔斯码，遇到【点】时，播放streamIdSlowDi或streamIdFastDi；遇到【划】时，播放streamIdSlowDa或streamIdFastDa；遇到【空】时，延时若干毫秒。
 

```
    public void playDa(){
        if (speedFlag){
            soundPool.play(streamIdFastDa,1, 1, 0, 0, 1);
            delay(delayFastDa + delayFastDi);
        }
        else{
            soundPool.play(streamIdSlowDa,1, 1, 0, 0, 1);
            delay(delaySlowDa + delaySlowDi);
        }
    }

    public void playDi(){
        if (speedFlag){
            soundPool.play(streamIdFastDi,1, 1, 0, 0, 1);
            delay(delayFastDi + delayFastDi);
        }
        else{
            soundPool.play(streamIdSlowDi,1, 1, 0, 0, 1);
            delay(delaySlowDi + delaySlowDi);
        }
    }

    public void playBlank(){
        if (speedFlag){
            delay(delayFastDa + delayFastDi);
        }
        else{
            delay(delaySlowDa + delaySlowDi);
        }
    }
    private void delay(long _ms){
        try {
            Thread.sleep(_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
这个方法有一个缺陷，初次播放时有一点延迟，耳朵能够听出来。

 - 音频文件制作
在程序中定义六个InputStream来读取上述六个音频文件。

```
    final private InputStream streamFastBlank;        //空音频文件
    final private InputStream streamFastDa;           //长音音频文件
    final private InputStream streamFastDi;           //短音音频文件
    final private InputStream streamSlowBlank;        //空音频文件
    final private InputStream streamSlowDa;           //长音音频文件
    final private InputStream streamSlowDi;           //短音音频文件
        //指定外部资源：构成莫尔斯信号的音频单元文件
        Context context = MyApplication.getContextObject();
        Resources res = context.getResources();
        streamFastBlank = res.openRawResource(R.raw.morse_blank_050ms_600hz);
        streamFastDa = res.openRawResource(R.raw.morse_da_150ms_600hz);
        streamFastDi = res.openRawResource(R.raw.morse_di_050ms_600hz);
        streamSlowBlank = res.openRawResource(R.raw.morse_blank_070ms_600hz);
        streamSlowDa = res.openRawResource(R.raw.morse_da_210ms_600hz);
        streamSlowDi = res.openRawResource(R.raw.morse_di_070ms_600hz);
        
```
音频文件制作的过程就是根据莫尔斯码来拼接输出到FileOutputStream。
循环解析莫尔斯码，
遇到【点】时，输出streamFastDi或streamSlowDi；
遇到【划】时，输出streamFastDa或streamSlowDa；
遇到【空】时，输出streamFastBlank或streamSlowBlank。
需要说明的是，六个输入文件采用16位单声道的wav格式的音频文件，拼接后的输出文件也采用相同格式。拼接输出时要注意44个字节的wav文件头的编辑。 有关wav文件格式请参考互联网上的资料。

```
package com.example.wxson.morsetoy;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by wxson on 2017/9/1.
 *
 */

class MakeAudioFile implements IMakeAudioFile {
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
   public boolean mergeAudioFiles(String _outputFile, char[] _morseCodes, InputStream _streamBlank, InputStream _streamDa, InputStream _streamDi){
        try {
            //读取音频流的实例
            IInputWavStreamCache streamCacheBlank = new InputWavStreamCache();
            IInputWavStreamCache streamCacheDa = new InputWavStreamCache();
            IInputWavStreamCache streamCacheDi = new InputWavStreamCache();

            //读取音频流到缓存
            if (!streamCacheBlank.readStream(_streamBlank))
                return false;
            if (!streamCacheDa.readStream(_streamDa))
                return false;
            if (!streamCacheDi.readStream(_streamDi))
                return false;

            //逐个解析莫尔斯码，计算数据部总长度
            int totalPCMSize = 0;
            for(char chr : _morseCodes){
                switch (chr){
                    case '.':
                        totalPCMSize += streamCacheDi.getPCMSize();
                        totalPCMSize += streamCacheBlank.getPCMSize();
                        break;
                    case '_':
                        totalPCMSize += streamCacheDa.getPCMSize();
                        totalPCMSize += streamCacheBlank.getPCMSize();
                        break;
                    default:
                        totalPCMSize += streamCacheBlank.getPCMSize() * 4;
                        break;
                }
            }

            //编辑输出音频文件头部
            WavFileHeader wavFileHeader = new WavFileHeader();
            wavFileHeader.mChunkSize = totalPCMSize + 44 - 8;   //文件总长度 - 8
            wavFileHeader.mSubChunk2Size = totalPCMSize;        //数据部长度
            byte[] header = wavFileHeader.getHeader();          //取得输出文件头
            if (BuildConfig.DEBUG && header.length != 44){              //文件头长度检查
                throw new AssertionError("wav file head.length != 44");
            }

            FileOutputStream fos = new FileOutputStream( _outputFile);   //定义输出文件
            fos.write(header, 0, header.length);                        //输出文件头

            //取得输入音频流的数据部
            byte[] streamBuffBlank = streamCacheBlank.getDataStreamBuff().toByteArray();
            byte[] streamBuffDa = streamCacheDa.getDataStreamBuff().toByteArray();
            byte[] streamBuffDi = streamCacheDi.getDataStreamBuff().toByteArray();

            //逐个解析莫尔斯码，数据部输出
            for(char chr : _morseCodes){
                switch (chr){
                    case '.':
                        fos.write(streamBuffDi);
                        fos.write(streamBuffBlank);
                        break;
                    case '_':
                        fos.write(streamBuffDa);
                        fos.write(streamBuffBlank);
                        break;
                    default:
                        fos.write(streamBuffBlank);
                        fos.write(streamBuffBlank);
                        fos.write(streamBuffBlank);
                        fos.write(streamBuffBlank);
                        break;
                }
            }
            //关闭输出
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

```
上述内容是在android studio上实现。
代码保存在[github上](https://github.com/wxson7282/MyApp/tree/master/morsetoy)，欢迎有兴趣的朋友交流。
如果发现bug或者需要改进的地方，请不吝赐教。wxson@126.com
