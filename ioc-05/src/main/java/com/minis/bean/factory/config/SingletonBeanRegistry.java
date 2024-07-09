package com.minis.bean.factory.config;

/**
 * @author abners.
 * @description 管理单例 Bean
 * @date 2024/2/27 14:45
 */
public interface SingletonBeanRegistry {
    /**
     * 单例注册
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 获取单例
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * 判断单例是否存在
     * @param beanName
     * @return
     */
    boolean containsSingleton(String beanName);

    /**
     * 获取所有单例名
     * @return
     */
    String[] getSingletonNames();
}
