package com.minis.beans;

/**
 * Bean 工厂
 * 接口有两个特性：
 * 1. 获取一个 Bean (getBean)
 * 2. 注册一个 BeanDefinition (registerBeanDefinition)
 */
public interface BeanFactory {
    /**
     * 获取 Bean
     *
     * @param beanName bean 名称
     * @return Bean
     * @throws BeansException 自定义异常
     */
    Object getBean(String beanName) throws BeansException;

    /**
     * 注册 BeanDefinition
     * @param beanDefinition beanDefinition
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);
}
