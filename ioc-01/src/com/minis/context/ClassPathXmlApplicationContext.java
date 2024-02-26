package com.minis.context;

import com.minis.beans.*;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * 当前新 ClassPathXmlApplicationContext 在实例化过程中做了三件事：
 * 1. 解析 XML 文件中的内容
 * 2. 加载解析的内容，构建 BeanDefinition
 * 3. 读取 BeanDefinition 的配置信息，实例化 Bean，然后把它注入到 BeanFactory 容器中
 */
public class ClassPathXmlApplicationContext implements BeanFactory {
    BeanFactory beanFactory;

    /**
     * 整合容器的启动过程，读取外部配置，解析 Bean 定义，创建 BeanFactory
     * @param fileName 配置文件名
     */
    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        BeanFactory factory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(resource);
        this.beanFactory = factory;
    }

    /**
     * context 再对外提供一个 getBean，底下就是调用的 BeanFactory 对应的方法
     * @param beanName bean 名称
     * @return Bean
     * @throws BeansException 自定义异常
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
