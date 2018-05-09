package 积累.util.Http;

import java.util.LinkedHashMap;

public interface IParamBuilder<T> {
    T build();

    /**
     * 当网络请求框架有多个请求体类型的时候 可使用该类 作为临时请求对象 自行转化
     * 如:xutils2.6.7 只有一个RequestParam此时则无需使用该类
     * 而okhttp针对不同表单有不同的类 此时需要使用该类来返回一个唯一对象
     * @param <T>
     */
    abstract class DefaultParamBuilder<T> implements IParamBuilder<T> {
        public LinkedHashMap<String, String>                    headers;
        public LinkedHashMap<String, String>                    form;
        public LinkedHashMap<String, String>                    queryStrs;
        public LinkedHashMap<String, RequestMap.UploadFileInfo> files;
        public String                                           json;

        public DefaultParamBuilder(RequestMap requestMap1) {
            headers = requestMap1.getHeaders();
            form = requestMap1.getForms();
            json = requestMap1.getJson();
            files = requestMap1.getFiles();
            queryStrs = requestMap1.getQueryStrs();
        }
    }
}
