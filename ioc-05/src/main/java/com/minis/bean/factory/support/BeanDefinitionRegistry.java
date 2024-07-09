package com.minis.bean.factory.support;

import com.minis.bean.factory.config.BeanDefinition;

/**
 * @author abners.
 * @description 类似存放 BeanDefinition 的仓库
 * 可以存放、移除、获取及判断 BeanDefinition 对象
 * @date 2024/2/27 17:11
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition definition);

    void removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean containsBeanDefinition(String name);
}
