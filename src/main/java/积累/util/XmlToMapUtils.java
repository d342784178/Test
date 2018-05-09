package 积累.util; /**
 * Created by lxy on 16/9/10.
 */

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * @author
 * @create 2016-09-10 11:45
 **/
public class XmlToMapUtils {
    /**
     * map转xml
     * 只支持填充content不支持属性
     * @param map
     * @param rootStr
     * @return
     * @exception Exception
     */
    public static String toXml(Map<String, Object> map, String rootStr) throws Exception {
        Element  root     = DocumentHelper.createElement(rootStr);
        Document document = DocumentHelper.createDocument(root);
        getElement(map, root);


        //把生成的xml文档存放在硬盘上  true代表是否换行
        XMLWriter xmlWriter = new XMLWriter(new ByteArrayOutputStream());
        xmlWriter.write(document);
        xmlWriter.close();
        return document.asXML();
    }

    private static void getElement(Map<String, Object> map, Element root) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String  key     = entry.getKey();
            Object  value   = entry.getValue();
            Element element = root.addElement(key);
            if (value instanceof Map) {
                getElement((Map<String, Object>) value, element);
            } else {
                element.addText(value.toString());
            }

        }
    }

    public static Map<String, Object> toMap(Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null) {
            return map;
        }
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            //System.out.println(e.getName());
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), toMap(e));
            } else {
                map.put(e.getName(), e.getText());
            }
        }
        return map;
    }

    public static Map<String, Object> toMap(Element e) {
        Map  map  = new HashMap<String, Object>();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter    = (Element) list.get(i);
                List    mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = toMap(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }
}
