package rpc.user;

import org.junit.Test;
import rpc.client.NioClient;
import rpc.client.NioServer;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-17
 * Time: 15:31
 */
public class Main {
    @Test
    public void testClient() throws Exception {
        Intf intfRpcClient = NioClient.genClient("127.0.0.1", 9800);
        for (int i = 0; i < 10000; i++) {
            String result = intfRpcClient.invoke("a", "b");
            System.out.println(result);
        }

    }

    @Test
    public void testServer() throws Exception {
        new NioServer().start();
    }
}
