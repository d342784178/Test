package jdk特性.基本使用.nio;

import com.google.common.collect.Maps;

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
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5555));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
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
                    }
                    if (selectionKey.isReadable()) {
                        System.out.println("读事件就绪");
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        SocketContext socketContext = SocketContext.get(socketChannel);
                        ByteBuffer    readBuffer    = socketContext.getReadBuffer();
                        int           read          = socketChannel.read(readBuffer);
                        readBuffer.flip();
                        if (read > 0) {
                            System.out.println(new String(readBuffer.array(), 0, read));
                        } else if (read == -1) {
                            System.out.println("断开..."
                                    + socketChannel.socket().getRemoteSocketAddress());
                            socketChannel.close();
                        }
                        readBuffer.clear();
                    }
                }
            }
        }
    }

    public static class SocketContext {
        private static Map<SocketChannel, SocketContext> map = Maps.newConcurrentMap();

        public static SocketContext get(SocketChannel socketChannel) {
            if (map.containsKey(socketChannel)) {
                return map.get(socketChannel);
            } else {
                SocketContext value = new SocketContext();
                map.put(socketChannel, value);
                return value;
            }
        }

        private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        private ByteBuffer readBuffer  = ByteBuffer.allocate(1024);

        public ByteBuffer getWriteBuffer() {
            return writeBuffer;
        }

        public ByteBuffer getReadBuffer() {
            return readBuffer;
        }
    }

    public static void main(String args[]) throws IOException {
        Server server = new Server();
    }
}
