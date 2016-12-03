package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class NioServer {
    private ByteBuffer                   msgBuffer = ByteBuffer.allocate(1024);
    private HashMap<Channel, ByteBuffer> clients   = new HashMap<Channel, ByteBuffer>();

    public NioServer() {
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
            msgBuffer.put(scanner.nextLine().getBytes());
            dispatcherMsg();
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
                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("client:" + socketChannel.socket().getRemoteSocketAddress() + " connected");
                        clients.put(socketChannel, ByteBuffer.allocate(1024));
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        msgBuffer.clear();
                        int read = socketChannel.read(msgBuffer);
                        if (read > 0) {
                            addName(socketChannel.socket());
                            System.out.println(new String(msgBuffer.array(), 0, msgBuffer.position()));
                            dispatcherMsg();
                        }
                    } else if (selectionKey.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer    byteBuffer    = clients.get(selectionKey.channel());
                        socketChannel.configureBlocking(false);
                        byteBuffer.flip();
                        while (byteBuffer.hasRemaining()) {
                            socketChannel.write(byteBuffer);
                        }
                        byteBuffer.clear();
                    }
                }
            }
        }
    }

    /**
     * 开头增加client名字
     *
     * @param socket
     */
    private void addName(Socket socket) {
        SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
        byte[]        bytes               = (remoteSocketAddress.toString() + ":\n").getBytes();
        byte[]        bytes1              = new byte[bytes.length + msgBuffer.position()];
        System.arraycopy(bytes, 0, bytes1, 0, bytes.length);
        System.arraycopy(msgBuffer.array(), 0, bytes1, bytes.length, msgBuffer.position());
        msgBuffer.clear();
        msgBuffer.put(bytes1);

    }

    /**
     * 分发msg
     */
    private void dispatcherMsg() {
        for (Map.Entry<Channel, ByteBuffer> entry : clients.entrySet()) {
            ByteBuffer byteBuffer = entry.getValue();
            byte[]     bytes      = new byte[msgBuffer.position()];
            System.arraycopy(msgBuffer.array(), 0, bytes, 0, msgBuffer.position());
            byteBuffer.put(bytes);
        }
    }

    public static void main(String args[]) throws IOException {
        NioServer server = new NioServer();
    }
}
