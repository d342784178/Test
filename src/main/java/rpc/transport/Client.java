package rpc.transport;

import com.google.common.collect.Lists;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Desc: nio客户端
 * 1.writeable事件 不要一直订阅 因为当socket缓冲区可写入时就会触发该事件
 * 2.
 * Author: ljdong2
 * Date: 2018/8/31
 */
public class Client {
    private SocketChannel           channel;
    private int                     port;
    private String                  host;
    private ReadCallBack<ResEntity> readCallBack;
    private ClientContext           context;


    public Client(int port) {
        this.port = port;
    }

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public Client(String host, int port, ReadCallBack<ResEntity> readCallBack) {
        this.port = port;
        this.host = host;
        this.readCallBack = readCallBack;
    }

    public void init() throws Exception {
        channel = SocketChannel.open();
        Selector rwSelector = Selector.open();
        //阻塞式连接server
        channel.connect(new InetSocketAddress(host, port));
        //server连接成功转为非阻塞式
        channel.configureBlocking(false);
        SelectionKey selectionKey = channel.register(rwSelector, SelectionKey.OP_CONNECT);
        context = new ClientContext(selectionKey);
        while (true) {
            int rwEventCount = rwSelector.select(1);
            if (rwEventCount > 0) {
                for (SelectionKey selectedKey : rwSelector.selectedKeys()) {
                    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
                    if (selectedKey.isReadable()) {
                        List<ResEntity> resultList = context.read();
                        if (resultList != null) {
                            readCallBack.onRead(context, resultList);
                        }
                    }
                    if (selectedKey.isWritable()) {
                        context.flushWrite();
                        socketChannel.register(rwSelector, SelectionKey.OP_READ);
                    }
                }
            }
        }
    }

    public void write(ReqEntity reqEntity) {
        context.write(Lists.newArrayList(reqEntity));
        context.getSelectionKey().interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }


}
