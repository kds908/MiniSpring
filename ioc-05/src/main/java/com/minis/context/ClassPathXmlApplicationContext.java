package com.minis.context;

import com.minis.bean.BeansException;
import com.minis.bean.factory.BeanFactory;
import com.minis.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.bean.factory.config.AbstractAutowiredCapableBeanFactory;
import com.minis.bean.factory.config.BeanFactoryPostProcessor;
import com.minis.bean.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前新 ClassPathXmlApplicationContext 在实例化过程中做了三件事：
 * 1. 解析 XML 文件中的内容
 * 2. 加载解析的内容，构建 BeanDefinition
 * 3. 读取 BeanDefinition 的配置信息，实例化 Bean，然后把它注入到 BeanFactory 容器中
 */
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    AbstractAutowiredCapableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    /**
     * 整合容器的启动过程，读取外部配置，解析 Bean 定义，创建 BeanFactory
     * @param fileName 配置文件名
     */
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AbstractAutowiredCapableBeanFactory factory = new AbstractAutowiredCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(resource);
        this.beanFactory = factory;
        if (isRefresh) {
            try {
                System.out.println("刷新：ClassPathXmlApplicationContext#refresh");
                refresh();
            } catch (BeansException | IllegalStateException e) {
                throw new RuntimeException(e);
            }
        }
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
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

//    @Override
//    public void registerBean(String beanName, Object obj) {
//        this.beanFactory.registerBean(beanName, obj);
//    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        beanFactoryPostProcessors.add(postProcessor);
    }

    public void refresh() throws BeansException, IllegalStateException {
        registerBeanPostProcessors(beanFactory);
        onFresh();
    }

    private void registerBeanPostProcessors(AbstractAutowiredCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onFresh() {
        this.beanFactory.refresh();
    }
}
