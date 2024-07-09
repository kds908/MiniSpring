package com.minis.bean.factory.annotation;

import com.minis.bean.BeansException;
import com.minis.bean.factory.config.AbstractAutowiredCapableBeanFactory;
import com.minis.bean.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/5/28 16:58
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private AbstractAutowiredCapableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization " + beanName);
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String fieldName = field.getName();
                Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                    System.out.println("autowire " + fieldName + " for bean " + beanName);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public AbstractAutowiredCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AbstractAutowiredCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
