package util.Http;


import util.Http.annnotation.*;
import util.Http.okhttp.RxConvertOKhttpUtil3_2_0;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;

/**
 * Desc:
 * User: DLJ
 * Date: 2016-09-11
 * Time: 22:12
 */
public class NetHelper {

    private static HashMap<String, Object> proxys = new HashMap<>();

    private static String getUrl(String route) {
        return route;
    }

    public static <T> T build(Class<T> clazz) {
        if (clazz.isInterface()) {
            Object object = proxys.get(clazz.getCanonicalName());
            if (object == null) {
                object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new
                        InvocationHandler() {
                            @Override
                            public Object invoke(Object o, Method method, Object[] objects) throws
                                    Throwable {
                                boolean      isPost      = false;
                                boolean      isGet       = false;
                                RequestMap   requestMap  = new RequestMap();
                                Annotation[] annotations = method.getAnnotations();
                                for (Annotation annotation : annotations) {
                                    if (annotation instanceof POST) {
                                        POST post = (POST) annotation;
                                        isPost = true;
                                        requestMap.putUrl(getUrl(post.value()));
                                    } else if (annotation instanceof GET) {
                                        GET get = (GET) annotation;
                                        isGet = true;
                                        requestMap.putUrl(getUrl(get.value()));
                                    }
                                }
                                getParam(method, objects, requestMap);
                                Class returnType        = null;
                                Type  genericReturnType = method.getGenericReturnType();
                                if (genericReturnType instanceof ParameterizedType) {
                                    Type[] types = ((ParameterizedType) genericReturnType)
                                            .getActualTypeArguments();
                                    for (Type type : types) {
                                        returnType = (Class) type;
                                    }
                                }
                                if (isPost && !isGet) {//post
                                    return RxConvertOKhttpUtil3_2_0.getInstance(true)
                                            .post(requestMap, returnType);
                                } else if (!isPost && isGet) {//get
                                    return RxConvertOKhttpUtil3_2_0.getInstance(true)
                                            .get(requestMap, returnType);
                                } else {
                                    throw new IllegalArgumentException("到底是post还是get啊?!!!");
                                }
                            }
                        });
                proxys.put(clazz.getCanonicalName(), object);
            }
            return (T) object;
        } else {
            throw new IllegalArgumentException("clazz is not run net-interface");
        }
    }

    public static RequestMap getRequestMap() {
        return null;
    }

    private static void getParam(Method method, Object[] objects, RequestMap requestMap) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    requestMap.putForm(param.value(), (String) objects[i]);
                } else if (annotation instanceof Header) {
                    Header header = (Header) annotation;
                    requestMap.putHeader(header.value(), (String) objects[i]);
                } else if (annotation instanceof QueryStr) {
                    QueryStr queryStr = (QueryStr) annotation;
                    requestMap.putQuery(queryStr.value(), (String) objects[i]);
                }
            }
        }
    }
}
