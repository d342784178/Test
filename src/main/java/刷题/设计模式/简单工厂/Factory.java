package 刷题.设计模式.简单工厂;

/**
 * Desc:  适合纵向拓展
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:54
 */
public class Factory {

    public <T extends IProduct> T produce(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
