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
