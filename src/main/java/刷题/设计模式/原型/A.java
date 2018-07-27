package 刷题.设计模式.原型;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 11:07
 */
public class A implements Cloneable{
    private String a;

    public void setA(String a) {
        this.a = a;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("a", a)
                .toString();
    }
}
