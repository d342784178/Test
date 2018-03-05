package util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {
    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     * @param clazz The class to introspect
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     * @param clazz clazz The class to introspect
     * @param index 注解index
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 通过反射,获得定义Class时声明的接口的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     * @param clazz clazz The class to introspect
     * @param index  接口index
     * @param index2 注解index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static Class getInterfaceGenricType(Class clazz, int index, int index2) throws IndexOutOfBoundsException {

        Type   genType = clazz.getGenericInterfaces()[index];
        Type[] params  = ((ParameterizedType) genType).getActualTypeArguments();

        if (index2 >= params.length || index2 < 0) {
            return Object.class;
        }
        if (!(params[index2] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index2];
    }
}    
