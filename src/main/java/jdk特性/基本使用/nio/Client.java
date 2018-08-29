package jdk特性.基本使用.nio;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Desc: ${DESCIPTION} User: DLJ Date: 2016/12/3
 */
public class Client {
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public Client() {
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

    public void write(byte[] bytes) {
        writeBuffer.put(bytes);
    }

    private void init() throws Exception {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("localhost", 5555));
        while (true) {
            if (channel.isConnected()) {
                //TODO 当socket发送缓冲区满了 会发生空转
                writeBuffer.flip();
                while (writeBuffer.hasRemaining()) {
                    channel.write(writeBuffer);
                }
                writeBuffer.clear();
            }
            //当没有事件触发时会阻塞
            int ready = selector.select(500);
            if (ready > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.configureBlocking(false);
                    if (selectionKey.isConnectable()) {
                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        System.out.println("读事件就绪");
                        ByteBuffer readBuffer = Server.SocketContext.get(socketChannel).getReadBuffer();
                        int        read       = socketChannel.read(readBuffer);
                        readBuffer.flip();
                        if (read > 0) {
                            System.out.print(new String(readBuffer.array(), 0, read));
                        } else if (read == -1) {
                            System.out.println("断开..."
                                    + socketChannel.socket().getRemoteSocketAddress());
                            socketChannel.close();
                        }
                    }
                }
            }
        }


    }

    public static void main(String args[]) throws IOException {
        ArrayList<Client> clients = Lists.newArrayList();
        for (int i = 0; i < 1000; i++) {
            Client client = new Client();
            client.write(("hello" + i).getBytes());
            clients.add(client);
        }
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            clients.forEach(client -> {
                client.write(s.getBytes());
            });
        }


    }
}
