package com.minis.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author abners.
 * @description
 * @date 2024/2/27 15:05
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory{
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>(256);

    public SimpleBeanFactory() {
    }

    /**
     * 容器核心方法
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接拿 bean 实例
        Object singleton = this.getSingleton(beanName);
        // 如果此时还没有这个实例，则获取它的定义来创建实例
        if (singleton == null) {
            // 获取 bean 的定义
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            if (beanDefinition == null) {
                throw new BeansException("no bean names " + beanName);
            }
            try {
                singleton = Class.forName(beanDefinition.getClassName())
                        .getConstructor()
                        .newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            // 注册 bean 实例
            this.registerSingleton(beanName, singleton);
        }
        return singleton;
    }

    @Override
    public Boolean containsBean(String name) {
        return containsSingleton(name);
    }

//    @Override
//    public void registerBean(String beanName, Object obj) {
//        this.registerSingleton(beanName, obj);
//    }


    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanDefinition.getId(), beanDefinition);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitions.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitions.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitions.get(name).getClass();
    }
}
