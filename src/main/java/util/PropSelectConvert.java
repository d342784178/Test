package util;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.sf.cglib.core.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Desc: 属性选择器
 * 测试用例见 com.iflytek.ossp.mobilehall.common.utils.PropSelectConvertTest
 * 注意:目标类不能使用lombok的@ToString注解 !!
 * Author: ljdong2
 * Date: 2018-03-06
 * Time: 11:22
 */
@SuppressWarnings("uncheck")
public class PropSelectConvert<T> implements Converter {
    private static String FIELD_SELECTED = "FIELD_SELECTED";

    @Getter
    @Setter
    private List<String> list = Lists.newArrayList();
    @Getter
    @Setter
    private Class<T> dest;

    private PropSelectConvert() {
    }

    public static <T> PropSelectConvert<T> getConvert(SelectFunction<T> dest) {
        Class<T> cls = GenericsUtils.getInterfaceGenricType(dest.getClass(), 0, 0);
        T warp = warp(cls);
        dest.select(warp);
        try {
            Field propertiesSelecteds = warp.getClass().getDeclaredField(FIELD_SELECTED);
            ArrayList<String> o = (ArrayList<String>) propertiesSelecteds.get(warp);//获取选择属性
            PropSelectConvert convert = new PropSelectConvert();
            convert.getList().addAll(o);
            convert.setDest(cls);
            return convert;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过bytebuddy生成选择器
     *
     * @return
     */
    private static <T> T warp(Class<T> cls) {
        try {
            ByteBuddy byteBuddy = new ByteBuddy();
            T get = byteBuddy.subclass(cls)
                    .name("assadfsddf")
                    .defineField(FIELD_SELECTED, ArrayList.class, Visibility.PUBLIC)
                    .method(ElementMatchers.nameStartsWith("get"))
                    .intercept(MethodDelegation.to(PropertiesSelectorIntercept.class))
                    .make()
                    .load(PropSelectConvert.class.getClassLoader())
                    .getLoaded().newInstance();
            return get;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param sourceValue source对象属性值
     * @param targetClass target对象对应类
     * @param methodName  targetClass里属性对应set方法名,eg.setId
     * @return
     */
    @Override
    public Object convert(Object sourceValue, Class targetClass, Object methodName) {
        //System.out.println(value);
        //System.out.println(target);
        //System.out.println(context);
        String methodNameStr = (String) methodName;
        if (list.contains(methodNameStr.substring(3, methodNameStr.length()))) {
            return sourceValue;
        } else {
            return null;
        }
    }


    /**
     * 选择器接口
     */
    interface SelectFunction<T> {
        void select(T t);
    }


    public static class PropertiesSelectorIntercept {
        @RuntimeType
        public static <T> T invoke(@Origin Method method, @SuperCall Callable<T> callable, @Super Object obj) throws Exception {
            String name = method.getName();
            String fieldName = name.substring(3, name.length());
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            declaredFields[0].setAccessible(true);
            Object oo = declaredFields[0].get(obj);
            Field propertiesSelected = oo.getClass().getDeclaredField("FIELD_SELECTED");
            ArrayList<Object> list = null;
            Object o = propertiesSelected.get(oo);
            if (o == null) {
                list = Lists.newArrayList();
                propertiesSelected.set(oo, list);
            } else {
                list = (ArrayList<Object>) o;
            }
            list.add(fieldName);
            return null;
        }
    }

}
