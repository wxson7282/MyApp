package com.example.wxson.morsetoy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by wxson on 2017/9/
 */

interface IInputWavStreamCache {
    /**
     * 从InputStream _stream中逐个字节读取，写入输出缓冲区并计算PCMSize
     * @param _stream 输入字节流
     * @return 读取成功标识
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean readStream(InputStream _stream);

    /**
     * 获取数据部分的长度
     * @return 数据部分的长度
     */
    int getPCMSize();

    /**
     * 获取数据部输出缓冲区
     * @return 数据部输出缓冲区
     */
    ByteArrayOutputStream  getDataStreamBuff();

}
