package rpc.execute;

import com.google.common.collect.Lists;
import rpc.Impl;
import rpc.Intf;
import rpc.transport.ReqEntity;
import rpc.transport.ResEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-27
 * Time: 11:57
 */
public class RpcExecute {
    private static Map<String, Object> map = new HashMap<>();

    static {
        map.put(Intf.class.getCanonicalName(), new Impl());
    }

    public static List<ResEntity> execute(List<ReqEntity> reqEntities) throws Exception {
        ArrayList<ResEntity> resultList = Lists.newArrayList();
        for (ReqEntity reqEntity : reqEntities) {
            Object target = map.get(reqEntity.getClsName());
            for (Method method : target.getClass().getMethods()) {
                if (method.getName().equals(reqEntity.getMethodName())) {
                    Object result = method.invoke(target, reqEntity.getParam());
                    resultList.add(new ResEntity(reqEntity.getReqId(), result.getClass()
                                                                             .getCanonicalName(), (String) result));
                    break;
                }
            }
            //resultList.add(new ResEntity(reqEntity.getReqId(),String.class.getCanonicalName(),"ab"));
        }
        return resultList;
    }
}
