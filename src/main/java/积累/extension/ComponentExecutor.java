package 积累.extension;

import java.util.function.Function;

/**
 * @author fulan.zjf
 */
public abstract class ComponentExecutor {


    /**
     * @param targetClz        拓展点
     * @param exeFunction
     * @param bizParamProvider 类型参数提供者 用于匹配extension.Extension#bizCode()
     * @return
     */
    public <R, C extends ExtensionPointI> R execute(Class<C> targetClz, Function<C, R> exeFunction, Object
            bizParamProvider) {
        C component = locateComponent(targetClz, bizParamProvider);
        return exeFunction.apply(component);
    }

    protected abstract <C extends ExtensionPointI> C locateComponent(Class<C> targetClz, Object bizParamProvider);
}
