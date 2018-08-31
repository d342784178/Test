package jdk特性.基本使用.nio;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import 积累.util.ByteUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Desc: nio客户端
 * 1.writeable事件 不要一直订阅 因为当socket缓冲区可写入时就会触发该事件
 * 2.
 * Author: ljdong2
 * Date: 2018/8/31
 */
public class Client {
    private ByteBuf       writeBuffer = new UnpooledByteBufAllocator(false).buffer(1024 * 5, 1024 * 10000);
    private SocketChannel channel;

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


    private void init() throws Exception {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("localhost", 5555));
        while (channel.isOpen()) {
            if (channel.isConnected() && writeBuffer.isReadable()) {
                //writeBuffer可读 注册write事件
                channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
            // 当采用临时订阅OP_WRITE方式 必须使用select(ms)进行超时返回
            // 因为很有可能当select()前极短时间内writeBuffer有数据,而此时没有订阅OP_WRITE事件,会使select()一直阻塞
            int ready = selector.select(300);
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
                    }
                    if (selectionKey.isReadable()) {
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
                    if (selectionKey.isWritable()) {
                        while (writeBuffer.isReadable()) {
                            ByteBuffer byteBuffer = writeBuffer.nioBuffer();
                            channel.write(byteBuffer);
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

    public void close() {
        try {
            //TODO 怎么保证写出完成
            while (true) {
                int canWrite = writeBuffer.writerIndex() - writeBuffer.readerIndex();
                if (canWrite != 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] bytes) {
        //如何防止缓冲区溢出
        while (true) {
            int canWrite = writeBuffer.maxCapacity() - writeBuffer.writerIndex();
            if (canWrite < bytes.length) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        writeBuffer.writeBytes(bytes);
    }

    public static void main(String args[]) throws IOException {
        ArrayList<Client> clients = Lists.newArrayList();
        for (int i = 0; i < 1; i++) {
            Client client = new Client();
            clients.add(client);
        }
        for (int i = 0; i < 1024 / 4 * 15000; i++) {
            int finalI = i;
            clients.forEach(client -> {
                //线程安全
                client.write(ByteUtils.getBytes(finalI));
            });
        }
        clients.forEach(client -> {
            client.close();
        });
    }
}
