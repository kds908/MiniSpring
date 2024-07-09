package com.minis.bean.factory;

import com.minis.bean.BeansException;

import java.util.Map;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/6/3 15:23
 */
public interface ListableBeanFactory extends BeanFactory {

    boolean containsBeanDefinition(String beanName);
    int getBeanDefinitionCount();
    String[] getBeanDefinitionNames();
    String[] getBeanNamesForType(Class<?> type);
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

}
