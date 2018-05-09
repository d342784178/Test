package 工具使用.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[]  bytes  = "客户端已连接".getBytes();
        ByteBuf buffer = Unpooled.buffer(bytes.length);
        buffer.writeBytes(bytes);
        ctx.writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead;" + msg);
        String  msgStr = (String) msg;
        ByteBuf buf    = Unpooled.copiedBuffer(msgStr.getBytes());
        byte[]  bytes  = "服务端已接收消息:".getBytes();
        ByteBuf buffer = Unpooled.buffer(bytes.length + buf.writerIndex());
        buffer.writeBytes(bytes);
        buffer.writeBytes(buf);
        buffer.retain();
        ctx.writeAndFlush(buffer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channelReadComplete..");
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        //ctx.flush(); // 第二种方法：在client端关闭channel连接，这样的话，会触发两次channelReadComplete方法。
        //ctx.flush().close().sync(); // 第三种：改成这种写法也可以，但是这中写法，没有第一种方法的好。
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server exceptionCaught:" + cause.getMessage());
        cause.printStackTrace();
        ctx.close(); // 关闭发生异常的连接
    }
}