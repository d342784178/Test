package rpc.transport;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.ByteBuffer;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 17:39
 */
public class ReqEntity implements IOutPut {
    private String   reqId;
    private String   clsName;
    private String   methodName;
    private Object[] param;

    public ReqEntity(String reqId, String clsName, String methodName, Object[] param) {
        this.reqId = reqId;
        this.clsName = clsName;
        this.methodName = methodName;
        this.param = param;
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

    public Object[] getParam() {
        return param;
    }

    public ReqEntity setParam(Object[] param) {
        this.param = param;
        return this;
    }

    @Override
    public void outPut(ByteBuffer buffer) {
        byte[] reqIdBytes         = reqId.getBytes(NioClient.UTF8);
        byte[] canonicalNameBytes = clsName.getBytes(NioClient.UTF8);
        byte[] methodNameBytes    = methodName.getBytes(NioClient.UTF8);
        byte[] paramBytes         = JSON.toJSONString(param).getBytes(NioClient.UTF8);

        buffer.putInt(reqIdBytes.length);
        buffer.putInt(canonicalNameBytes.length);
        buffer.putInt(methodNameBytes.length);
        buffer.putInt(paramBytes.length);

        buffer.put(reqIdBytes);
        buffer.put(canonicalNameBytes);
        buffer.put(methodNameBytes);
        buffer.put(paramBytes);
    }

    public static ReqEntity read(ByteBuffer readBuf, int reqIdLength, int clsNameLength, int methodNameLength,
                                 int paramLength) {
        byte[] array   = readBuf.array();
        String reqId   = new String(array, readBuf.position(), reqIdLength, NioClient.UTF8);
        String clsName = new String(array, readBuf.position() + reqIdLength, clsNameLength, NioClient.UTF8);
        String methodName = new String(array, readBuf.position() + reqIdLength + clsNameLength, methodNameLength,
                NioClient.UTF8);
        String params = new String(array, readBuf.position() + reqIdLength + clsNameLength + methodNameLength,
                paramLength, NioClient.UTF8);
        //移动读指针
        readBuf.position(readBuf.position() + reqIdLength + clsNameLength + methodNameLength + paramLength);
        return new ReqEntity(reqId, clsName, methodName, JSON.parseArray(params).toArray());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("reqId", reqId)
                .append("clsName", clsName)
                .append("methodName", methodName)
                .append("param", param)
                .toString();
    }
}
