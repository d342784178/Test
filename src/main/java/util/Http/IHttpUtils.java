package util.Http;

import util.GsonTools;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author DLJ
 * @Description 网络请求工具类接口
 * @date ${date} ${time}
 * ${tags}
 */
public interface IHttpUtils {


    /**
     * @param params
     * @param callBack
     */
    void post(final RequestMap params, IHttpCallBack<String>
            callBack);

    void get(final RequestMap params, IHttpCallBack<String>
            callBack);

    void put(final RequestMap params, IHttpCallBack<String>
            callBack);

    /**
     * @param targetPath 下载到本地路径
     * @param params     参数
     * @param callBack
     */
    void download(String targetPath, final RequestMap params, IHttpCallBack<File> callBack);

    /**
     * 提供由公共回调到自身框架特殊回调的转化
     * @param params
     * @param callBack
     * @return 返回自身框架所需的特殊回调接口
     */
    <T> Object generateCallBack(RequestMap params, IHttpCallBack<T> callBack);


    abstract class AbstractHttpUtils implements IHttpUtils {
        protected boolean isDebug;

        public AbstractHttpUtils(boolean isDebug) {
            this.isDebug = isDebug;
        }

        protected void printLog(RequestMap params, String result) {
            if (isDebug) {
                StringBuffer sb = new StringBuffer("");
                sb.append("请求网络时候所传递的参数++++++++++++++++++++++++++++++++++开始\n");
                sb.append(params.toString());
                sb.append("网络返回的结果++++++++++++++++++++++++++++++++++开始\n")
                        .append(result != null ? GsonTools.prettyFormat(result) : "null")
                        .append("\n")
                        .append("网络返回的结果++++++++++++++++++++++++++++++++++++++结束\n");
                System.out.println(sb.toString());
            }
        }


        @Override
        public void put(RequestMap params, IHttpCallBack<String> callBack) {

        }

        public boolean isDebug() {
            return isDebug;
        }

        public void setDebug(boolean debug) {
            isDebug = debug;
        }

        /**
         * 获取url后的queryString
         * @param queries
         */
        protected static String getQueryStr(Map<String, String> queries) {
            StringBuilder sb = new StringBuilder("");
            if (queries != null && queries.keySet().size() > 0) {
                boolean  firstFlag = true;
                Iterator iterator  = queries.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                    if (firstFlag) {
                        sb.append("?" + (String) entry.getKey() + "=" + (String) entry.getValue());
                        firstFlag = false;
                    } else {
                        sb.append("&" + (String) entry.getKey() + "=" + (String) entry.getValue());
                    }
                }
            }
            return sb.toString();
        }
    }

}
