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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
            int ready = selector.select();
            if (ready > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    if (writeBuffer.writerIndex() > (writeBuffer.maxCapacity() / 3 * 2) && writeBuffer.writerIndex() - writeBuffer
                            .readerIndex() < (writeBuffer.maxCapacity() / 3)) {
                        System.out.println(String.format("缓冲区使用超过2/3 discardReadBytes writerIndex:%d " +
                                "readerIndex:%d", writeBuffer
                                .writerIndex(), writeBuffer.readerIndex()));
                        writeBuffer.discardReadBytes();
                    }

                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.configureBlocking(false);
                    if (selectionKey.isConnectable()) {
                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
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
                        //改为主动读取式
                        ByteBuffer byteBuffer = awaitGetWrite(writeBuffer, 30, 50);
                        if (byteBuffer != null) {
                            int write = channel.write(byteBuffer);
                            writeBuffer.readerIndex(writeBuffer.readerIndex() + write);
                            if (write != byteBuffer.limit()) {
                                System.out.print("a");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 等待获取写缓存
     * @param byteBuf 缓冲区
     * @param ms      缓冲时间 防止空转
     * @param cap     阈值:超过则直接返回,没超过等待ms后判断是否超过阈值
     * @return
     */
    public ByteBuffer awaitGetWrite(ByteBuf byteBuf, long ms, int cap) {
        //缓冲大小 稍大于socket缓冲区大小最好 自己调整
        int socketCap = 1024 * 100;
        if (byteBuf.readableBytes() >= cap) {//>=cap直接返回
            return byteBuf.copy(0, byteBuf.readableBytes() > socketCap ? socketCap : byteBuf.readableBytes())
                          .nioBuffer();
        } else {//<cap等待
            CountDownLatch countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await(ms, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (byteBuf.readableBytes() > 0) {
                return byteBuf.copy(0, byteBuf.readableBytes() > socketCap ? socketCap : byteBuf.readableBytes())
                              .nioBuffer();
            } else {
                return null;
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
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1024 / 4 * 15000; i++) {
            int finalI = i;
            clients.forEach(client -> {
                //线程安全
                client.write(ByteUtils.getBytes(finalI));
            });
        }
        clients.forEach(Client::close);
        System.out.println(System.currentTimeMillis() - l);
    }
}
