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
 * @date: 2024/6/3 15:41
 */
public interface AutowiredCapableBeanFactory extends BeanFactory {
    int AUTOWIRED_NO = 0;
    int AUTOWIRED_BY_NAME = 1;
    int AUTOWIRED_BY_TYPE = 2;

    Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
