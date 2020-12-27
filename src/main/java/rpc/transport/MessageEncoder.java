package rpc.transport;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-20
 * Time: 20:50
 */
public class MessageEncoder {

    public List<ReqEntity> decodeReq(Context context) throws IOException {
        SelectionKey         selectionKey  = context.getSelectionKey();
        SocketChannel        socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer           readBuf       = context.getReaderBuf();
        int                  read          = socketChannel.read(readBuf);
        ArrayList<ReqEntity> reqs          = null;
        if (read > 0) {
            readBuf.flip();
            while (readBuf.remaining() > 4) {
                reqs = Lists.newArrayList();
                readBuf.mark();
                int reqIdLength      = readBuf.getInt();
                int clasLength       = readBuf.getInt();
                int methodNameLength = readBuf.getInt();
                int paramLength      = readBuf.getInt();
                if (readBuf.remaining() >= (reqIdLength + clasLength + methodNameLength + paramLength)) {
                    //协议体完整
                    ReqEntity reqEntity = ReqEntity.read(readBuf, reqIdLength, clasLength, methodNameLength,
                            paramLength);
                    reqs.add(reqEntity);
                } else {
                    //协议体不完整,恢复position
                    readBuf.reset();
                    break;
                }
            }
            //缓冲区整理
            bufferCollect(readBuf);

        }
        return reqs;
    }

    public List<ResEntity> decodeRes(Context context) throws IOException {
        SelectionKey         selectionKey  = context.getSelectionKey();
        SocketChannel        socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer           readBuf       = context.getReaderBuf();
        int                  read          = socketChannel.read(readBuf);
        ArrayList<ResEntity> reqs          = null;
        if (read > 0) {
            readBuf.flip();
            while (readBuf.remaining() > 4) {
                reqs = Lists.newArrayList();
                readBuf.mark();
                int reqIdLength   = readBuf.getInt();
                int resTypeLength = readBuf.getInt();
                int resLenght     = readBuf.getInt();
                if (readBuf.remaining() >= (reqIdLength + resTypeLength + resLenght)) {
                    //协议体完整
                    ResEntity reqEntity = ResEntity.read(readBuf, reqIdLength, resTypeLength, resLenght);
                    reqs.add(reqEntity);
                } else {
                    //协议体不完整,恢复position
                    readBuf.reset();
                    break;
                }
            }
            //缓冲区整理
            bufferCollect(readBuf);

        }
        return reqs;
    }


    /**
     * 缓冲区整理: 整理&切回到写模式
     * @param readBuf
     */
    private void bufferCollect(ByteBuffer readBuf) {
        ByteBuffer slice = readBuf.slice();
        readBuf.clear();
        readBuf.put(slice);
    }

}
