package jdk特性.基本使用.nio;


import org.junit.Test;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Desc: nio测试用例
 * Author: DLJ
 * Date:
 */
public class NioDemo {
    @Test
    public void readFile() throws Exception {
        RandomAccessFile r = new RandomAccessFile("/Users/mic/IdeaProjects/webTest/nio/pom.xml", "r");
        //获取一个channel
        FileChannel channel = r.getChannel();
        //声明一个buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int        read;
        while ((read = channel.read(byteBuffer)) > 0) {
            //切换到写模式
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, read));
            //清空bytebuffer内标记(注意不清空内存 再次写会覆盖内存)
            byteBuffer.clear();
        }
        channel.close();
        r.close();
    }

    @Test
    public void testServerSocket() throws Exception {
        //open开启一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(5555));
        //死循环等待client连接
        while (true) {
            //此时serverSocketChannel为同步状态
            SocketChannel socketChannel = serverSocketChannel.accept();
            //类似c的malloc 申请一段内存
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //写内存
            byteBuffer.put("welcome".getBytes());
            //切换写模式到读模式
            byteBuffer.flip();
            socketChannel.write(byteBuffer);

            //clear之后回到写模式 注意:!!!clear后千万不要再调flip()
            //原因:clear时position=0 flip时limit = position 也就变成0了 然后就写不进去了
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, byteBuffer.remaining()));
        }
    }

    @Test
    public void testSocket() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 5555));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.remaining()));

        byteBuffer.clear();
        byteBuffer.put("client is connected".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        socketChannel.close();

    }
}
