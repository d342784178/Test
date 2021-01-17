package rpc.transport.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import rpc.client.NioClient;

import java.nio.ByteBuffer;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 17:45
 */
public class ResEntity implements IOutPut {
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

    @Override
    public void outPut(ByteBuffer buffer) {
        byte[] reqIdBytes   = reqId.getBytes(NioClient.UTF8);
        byte[] resTypeBytes = resType.getBytes(NioClient.UTF8);
        byte[] resBytes     = res.getBytes();

        buffer.putInt(reqIdBytes.length);
        buffer.putInt(resTypeBytes.length);
        buffer.putInt(resBytes.length);

        buffer.put(reqIdBytes);
        buffer.put(resTypeBytes);
        buffer.put(resBytes);
    }

    public static ResEntity read(ByteBuffer readBuf, int reqIdLength, int resTypeLength, int resLength) {
        byte[] array   = readBuf.array();
        String reqId   = new String(array, readBuf.position(), reqIdLength, NioClient.UTF8);
        String resType = new String(array, readBuf.position() + reqIdLength, resTypeLength, NioClient.UTF8);
        String res = new String(array, readBuf.position() + reqIdLength + resTypeLength, resLength,
                NioClient.UTF8);
        //移动读指针
        readBuf.position(readBuf.position() + reqIdLength + resTypeLength + resLength);
        return new ResEntity(reqId, resType, res);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("reqId", reqId)
                .append("resType", resType)
                .append("res", res)
                .toString();
    }
}
