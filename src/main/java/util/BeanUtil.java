package util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

    public static void transMap2Bean2(Map<String, Object> map, Object obj) throws InvocationTargetException,
            IllegalAccessException {
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.copyProperties(obj, map);
    }

    public static void transMap2Bean(Map<String, Object> map, Object obj) throws Exception {
        BeanInfo             beanInfo            = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value  = map.get(key);
                Method setter = property.getWriteMethod();
                setter.invoke(obj, new Object[]{value});
            }
        }
    }

    public static Map<String, Object> transBean2Map(Object obj) throws Exception {
        Map                  map                 = new HashMap();
        BeanInfo             beanInfo            = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            if (!"class".equals(key)) {
                Method getter = property.getReadMethod();
                Object value  = getter.invoke(obj, new Object[0]);
                map.put(key, value);
            }
        }
        return map;
    }

    public static void main(String[] args) {
    }
}
