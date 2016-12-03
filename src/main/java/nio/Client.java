package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Desc: ${DESCIPTION} User: DLJ Date: 2016/12/3
 */
public class Client {
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public Client() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            writeBuffer.put(scanner.nextLine().getBytes());
        }
    }

    private void init() throws Exception {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("localhost", 5555));
        while (true) {
            int ready = selector.select();
            if (ready > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isConnectable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
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
        new Client();
    }
}
