package 积累.util.Http;

import rx.Observable;

/**
 * Desc: 使用了rx的IHttpUtils
 * User: DLJ
 * Date: 2016-05-11
 * Time: 14:04
 */
public interface IRxHttpUtils<T> extends IHttpUtils {

    Observable<IHttpCallBack.ResponseBean> post(final RequestMap params);

    Observable<IHttpCallBack.ResponseBean> get(final RequestMap params);

    /**
     * @param targetPath 下载到本地路径
     * @param params     参数
     */
    Observable<DownloadProgress> download(String targetPath, final RequestMap params);

    /**
     * 提供由特殊回调到公共回调的转化
     *
     * @param observable 特殊回调
     * @param params     参数列表用于打印日志
     *
     * @return 公共回调
     */
    Observable<IHttpCallBack.ResponseBean> getResponseBeanObserverable(Observable<T> observable,
                                                                       RequestMap params);

    class DownloadProgress {
        public long   totalLength;
        public long   currentLength;
        public int    currentPrecent;
        public String url;

        public DownloadProgress(long length) {
            this.totalLength = length;
        }

        public DownloadProgress(long totalLength, String target) {
            this.totalLength = totalLength;
            this.url = target;
        }
    }
}
