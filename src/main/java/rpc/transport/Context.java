package rpc.transport;

import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-20
 * Time: 20:52
 */
@Data
public abstract class Context<T, R extends IOutPut> {
    private ByteBuffer          readerBuf;
    private ByteBuffer          writebuf;
    private SelectionKey        selectionKey;
    private ArrayDeque<IOutPut> writeDeque;

    public Context(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.readerBuf = ByteBuffer.allocate(1024);
        this.writebuf = ByteBuffer.allocate(1024);
        this.writeDeque = new ArrayDeque<>();

    }

    public abstract List<T> read() throws IOException;

    public void write(List<R> o) {
        for (R r : o) {
            writeDeque.push(r);
        }
    }


    public void flushWrite() throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        while (true) {
            IOutPut resEntity = writeDeque.pollLast();
            if (resEntity == null) {
                return;
            }
            resEntity.outPut(writebuf);
            writebuf.flip();
            while (writebuf.hasRemaining()) {
                socketChannel.write(writebuf);
            }
            writebuf.clear();
        }
    }
}
