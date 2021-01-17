package rpc.transport.base;

import rpc.transport.context.Context;
import rpc.transport.context.ServerContext;
import rpc.transport.dto.ReqEntity;
import rpc.transport.dto.ResEntity;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * @link http://www.infoq.com/cn/articles/netty-threading-model
 * 注意: reactor线程模型不包括业务处理 只包含tcp连接(connect),读写(r/w)过程的处理
 * Desc: reactor单线程
 * 1. 所有事件处理在同一个线程(不包括业务处理) 一个selector
 * Author: DLJ
 * Date:
 */
public class Server {

    private ServerSocketChannel     channel;
    private int                     port;
    private ReadCallBack<ReqEntity> readCallBack;


    public Server(int port) {
        this.port = port;
    }


    public Server(int port, ReadCallBack readCallBack) {
        this.port = port;
        this.readCallBack = readCallBack;
    }

    public void init() throws Exception {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(9800));
        Selector acceptorSelector = Selector.open();
        Selector rwSelector       = Selector.open();
        //非阻塞式
        serverSocket.configureBlocking(false);
        serverSocket.register(acceptorSelector, SelectionKey.OP_ACCEPT);

        while (true) {
            int acceptEventCount = acceptorSelector.selectNow();
            if (acceptEventCount > 0) {
                for (SelectionKey selectedKey : acceptorSelector.selectedKeys()) {
                    SocketChannel socketChannel = serverSocket.accept();
                    socketChannel.configureBlocking(false);
                    SelectionKey credentials = socketChannel.register(rwSelector, SelectionKey.OP_READ);
                    credentials.attach(new ServerContext(credentials));
                    System.out.println("client accept");
                }
            }

            int rwEventCount = rwSelector.select(1);
            if (rwEventCount > 0) {
                for (SelectionKey selectedKey : rwSelector.selectedKeys()) {
                    ServerContext context       = (ServerContext) selectedKey.attachment();
                    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
                    if (selectedKey.isReadable()) {
                        List<ReqEntity> command = context.read();
                        if (command != null) {
                            readCallBack.onRead(context, command);
                        }
                    }
                    if (selectedKey.isWritable()) {
                        context.flushWrite();
                        socketChannel.register(rwSelector, SelectionKey.OP_READ, context);
                    }
                }
            }
        }
    }

    public void write(Context context, List<ResEntity> resEntityList) {
        context.write(resEntityList);
        context.getSelectionKey().interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
}
