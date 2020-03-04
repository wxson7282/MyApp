package com.example.wxson.morsetoy;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by wxson on 2017/9/1.
 *
 */

class AudioFileMaker implements IAudioFileMaker {
    private IInputWavStreamCache blankElement;
    private IInputWavStreamCache longElement;
    private IInputWavStreamCache shortElement;

    @Override
    public void setAudioElement(IInputWavStreamCache _streamBlank, IInputWavStreamCache _streamDa, IInputWavStreamCache _streamDi) {
        //读取音频流到缓存
        blankElement = _streamBlank;
        longElement = _streamDa;
        shortElement = _streamDi;
    }

    @Override
    public void makeAudioFile(String _outputFile, char[] _morseCodes) throws Exception {
        //逐个解析莫尔斯码，计算数据部总长度
        int totalPCMSize = 0;
        for (char chr : _morseCodes) {
            switch (chr) {
                case '.':
                    totalPCMSize += shortElement.getPCMSize();
                    totalPCMSize += blankElement.getPCMSize();
                    break;
                case '_':
                    totalPCMSize += longElement.getPCMSize();
                    totalPCMSize += blankElement.getPCMSize();
                    break;
                default:
                    totalPCMSize += blankElement.getPCMSize() * 4;
                    break;
            }
        }

        //编辑输出音频文件头部
        WavFileHeader wavFileHeader = new WavFileHeader();
        wavFileHeader.mChunkSize = totalPCMSize + 44 - 8;   //文件总长度 - 8
        wavFileHeader.mSubChunk2Size = totalPCMSize;        //数据部长度
        byte[] header = wavFileHeader.getHeader();          //取得输出文件头
        if (BuildConfig.DEBUG && header.length != 44) {              //文件头长度检查
            throw new AssertionError("wav file head.length != 44");
        }

        FileOutputStream fos = new FileOutputStream(_outputFile);   //定义输出文件
        fos.write(header, 0, header.length);                        //输出文件头

        //取得输入音频流元素的数据部
        byte[] streamBuffBlank = blankElement.getDataStreamBuff().toByteArray();
        byte[] streamBuffDa = longElement.getDataStreamBuff().toByteArray();
        byte[] streamBuffDi = shortElement.getDataStreamBuff().toByteArray();

        //逐个解析莫尔斯码，数据部输出
        for (char chr : _morseCodes) {
            switch (chr) {
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
}
