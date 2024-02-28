package com.minis.bean;

/**
 * @author abners.
 * @description 将 registerBeanDefinition 改为 registerBean，
 * 参数为 beanName 与 obj
 * Object obj 指代与 beanName 对应的 Bean 的信息
 * 新增 Singleton、Prototype 的判断
 * @date 2024/2/27 14:39
 */
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    Boolean containsBean(String name);

//    void registerBean(String beanName, Object obj);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);
}
