package rpc.transport;

import java.util.List;

public interface ReadCallBack<T> {

    default void onRead(Context context, List<T> resEntityList) {
    }

}