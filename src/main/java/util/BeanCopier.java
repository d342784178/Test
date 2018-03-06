package util;


import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 效率很高的bean拷贝工具类
 *
 * @author pengyuming
 */
public class BeanCopier {

    private static final Logger logger = LoggerFactory.getLogger(BeanCopier.class);

    private static final Map<String, net.sf.cglib.beans.BeanCopier> beanCopierMap = new HashMap<>();


    public static <T> T copyWithSelect(Object orig, PropSelectConvert<T> converter) {
        try {
            Class<T> destCls = converter.getDest();
            T t = destCls.newInstance();
            copy(orig, t, converter);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T copy(Object orig, Class<T> destCls) {
        return copy(orig, destCls, null);
    }

    public static <T> T copy(Object orig, Class<T> destCls, Converter converter) {
        try {
            T t = destCls.newInstance();
            copy(orig, t, null);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * bean属性拷贝
     *
     * @param orig
     * @param dest
     */
    public static void copy(Object orig, Object dest) {
        copy(orig, dest, null);
    }


    /**
     * bean属性拷贝，并可以自定义converter
     *
     * @param orig
     * @param dest
     * @param converter
     */
    public static void copy(Object orig, Object dest, Converter converter) {
        String key = genKey(orig.getClass(), dest.getClass());
        boolean useConverter = converter != null ? true : false;
        net.sf.cglib.beans.BeanCopier copier = null;
        if (!beanCopierMap.containsKey(key)) {
            copier = net.sf.cglib.beans.BeanCopier.create(orig.getClass(), dest.getClass(), useConverter);
        } else {
            copier = beanCopierMap.get(key);
        }
        copier.copy(orig, dest, converter);
    }

    private static String genKey(Class<?> origClazz, Class<?> destClazz) {
        return origClazz.getName() + destClazz.getName();
    }

    /**
     * 将from转换成指定类的实例
     *
     * @param from
     * @param toClazz
     * @return
     */
    public static <E> E transform(Object from, Class<E> toClazz) {
        return transform(from, toClazz, null);
    }

    /**
     * 将from转换成指定类的实例，可以自定义converter
     *
     * @param from
     * @param toClazz
     * @param converter
     * @return
     */
    public static <E> E transform(Object from, Class<E> toClazz, Converter converter) {
        try {
            if (from == null) {
                return null;
            }
            E to = toClazz.newInstance();
            copy(from, to, converter);
            return to;
        } catch (Exception e) {
            logger.error("transform error", e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 列表的转换
     *
     * @param fromList
     * @param toClazz
     * @return
     */
    public static <E> List<E> transform(List<?> fromList, Class<E> toClazz) {
        return transform(fromList, toClazz, null);
    }

    /**
     * 列表的转换，可以自定义converter
     *
     * @param fromList
     * @param toClazz
     * @param converter
     * @return
     */
    public static <E> List<E> transform(List<?> fromList, Class<E> toClazz, Converter converter) {
        if (fromList == null) {
            return null;
        }
        List<E> toList = new ArrayList<E>();
        for (Object from : fromList) {
            E to = transform(from, toClazz, converter);
            toList.add(to);
        }
        return toList;
    }

}
