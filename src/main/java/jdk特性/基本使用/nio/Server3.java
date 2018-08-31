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
 * Desc: reactor主从多线程
 * 1. 一个线程池处理一组端口 每个端口对应一个selector 一个线程
 * 2. 一个线程池处理r/w事件  每个线程一个selector
 * 3.
 * Author: DLJ
 * Date:
 */
public class Server3 {

    public Server3() {
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
        ExecutorService rwPool       = Executors.newFixedThreadPool(10);
        ExecutorService acceptorPool = Executors.newFixedThreadPool(10);
        acceptorPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
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
                                if (selectionKey.isAcceptable()) {
                                    SocketChannel socketChannel = serverSocketChannel.accept();
                                    socketChannel.configureBlocking(false);
                                    rwPool.submit(new Worker(socketChannel));
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class Worker implements Runnable {
        Selector      selector;
        SocketChannel socketChannel;

        public Worker(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            try {
                selector = Selector.open();
                socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                System.out.println("client:" + socketChannel.socket().getRemoteSocketAddress() + " connected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                int ready = selector.select(500);
                if (ready > 0) {
                    Set<SelectionKey>      selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator      = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                   //...
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String args[]) throws IOException {
        Server3 server = new Server3();

    }
}
