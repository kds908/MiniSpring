package com.minis.bean.factory.support;

import com.minis.bean.BeansException;
import com.minis.bean.factory.config.AbstractAutowiredCapableBeanFactory;
import com.minis.bean.factory.config.BeanDefinition;
import com.minis.bean.factory.config.ConfigurableListableBeanFactory;

import java.util.*;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/6/3 15:52
 */
public class DefaultListableBeanFactory extends AbstractAutowiredCapableBeanFactory implements ConfigurableListableBeanFactory {
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionNames.toArray(new String[0]);
    }

    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanDefinitionName : this.beanDefinitionNames) {
            boolean matchFound = false;
            BeanDefinition definition = this.getBeanDefinition(beanDefinitionName);
            Class<?> classToMatch = definition.getClass();
            if (type.isAssignableFrom(classToMatch)) {
                matchFound = true;
            }
            if (matchFound) {
                result.add(beanDefinitionName);
            }
        }
        return result.toArray(new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            result.put(beanName, (T) beanInstance);
        }
        return result;
    }

}
