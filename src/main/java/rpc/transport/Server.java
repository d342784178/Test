package rpc.transport;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @link http://www.infoq.com/cn/articles/netty-threading-model
 * 注意: reactor线程模型不包括业务处理 只包含tcp连接(connect),读写(r/w)过程的处理
 * Desc: reactor单线程
 * 1. 所有事件处理在同一个线程(不包括业务处理) 一个selector
 * Author: DLJ
 * Date:
 */
public class Server {


    public Server() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void init() throws Exception {
        AtomicInteger       a                   = new AtomicInteger();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5555));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            for (Map.Entry<SocketChannel, SocketContext> socketChannelSocketContextEntry :
                    SocketContext.map.entrySet()) {
                SocketChannel channel       = socketChannelSocketContextEntry.getKey();
                SocketContext socketContext = socketChannelSocketContextEntry.getValue();
                ByteBuf       writeBuffer   = socketContext.getWriteBuffer();

                if (channel.isConnected() && writeBuffer.isReadable()) {
                    //writeBuffer可读 注册write事件
                    channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
            }

            int ready = selector.select(500);
            //防止空转
            if (ready > 0) {
                Set<SelectionKey>      selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator      = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("client:" + socketChannel.socket().getRemoteSocketAddress() + " connected");
                    } else {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        SocketContext socketContext = SocketContext.get(socketChannel);
                        ByteBuf       readBuffer    = socketContext.getReadBuffer();
                        ByteBuf       writeBuffer   = socketContext.getWriteBuffer();

                        if (selectionKey.isReadable()) {
                            //考虑tcp分包 保证一次读完
                            ByteBuffer allocate = ByteBuffer.allocate(1024);
                            int        read     = socketChannel.read(allocate);
                            allocate.flip();
                            if (read > 0) {
                                readBuffer.writeBytes(allocate.array(), 0, read);
                            } else if (read == -1) {
                                SocketContext.remove(socketChannel);
                                socketChannel.close();
                                System.out.println("断开..."
                                        + socketChannel.socket().getRemoteSocketAddress());
                                continue;
                            }
                        }
                        if (selectionKey.isWritable()) {
                            while (writeBuffer.isReadable()) {
                                ByteBuffer byteBuffer = writeBuffer.nioBuffer();
                                socketChannel.write(byteBuffer);
                                writeBuffer.readerIndex(writeBuffer.readerIndex() + byteBuffer.position());
                                int left = byteBuffer.limit() - byteBuffer.position();
                                if (left != 0) {//无法一次性写入到缓冲区中,可能发生空转 break
                                    System.err.print("a");
                                    if (writeBuffer.writerIndex() > (writeBuffer.maxCapacity() / 3 * 2) && writeBuffer.writerIndex() - writeBuffer
                                            .readerIndex() < (writeBuffer.maxCapacity() / 3)) {
                                        System.out.println(String.format("缓冲区使用超过2/3 discardReadBytes writerIndex:%d " +
                                                "readerIndex:%d", writeBuffer
                                                .writerIndex(), writeBuffer.readerIndex()));
                                        writeBuffer.discardReadBytes();
                                    }
                                    //防止空转 等待下次write事件
                                    break;
                                } else {
                                    //注意clear()的使用 因为writeBuffer一直在写入 writerIndex可能>readIndex
                                    if (writeBuffer.writerIndex() == writeBuffer.readerIndex()) {
                                        //TODO 因为不是原子过程 理论上会有问题 但实际验证中却没问题 待验证
                                        writeBuffer.clear();
                                        System.out.println("clear");
                                    } else {
                                        System.out.println("discardReadBytes");
                                        writeBuffer.discardReadBytes();
                                    }

                                }
                            }
                            //writeBuffer不可读 注销write事件
                            if (!writeBuffer.isReadable()) {
                                socketChannel.register(selector, SelectionKey.OP_READ);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class SocketContext {
        public static Map<SocketChannel, SocketContext> map = Maps.newConcurrentMap();

        public static SocketContext get(SocketChannel socketChannel) {
            if (map.containsKey(socketChannel)) {
                return map.get(socketChannel);
            } else {
                SocketContext value = new SocketContext();
                map.put(socketChannel, value);
                return value;
            }
        }

        public static SocketContext remove(SocketChannel socketChannel) throws IOException {
            return map.remove(socketChannel);

        }

        private ByteBuf writeBuffer = new UnpooledByteBufAllocator(false).buffer(1024, 1024 * 10000);
        private ByteBuf readBuffer  = new UnpooledByteBufAllocator(false).buffer(1024);

        public ByteBuf getWriteBuffer() {
            return writeBuffer;
        }

        public ByteBuf getReadBuffer() {
            return readBuffer;
        }
    }

    public static void main(String args[]) throws IOException {
        Server server = new Server();
    }
}
