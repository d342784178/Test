package 工具使用.es;

import com.google.common.base.Function;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-02
 * Time: 14:30
 */
public class Scroller<T> {
    private String            scrollerId;
    private T                 result;
    private ScrollerHelper<T> helper;

    public Scroller(String scrollerId, T result, Long scrollKeeplive, Function<SearchResponse, T> resultTranformFunc) {
        this.scrollerId = scrollerId;
        this.result = result;
        helper = new ScrollerHelper<>(generateFunc2(scrollKeeplive, resultTranformFunc));
    }

    private <T> Func<String, Scroller<T>> generateFunc2(long scrollKeepalive,
                                                        Function<SearchResponse, T> resultTranformFunc) {
        return new Func<String, Scroller<T>>() {
            @Override
            public Scroller<T> call(String scrollerId, TransportClient client) {
                SearchResponse sr = client.prepareSearchScroll(scrollerId)
                                          .setScroll(new TimeValue(scrollKeepalive))//在指定时间内执行scroll操作
                                          .execute()
                                          .actionGet();
                String newScrollerId = sr.getScrollId();
                T      result        = resultTranformFunc.apply(sr);
                return result != null ? new Scroller<T>(newScrollerId, result, scrollKeepalive, resultTranformFunc) :
                        null;
            }
        };
    }

    public T getResult() {
        return result;
    }

    public ScrollerHelper<T> helper() {
        return helper;
    }

    public static class ScrollerHelper<T> {

        private Func<String, Scroller<T>> func;

        private ScrollerHelper(Func<String, Scroller<T>> func) {
            this.func = func;
        }

        /**
         * @return 当读取完成时 返回null
         * 返回新的ScrollerHelper 结果存在ScrollerHelper.result中
         */
        public Scroller<T> next(Scroller<T> scroller, TransportClient client) {
            return func.invoke(scroller.scrollerId, client);
        }

    }

    public static abstract class Func<T, F> {
        public abstract F call(final T t, TransportClient client);

        public F invoke(T t, TransportClient client) {
            return call(t, client);
        }

    }
}
