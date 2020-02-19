package com.example.wxson.morsetoy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by wxson on 2017/9/1.
 * wav音频文件头
 * 对应32位浮点采样速率44100Hz单声道格式
 */

class WavFileHeader {
    int mChunkSize = 0;              //从mFormat到文件尾的总字节数
    int mSubChunk2Size  = 0;         //数据部分的长度

    byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String mChunkID = "RIFF";
        WriteChar(bos, mChunkID.toCharArray());
        WriteInt(bos, mChunkSize);
        String mFormat = "WAVE";
        WriteChar(bos, mFormat.toCharArray());
        String mSubChunk1ID = "fmt ";
        WriteChar(bos, mSubChunk1ID.toCharArray());
        int mSubChunk1Size = 16;
        WriteInt(bos, mSubChunk1Size);
        short mAudioFormat = 1;
        WriteShort(bos, mAudioFormat);
        short mNumChannel = 1;
        WriteShort(bos, mNumChannel);
        int mSampleRate = 44100;
        WriteInt(bos, mSampleRate);
        int mByteRate = 88200;
        WriteInt(bos, mByteRate);
        short mBlockAlign = 2;
        WriteShort(bos, mBlockAlign);
        short mBitsPerSample = 16;
        WriteShort(bos, mBitsPerSample);
        String mSubChunk2ID = "data";
        WriteChar(bos, mSubChunk2ID.toCharArray());
        WriteInt(bos, mSubChunk2Size);
        bos.flush();
        byte[] r = bos.toByteArray();
        bos.close();
        return r;
    }

    private void WriteChar(ByteArrayOutputStream bos, char[] id)
    {
        for (char c : id) {
            bos.write(c);
        }
    }

    private void WriteShort(ByteArrayOutputStream bos, int s)
            throws IOException
    {
        byte[] myByte = new byte[2];
        myByte[1] = (byte) ((s << 16) >> 24);
        myByte[0] = (byte) ((s << 24) >> 24);
        bos.write(myByte);
    }

    private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException
    {
        byte[] buf = new byte[4];
        buf[3] = (byte) (n >> 24);
        buf[2] = (byte) ((n << 8) >> 24);
        buf[1] = (byte) ((n << 16) >> 24);
        buf[0] = (byte) ((n << 24) >> 24);
        bos.write(buf);
    }


}
