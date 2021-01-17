package rpc.transport.base;

import rpc.transport.context.Context;

import java.util.List;

public interface ReadCallBack<T> {

    default void onRead(Context context, List<T> resEntityList) {
    }

}