package rpc.client;

import rpc.transport.execute.RpcExecute;
import rpc.transport.base.IServer;
import rpc.transport.base.ReadCallBack;
import rpc.transport.base.Server;
import rpc.transport.context.Context;
import rpc.transport.dto.ReqEntity;
import rpc.transport.dto.ResEntity;

import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 17:55
 */
public class NioServer implements IServer, ReadCallBack<ReqEntity> {

    private Server server;

    @Override
    public void start() throws Exception {
        server = new Server(9800, this);
        server.init();
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public void onRead(Context context, List<ReqEntity> resEntityList) {
        try {
            List<ResEntity> result = RpcExecute.execute(resEntityList);
            server.write(context, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
