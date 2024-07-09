package com.minis.bean.factory.support;

import com.minis.bean.BeansException;
import com.minis.bean.factory.BeanFactory;
import com.minis.bean.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/5/28 17:08
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    protected final List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {
    }

    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        //先尝试直接从容器中获取bean实例
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            //如果没有实例，则尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) { //如果连毛胚都没有，则创建bean实例并注册
                System.out.println("get bean null -------------- " + beanName);
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                singleton = createBean(beanDefinition);
                this.registerBean(beanName, singleton);

                // 进行 bean post processor 处理
                // step 1 ：postProcessorBeforeInstantiation
                applyBeanPostProcessorAfterInitialization(singleton, beanName);
                // step 2 : init-method
                if (beanDefinition.getInitMethodName() != null) {
                    invokeInitMethod(beanDefinition, singleton);
                }
                // step 3 : postProcessorAfterInstantiation
                applyBeanPostProcessorAfterInitialization(singleton, beanName);
            }
        }
        return singleton;
    }

    private void invokeInitMethod(BeanDefinition beanDefinition, Object object) throws BeansException {
        Class<?> clazz = beanDefinition.getClass();
        Method method = null;
        try {
            method = clazz.getMethod(beanDefinition.getInitMethodName());
            try {
                method.invoke(object);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException e) {
            throw new BeansException(e.getMessage());
        }
    }

    @Override
    public Boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public void registerBean(String beanName, Object singleton) {
        this.registerSingleton(beanName, singleton);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        Object obj = doCreateBean(beanDefinition);
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        populateBean(beanDefinition, clazz, obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clazz;
        Object obj;
        Constructor<?> constructor;
        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 处理构造器参数
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        if (!constructorArgumentValues.isEmpty()) {
            Class<?>[] paramTypes = new Class[constructorArgumentValues.getArgumentCount()];
            Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
            for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                ConstructorArgumentValue argumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
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
        return obj;
    }

    private void populateBean(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        handleProperties(beanDefinition, clazz, obj);
    }

    /**
     * 补充属性值
     * @param definition
     * @param clz
     * @param obj
     */
    private void handleProperties(BeanDefinition definition, Class<?> clz, Object obj) {
        // 属性处理
        ConstructorPropertyValues constructorPropertyValues = definition.getPropertyValues();
        if (!constructorPropertyValues.isEmpty()) {
            for (int i = 0; i < constructorPropertyValues.size(); i++) {
                ConstructorPropertyValue constructorPropertyValue = constructorPropertyValues.getPropertyValueList().get(i);
                String pType = constructorPropertyValue.getType();
                String pName = constructorPropertyValue.getName();
                Object pValue = constructorPropertyValue.getValue();
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
                    method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    abstract public Object applyBeanPostProcessorBeforInitialization(Object existingBean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
