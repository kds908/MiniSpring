package com.minis.bean.factory.config;

import com.minis.bean.BeansException;
import com.minis.bean.factory.BeanFactory;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/5/29 17:20
 */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
