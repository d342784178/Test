package rpc.transport.context;

import rpc.transport.dto.ReqEntity;
import rpc.transport.dto.ResEntity;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-27
 * Time: 12:58
 */
public class ServerContext extends Context<ReqEntity, ResEntity> {
    private MessageEncoder messageEncoder;


    public ServerContext(SelectionKey selectionKey) {
        super(selectionKey);
        this.messageEncoder = new MessageEncoder();
    }

    @Override
    public List<ReqEntity> read() throws IOException {
        return messageEncoder.decodeReq(this);

    }


}
