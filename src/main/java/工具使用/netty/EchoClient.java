package 工具使用.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import 积累.util.GsonTools;

import java.net.InetSocketAddress;
import java.util.List;

public class EchoClient {
    private final String host;

    private final int port;

    public EchoClient() {
        this(0);
    }

    public EchoClient(int port) {
        this("localhost", port);
    }

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group) // 注册线程池
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(this.host, this.port)) // 绑定连接端口和host信息
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));//回车分割
//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("&&"
//                                    .getBytes())));//指定符号分割
//                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));//定长分割
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4));//指定长度
                            ch.pipeline().addLast(new StringDecoder() {//过滤前4个字节的长度标志位
                                @Override
                                protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
                                        throws Exception {
                                    super.decode(ctx, msg.copy(4,msg.readableBytes()-4), out);
                                }
                            });
                            ch.pipeline().addLast(new EchoClientHandler());


                            ch.pipeline().addFirst(new MessageToMessageEncoder<String>() {

                                @Override
                                protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out)
                                        throws Exception {
                                    byte[] bytes = msg.getBytes();
                                    out.add(Unpooled.copiedBuffer(bytes));
                                }
                            });
                            ch.pipeline().addFirst(new MessageToMessageEncoder<AADTO>() {
                                @Override
                                protected void encode(ChannelHandlerContext ctx, AADTO msg, List<Object> out) throws
                                        Exception {
                                    String s = GsonTools.beanToJson(msg);
                                    out.add(Unpooled.copiedBuffer(s.getBytes()));
                                }
                            });
                            ch.pipeline().addFirst(new LengthFieldPrepender(4));
                        }
                    });

            ChannelFuture cf = b.connect().sync(); // 异步连接服务器

            cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("127.0.0.1", 65535).start(); // 连接127.0.0.1/65535，并启动
    }
}