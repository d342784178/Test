package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import util.encryp.MD5Util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Desc: http请求 加解密
 * Author: DLJ
 * Date: 2016-12-30
 * Time: 09:07
 */
public class SignUtil {
    private static final Logger log = Logger.getLogger(SignUtil.class);

    private static Map<String, String> transBean2Map(Object obj)
            throws Exception {
        Map                  map                 = new HashMap();
        BeanInfo             beanInfo            = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!"class".equals(key)) {
                Method getter = property.getReadMethod();
                if (getter != null) {
                    Object value = getter.invoke(obj, new Object[0]);
                    if (value != null) {
                        map.put(key, value.toString());
                    }
                }
            }
        }
        return map;
    }

    /**
     * 先排序再拼接
     * @param sortedParams
     * @return
     */
    private static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys    = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key   = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.isNoneEmpty(key, value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * 加密
     * 1  所有参数按 a-z 排序以&拼接为 QueryString 形式待签名字符串
     * 2  把私钥直接拼接到待签名字符串后面,形成新的字符串,
     * 3  利用MD5 的签名函数对这个新的字符串进行签名运算
     * 4  并将签名结果字符串全部字母转换为大写字母。
     * 5  参数进行UrlEncode
     * @Return
     * @Auther xingyu.lu
     * @Date 16/12/20 10:32
     */
    public static Map<String, String> sign(Map<String, String> map, String signKey) {
        String preSignStr = null;
        if (map != null) {
            //1
            preSignStr = getSignContent(map);
        }
        String sign = null;
        if (preSignStr != null) {
            //2 3 4
            sign = Signature(preSignStr, signKey);
        }
        if (sign != null) {
            map.put("sign", sign);
            try {
                //5
                map = encodingParam(map, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("encodingParam{}", e);
                return new HashMap<>();
            }
        }
        return map;
    }

    /**
     * 验证签名
     * 1.获取加密字段
     * 2.字典排序
     * 1.urldecode
     * 2.验证MD5
     * @param map     参数
     * @param signKey 私钥
     * @return 返回原始参数
     * @exception Exception
     */
    public static Map<String, String> checkSign(Map<String, String> map, String signKey) throws Exception {
        LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
        //加密字段
        String signStr = map.remove("sign");

        //urldecode 获取待签名字符串
        StringBuffer      sb   = new StringBuffer("");
        ArrayList<String> keys = new ArrayList<>(map.keySet());
        //字典排序
        Collections.sort(keys);
        int index = 0;
        for (String key : keys) {
            String decodeValue = URLDecoder.decode(map.get(key), "utf-8");
            sb.append((index == 0 ? "" : "&") + key + "=" + decodeValue);
            if (index + 1 == keys.size()) {
                decodeValue = org.apache.commons.lang3.StringUtils.removeEnd(decodeValue, signKey);
            }
            newMap.put(key, decodeValue);
            index++;
        }
        //待签名字符串(参数的queryString + signKey)
        String  s               = sb.toString();
        boolean endsWithSignKey = s.endsWith(signKey);
        //签名验证是否相等
        boolean md5Equals = MD5Util.MD5UpCase(s).equals(signStr);
        if (endsWithSignKey && md5Equals) {
            return newMap;
        } else {
            throw new IllegalArgumentException("签名验证失败");
        }
    }

    public static Map<String, String> sign(Object obj, String signKey) {
        try {
            return sign(transBean2Map(obj), signKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("请求参数解析失败");
        }
    }

    /**
     * @Describe 对参数进行Encode
     * @Param
     * @Return
     * @Auther xingyu.lu
     * @Date 16/12/19 20:11
     */
    private static Map encodingParam(Map<String, String> preEncodingMap, String charSet)
            throws UnsupportedEncodingException {
        Map<String, String> enCodedMap = new HashMap<>();
        List<String>        keys       = new ArrayList<String>(preEncodingMap.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key   = keys.get(i);
            String value = URLEncoder.encode(preEncodingMap.get(key), charSet);
            enCodedMap.put(key, value);
        }
        return enCodedMap;
    }

    /**
     * @Describe 签名
     * @Auther xingyu.lu
     * @Date 16/12/19 17:37
     */
    private static String Signature(String preSignString, String key) {
        return MD5Util.MD5UpCase(preSignString + key);
    }
}
