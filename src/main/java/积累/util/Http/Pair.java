package 积累.util.Http;

/**
 * @desc TODO
 * @auth DLJ
 * @createDate 2017/7/12
 */
public class Pair<T, F> {
    public T key;

    public F value;

    public Pair(T key, F value) {
        this.key = key;
        this.value = value;
    }

    public static <T, F> Pair<T, F> of(T key, F value) {
        return new Pair<T, F>(key, value);
    }
}
