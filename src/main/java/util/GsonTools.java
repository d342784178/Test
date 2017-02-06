package util;


import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonTools {

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    public static final TypeAdapter<String> StringTypeAdapter = new TypeAdapter<String>() {
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return "";//原先是返回Null，这里改为返回空字符串
                }
                return reader.nextString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private static Gson gson;

    static {
        GsonBuilder gsonBulder = new GsonBuilder();
//        gsonBulder.registerTypeAdapter(String.class, StringTypeAdapter);   //所有String类型null替换为字符串“”
//        gsonBulder.registerTypeAdapter(int.class, INTEGER); //int类型对float做兼容

        //通过反射获取instanceCreators属性
        try {
            Class builder = (Class) gsonBulder.getClass();
            Field f       = builder.getDeclaredField("instanceCreators");
            f.setAccessible(true);
            Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBulder);//得到此属性的值
            //注册数组的处理器
            gsonBulder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(new
                    ConstructorConstructor(val)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        gson = gsonBulder.create();
    }

    /**
     * 只导出有标注的
     * @param list
     * @return
     */
    public static String listToJsonByAnnotation(List list) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String s = gson.toJson(list);
        return s;
    }

    public static String listToJson(List list) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .create();
        String s = gson.toJson(list);
        return s;
    }

    public static String mapToJson(HashMap map) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .create();
        String s = gson.toJson(map);
        return s;
    }


    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        T t = GsonTools.gson.fromJson(gsonString, cls);
        return t;
    }

    /**
     * 支持泛型
     * @param gsonString
     * @param type
     * @return
     */
    public static <T> T jsonToBean(String gsonString, Type type) {
        T t = GsonTools.gson.fromJson(gsonString, type);
        return t;
    }

    public static <T> List<T> jsonToList(String gsonString, final Class<T> cls) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjs = new Gson().fromJson(gsonString, type);

        ArrayList<T> listOfT = new ArrayList<>();
        for (JsonObject jsonObj : jsonObjs) {
            listOfT.add(new Gson().fromJson(jsonObj, cls));
        }

        return listOfT;
    }


    public static <T> List<Map<String, T>> jsonToListMaps(
            String gsonString) {
        List<Map<String, T>> list = null;
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    public static <T> Map<String, T> jsonToMaps(String gsonString) {
        Map<String, T> map = null;
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    public static String beanToJsonWithNullProperties(Object object) {
        if (object == null) {
            return "";
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(object);
    }

    public static String beanToJson(Object object) {
        if (object == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * 格式化输出
     * @param uglyJSONString
     * @return
     */
    public static String prettyFormat(String uglyJSONString) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .create();
            JsonParser  jp = new JsonParser();
            JsonElement je = jp.parse(uglyJSONString);
            return gson.toJson(je);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return uglyJSONString;
        }
    }

}
