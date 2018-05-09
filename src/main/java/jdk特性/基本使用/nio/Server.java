package jdk特性.基本使用.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Desc:
 * Author: DLJ
 * Date:
 */
public class Server {
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer  = ByteBuffer.allocate(1024);

    public Server() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            putData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putData() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            writeBuffer.put(scanner.nextLine().getBytes());
        }
    }

    private void init() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5555));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int ready = selector.select();
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
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        int read = socketChannel.read(readBuffer);
                        readBuffer.flip();
                        if (read > 0) {
                            System.out.print(new String(readBuffer.array(), 0, read));
                        }
                    } else if (selectionKey.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        writeBuffer.flip();
                        while (writeBuffer.hasRemaining()) {
                            socketChannel.write(writeBuffer);
                        }
                        writeBuffer.clear();
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        Server server = new Server();
    }
}
