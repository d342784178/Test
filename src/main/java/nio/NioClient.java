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
 * Desc: 多用户聊天客户端
 * Author: DLJ
 * Date:
 */
public class NioClient {
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer  = ByteBuffer.allocate(1024);

    public NioClient() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //接受控制台输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            writeBuffer.put(scanner.nextLine().getBytes());
        }
    }

    /**
     * 类似server的init方法过程
     * @throws Exception
     */
    private void init() throws Exception {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        //注意connect必须在reigster之后调 否则没有isConnectable事件
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
                        readBuffer.clear();
                        int read = socketChannel.read(readBuffer);
                        if (read > 0) {
                            System.out.println(new String(readBuffer.array(), 0, read));
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
        new NioClient();
    }
}
