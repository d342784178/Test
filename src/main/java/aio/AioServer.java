package aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Desc: ${DESC} User: DLJ Date: 2016/12/3
 */
public class AioServer {
    public AioServer() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(5555));

        serverSocketChannel.accept(null,
                new CompletionHandler<AsynchronousSocketChannel, Object>() {
                    final ByteBuffer buffer = ByteBuffer.allocate(1024);

                    public void completed(AsynchronousSocketChannel result,
                                          Object attachment) {

                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        System.out.println("failed: " + exc);
                    }
                });
        // 主线程继续自己的行为
        while (true) {
            System.out.println("main thread");
            Thread.sleep(1000);
        }


    }

    public static void main(String args[]) {
        AioServer aioServer = new AioServer();
    }
}
