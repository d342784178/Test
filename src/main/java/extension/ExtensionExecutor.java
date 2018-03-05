/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.GenericsUtils;

/**
 * ExtensionExecutor
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionExecutor extends ComponentExecutor {

    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    @Autowired
    private ExtensionRepository extensionRepository;

    @Override
    protected <C extends ExtensionPointI> C locateComponent(Class<C> targetClz, Object bizParamProvider) {
        BIzParamFunction annotation = targetClz.getAnnotation(BIzParamFunction.class);
        if (annotation != null && bizParamProvider != null) {
            Class aClass = annotation.bizParamFunction();
            if (BizParamProvider.class.isAssignableFrom(aClass)) {//获取转换接口
                try {
                    BizParamProvider o                    = (BizParamProvider) aClass.newInstance();
                    Class            superClassGenricType = GenericsUtils.getInterfaceGenricType(o.getClass(), 0, 0);
                    if (bizParamProvider.getClass().isAssignableFrom(superClassGenricType)) {
                        String bizParam  = (String) o.getBizParam(bizParamProvider);
                        C      extension = locateExtension(targetClz, bizParam);
                        logger.debug("[Located Extension]: " + extension.getClass().getSimpleName());
                        return extension;
                    } else {
                        throw new IllegalArgumentException("bizParamProvider 参数类型不正确,要求类型:" + superClassGenricType
                                .getName());
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return locateExtension(targetClz, null);
    }

    /**
     * @param targetClz
     * @param bizParam
     */
    @SuppressWarnings("unchecked")
    protected <Ext extends ExtensionPointI> Ext locateExtension(Class<Ext> targetClz, String bizParam) {
        ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(targetClz.getSimpleName(), bizParam);
        /**
         * 1.First search key is: extensionPoint + bizCode
         */
        Ext extension = (Ext) extensionRepository.getExtensionRepo().get(extensionCoordinate);
        if (extension != null) {
            return extension;
        }
        /**
         * 2.Third search key is: extensionPoint
         */
        extensionCoordinate.setBizCode("-");
        extension = (Ext) extensionRepository.getExtensionRepo().get(extensionCoordinate);
        if (extension != null) {
            return extension;
        }
        throw new IllegalArgumentException("Can not find extension for ExtensionPoint: " + targetClz);
    }
}
