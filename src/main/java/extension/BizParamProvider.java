package extension;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-03-02
 * Time: 18:01
 */
public interface BizParamProvider<T,R> {
    R getBizParam(T t);
}
