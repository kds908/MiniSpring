package com.minis.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BeanDefinition 实例化及加载到内存
 */
public class SimpleBeanFactory implements BeanFactory {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanNames = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public SimpleBeanFactory() {
    }

    /**
     * 容器的核心方法
     * @param beanName bean 名称
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 尝试直接拿 Bean 实例
        Object singleton = singletons.get(beanName);
        // 如果此时还没有这个 Bean 的实例，则获取它的定义来创建实例
        if (singleton == null) {
            int i = beanNames.indexOf(beanName);
            if (i == -1) {
                throw new BeansException("there is no bean names " + beanName);
            } else {
                // 获取 Bean 定义
                BeanDefinition beanDefinition = beanDefinitions.get(i);
                try {
                    // 实例化
                    singleton = Class.forName(beanDefinition.getClassName())
                            .getDeclaredConstructor()
                            .newInstance();
                } catch (InstantiationException | IllegalAccessException
                         | InvocationTargetException | NoSuchMethodException
                         | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                // 注册 Bean 实例
                singletons.put(beanDefinition.getId(), singleton);
            }
        }
        return singleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.add(beanDefinition);
        this.beanNames.add(beanDefinition.getId());
    }
}
