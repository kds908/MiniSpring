package com.minis.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author abners.
 * @description
 * @date 2024/2/27 15:05
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
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
            singleton = createBean(beanDefinition);
            // 注册 bean 实例
            this.registerSingleton(beanName, singleton);
        }
        return singleton;
    }

    private Object createBean(BeanDefinition definition) {
        Class<?> clazz;
        Object obj;
        Constructor<?> constructor;
        try {
            clazz = Class.forName(definition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 处理构造器参数
        ArgumentValues argumentValues = definition.getConstructorArgumentValues();
        if (!argumentValues.isEmpty()) {
            Class<?>[] paramTypes = new Class[argumentValues.getArgumentCount()];
            Object[] paramValues = new Object[argumentValues.getArgumentCount()];
            for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                    paramTypes[i] = String.class;
                    paramValues[i] = argumentValue.getValue();
                } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                    paramTypes[i] = Integer.class;
                    paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                } else if ("int".equals(argumentValue.getType())) {
                    paramTypes[i] = int.class;
                    paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                } else {
                    paramTypes[i] = String.class;
                    paramValues[i] = argumentValue.getValue();
                }
            }
            try {
                // 按照特定构造器创建实例
                constructor = clazz.getConstructor(paramTypes);
                obj = constructor.newInstance(paramValues);
            } catch (NoSuchMethodException | InstantiationException
                     | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                // 无参直接创建实例
                obj = clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        // 属性处理
        PropertyValues propertyValues = definition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                Class<?>[] paramTypes = new Class[1];
                if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                    paramTypes[0] = String.class;
                } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                    paramTypes[0] = Integer.class;
                } else if ("int".equals(pType)) {
                    paramTypes[0] = int.class;
                } else {
                    paramTypes[0] = String.class;
                }
                Object[] paramValues = new Object[1];
                paramValues[0] = pValue;
                // 按照规范查找 setter 方法，调用setter 方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                Method method;
                try {
                    // 反射执行setter，赋值属性
                    method = clazz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return obj;
    }

    @Override
    public Boolean containsBean(String name) {
        return containsSingleton(name);
    }

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
