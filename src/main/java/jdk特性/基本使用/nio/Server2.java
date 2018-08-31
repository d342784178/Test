package jdk特性.基本使用.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc: reactor多线程
 * 1. 一个线程处理connect事件
 * 2. 一个线程池处理r/w事件
 * 3. 所有事件注册到一个selector中
 * Author: DLJ
 * Date:
 */
public class Server2 {

    public Server2() {
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
        ExecutorService     executorService     = Executors.newFixedThreadPool(10);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5555));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int ready = selector.select(500);
            if (ready > 0) {
                Set<SelectionKey>      selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator      = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isConnectable()) {
                    } else if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("client:" + socketChannel.socket().getRemoteSocketAddress() + " connected");
                    } else {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (selectionKey.isReadable()) {
                                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                                        socketChannel.configureBlocking(false);
                                        ByteBuffer readBuffer = Server.SocketContext.get(socketChannel).getReadBuffer();
                                        int        read       = socketChannel.read(readBuffer);
                                        readBuffer.flip();
                                        if (read > 0) {
                                            System.out.println(new String(readBuffer.array(), 0, read));
                                        }else if (read == -1) {
                                            System.out.println("断开..."
                                                    + socketChannel.socket().getRemoteSocketAddress());
                                            socketChannel.close();
                                        }
                                        readBuffer.clear();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }
    }


    public static void main(String args[]) throws IOException {
        Server2 server = new Server2();
    }
}
