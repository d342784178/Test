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
public class ClientContext extends Context<ResEntity, ReqEntity> {
    private MessageEncoder messageEncoder;

    public ClientContext(SelectionKey selectionKey) {
        super(selectionKey);
        this.messageEncoder = new MessageEncoder();
    }

    @Override
    public List<ResEntity> read() throws IOException {
        return messageEncoder.decodeRes(this);

    }


}
