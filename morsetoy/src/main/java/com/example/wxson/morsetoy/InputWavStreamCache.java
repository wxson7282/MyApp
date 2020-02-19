package com.example.wxson.morsetoy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by wxson on 2017/9/7.
 *
 */

class InputWavStreamCache implements IInputWavStreamCache {
    private int PCMSize;           //数据部分的长度
    private static final int headLen  = 44;     //wav文件头部的长度
    private ByteArrayOutputStream dataStreamBuff;



    /**
     * 从InputStream _stream中逐个字节读取，数据部写入buf并计算PCMSize
     * @param _stream 输入文件字节流 stream的打开关闭由调用方实现
     * @return 读取成功标识
     */
    public boolean readStream(InputStream _stream){
        //空stream判断
        if (_stream == null)
            return false;
        //变量初始化
        int size;
        byte[] buf = new byte[1024 * 1000]; //1M缓冲区
        PCMSize = 0;
        dataStreamBuff = new ByteArrayOutputStream();

        try{
            //读取stream
            size = _stream.read(buf);
            boolean isFirst=true;
            while (size != -1){
                if (isFirst){
                    //剔除输入文件的头部 写入缓冲区
                    dataStreamBuff.write(buf, headLen, size-headLen);
                    isFirst = false;
                }
                else {
                    //写入缓冲区
                    dataStreamBuff.write(buf, 0, size);
                }
                PCMSize += size;
                //读取stream
                size = _stream.read(buf);
            }
            //数据部长度应该扣除文件头长度
            PCMSize = PCMSize - headLen;
            //刷新此输出流并强制写出所有缓冲的输出字节
            dataStreamBuff.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @return 数据部分的长度
     */
    public int getPCMSize(){
        return PCMSize;
    }

    /**
     *
     * @return 数据部输出缓冲区
     */
    public ByteArrayOutputStream getDataStreamBuff(){
        return dataStreamBuff;
    }
}
