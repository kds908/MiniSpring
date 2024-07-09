package com.minis.bean.factory.config;

import com.minis.bean.BeansException;
import com.minis.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.bean.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/5/29 16:23
 */
public class AutowiredCapableBeanFactory extends AbstractBeanFactory {
    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorBeforInitialization(Object existingBean, String beanName) throws BeansException {
        System.out.println("执行 applyBeanPostProcessorBeforInitialization");
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            beanPostProcessor.setBeanFactory(this);
            result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
        System.out.println("执行 applyBeanPostProcessorAfterInitialization");
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }
}
