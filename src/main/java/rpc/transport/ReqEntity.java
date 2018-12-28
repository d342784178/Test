package rpc.transport;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 17:39
 */
public class ReqEntity {
    private String reqId;
    private String clsName;
    private String methodName;
    private Object param;

    public ReqEntity(String reqId, String clsName, String methodName, Object param) {
        this.reqId = reqId;
        this.clsName = clsName;
        this.methodName = methodName;
        this.param = param;
    }

    public void outPut(ByteBuf buffer) {
        byte[] reqIdBytes         = reqId.getBytes(NioClient.UTF8);
        byte[] methodNameBytes    = methodName.getBytes(NioClient.UTF8);
        byte[] canonicalNameBytes = clsName.getBytes(NioClient.UTF8);
        byte[] paramBytes         = JSON.toJSONString(param).getBytes(NioClient.UTF8);

        buffer.writeByte(reqIdBytes.length);
        buffer.writeByte(methodNameBytes.length);
        buffer.writeByte(canonicalNameBytes.length);
        buffer.writeByte(paramBytes.length);

        buffer.writeBytes(reqIdBytes);
        buffer.writeBytes(methodNameBytes);
        buffer.writeBytes(canonicalNameBytes);
        buffer.writeBytes(paramBytes);
    }

    public String getReqId() {
        return reqId;
    }

    public ReqEntity setReqId(String reqId) {
        this.reqId = reqId;
        return this;
    }

    public String getClsName() {
        return clsName;
    }

    public ReqEntity setClsName(String clsName) {
        this.clsName = clsName;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ReqEntity setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Object getParam() {
        return param;
    }

    public ReqEntity setParam(Object param) {
        this.param = param;
        return this;
    }
}
