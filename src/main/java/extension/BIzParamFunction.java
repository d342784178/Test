package extension;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-03-02
 * Time: 18:00
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BIzParamFunction {
    Class bizParamFunction();
}
