package jdk特性.jdk1_6.注解处理器;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Desc: 插入式注解处理器demo
 * 编译时增加参数 javac -processor jdk特性.jdk1_6.ComplieProcessorDemo
 * Author: ljdong2
 * Date: 2021-01-10
 * Time: 16:51
 * @link https://blog.csdn.net/sdksdk0/article/details/90695717
 */
// 可以用"*"表示支持所有Annotations
@SupportedAnnotationTypes("*")
// 只支持JDK 1.8的Java代码
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NameCheckerProcessor extends AbstractProcessor {

    private NameChecker nameChecker;

    /**
     * 初始化名称检查插件
     */
    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        nameChecker = new NameChecker(processingEnv);
    }

    /**
     * 对输入的语法树的各个节点进行进行名称检查
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getRootElements()) {
                nameChecker.checkNames(element);
            }
        }
        return false;
    }
}
