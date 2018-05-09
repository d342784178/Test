package jdk特性.基本使用.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

/**
 * Desc: 多用户聊天服务器
 * Author: DLJ
 * Date:
 */
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

    /**
     * 读取控制台输入
     *
     * @exception Exception
     */
    private void putData() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            msgBuffer.put(scanner.nextLine().getBytes());
            dispatcherMsg();
        }
    }

    /**
     * 解析下流程
     * 1.注册serverSocketChannel 到selector
     * 2.当有clients连接时 进入if (selectionKey.isAcceptable())条件
     * 3.获取socketChannel并注册到selector
     * 3.当socketChannel读写事件就绪时 进入读写事件判断雕件
     * 注意:由于是非阻塞式的 实际调用是会不断进入读写事件 !!所以要注意清空缓存避免重复读写!!!
     *
     * @exception Exception
     */
    private void init() throws Exception {
        //开启端口坚挺
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(55555));
        //切换非阻塞模式(selector必须使用非阻塞模式)
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        //注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //获取发生事件的个数
            int ready = selector.select();
            if (ready > 0) {
                //获取发生事件的对象结合
                Set<SelectionKey>      selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator      = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //注意:必须手动remove作为消耗事件!!
                    iterator.remove();
                    handle(serverSocketChannel, selector, selectionKey);
                }
            }
        }
    }

    /**
     * 处理相应事件
     *
     * @param serverSocketChannel
     * @param selector
     * @param selectionKey
     *
     * @exception IOException
     */
    private void handle(ServerSocketChannel serverSocketChannel, Selector selector, SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            //获取socketchannel
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            //把这个socketchannel注册到selector中管理
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            System.out.println("client:" + socketChannel.socket().getRemoteSocketAddress() + " connected");
            //添加到clients集合中
            clients.put(socketChannel, ByteBuffer.allocate(1024));
        } else if (selectionKey.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            socketChannel.configureBlocking(false);
            msgBuffer.clear();
            int read = socketChannel.read(msgBuffer);
            //因为会不断进入读写的判断条件 所以必须判断是否有读入内容
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
            //因为会不断进入读写的判断条件 所以必须判断是否有内容可供写出
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            //写完 清空缓存
            byteBuffer.clear();
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
        //复制msgBuffer 每个channel各一个
        for (Map.Entry<Channel, ByteBuffer> entry : clients.entrySet()) {
            try {
                ByteBuffer byteBuffer = entry.getValue();
                byte[]     bytes      = new byte[msgBuffer.position()];
                System.arraycopy(msgBuffer.array(), 0, bytes, 0, msgBuffer.position());
                byteBuffer.put(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        NioServer server = new NioServer();
    }
}
