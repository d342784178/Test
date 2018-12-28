package rpc.transport;

import io.netty.buffer.ByteBuf;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 17:45
 */
public class ResEntity {
    private String reqId;
    private String resType;
    private String res;

    public ResEntity(String reqId, String resType, String res) {
        this.reqId = reqId;
        this.resType = resType;
        this.res = res;
    }

    public String getReqId() {
        return reqId;
    }

    public ResEntity setReqId(String reqId) {
        this.reqId = reqId;
        return this;
    }

    public String getResType() {
        return resType;
    }

    public ResEntity setResType(String resType) {
        this.resType = resType;
        return this;
    }

    public String getRes() {
        return res;
    }

    public ResEntity setRes(String res) {
        this.res = res;
        return this;
    }

    public static ResEntity read(ByteBuf readBuf, int reqIdLength, int resTypeLength, int resLength) {
        byte[] temp = new byte[1024];
        readBuf.readBytes(temp);
        String reqId = new String(temp, 0, reqIdLength, NioClient.UTF8);
        readBuf.readBytes(temp);
        String resType = new String(temp, 0, resTypeLength, NioClient.UTF8);
        readBuf.readBytes(temp);
        String res = new String(temp, 0, resLength, NioClient.UTF8);
        return new ResEntity(reqId, resType, res);
    }
}
