package aio;

import com.sun.tools.corba.se.idl.toJavaPortable.Helper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
//import java.nio.channels.WritePendingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;

public class AioServer {
    private final AsynchronousServerSocketChannel server;
    //写队列，因为当前一个异步写调用还没完成之前，调用异步写会抛WritePendingException
    //所以需要一个写队列来缓存要写入的数据，这是AIO比较坑的地方
    private final Queue<ByteBuffer> queue   = new LinkedList<ByteBuffer>();
    private       boolean           writing = false;

    public static void main(String[] args) throws IOException {
        AioServer xiaona = new AioServer();
        xiaona.listen();
    }

    public AioServer() throws IOException {
        //设置线程数为CPU核数
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
        server = AsynchronousServerSocketChannel.open(channelGroup);
        //重用端口
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        //绑定端口并设置连接请求队列长度
        server.bind(new InetSocketAddress(55555), 80);
    }

    public void listen() {
        System.out.println(Thread.currentThread().getName() + ": run in listen method");
        //开始接受第一个连接请求
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel channel,
                                  Object attachment) {
                System.out.println(Thread.currentThread().getName() + ": run in accept completed method");

                //先安排处理下一个连接请求，异步非阻塞调用，所以不用担心挂住了
                //这里传入this是个地雷，小心多线程
                server.accept(null, this);
                //处理连接读写
                handle(channel);
            }

            private void handle(final AsynchronousSocketChannel channel) {
                System.out.println(Thread.currentThread().getName() + ": run in handle method");
                //每个AsynchronousSocketChannel，分配一个缓冲区
                final ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
                readBuffer.clear();
                channel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {

                    @Override
                    public void completed(Integer count, Object attachment) {
                        System.out.println(Thread.currentThread().getName() + ": run in read completed method");
                        if (count > 0) {
                            readBuffer.flip();
                            channel.write(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer buffer) {
                                    if (buffer.hasRemaining()) {
                                        channel.write(buffer, buffer, this);
                                    } else {
                                        // Go back and check if there is new data to write
                                        channel.write(readBuffer, readBuffer, this);
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    System.out.println("server write failed: " + exc);
                                }
                            });
                            readBuffer.clear();
                        } else {
                            try {
                                //如果客户端关闭socket，那么服务器也需要关闭，否则浪费CPU
                                channel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        //异步调用OS处理下个读取请求
                        //这里传入this是个地雷，小心多线程
                        channel.read(readBuffer, null, this);
                    }

                    /**
                     * 服务器读失败处理
                     * @param exc
                     * @param attachment
                     */
                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        System.out.println("server read failed: " + exc);
                        if (channel != null) {
                            try {
                                channel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            }

            /**
             * 服务器接受连接失败处理
             * @param exc
             * @param attachment
             */
            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("server accept failed: " + exc);
            }

        });
    }


}