package com.minis.context;

import com.minis.beans.BeanDefinition;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义了唯一构造函数，构造函数做两件事：
 * 1. 提供一个 readXml() 方法，通过传入文件路径，来获取XML内的信息
 * 2. 提供一个instanceBeans()方法，根据读取到的信息实例化Bean
 */
public class ClassPathXmlApplicationContextOld {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();
    // 构造器获取外部配置，解析出Bean的定义，形成内存映像

    public ClassPathXmlApplicationContextOld(String fileName) {
        this.readXml(fileName);
        this.instanceBeans();
    }

    private void readXml(String fileName) {
        SAXReader saxReader = new SAXReader();
        try {
            URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
            Document document = saxReader.read(xmlPath);
            // 获取最外层标签元素，即根标签元素
            Element rootElement = document.getRootElement();
            // 对配置文件中的每个bean进行处理
            for (Element element : (List<Element>) rootElement.elements()) {
                String beanId = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
                // 将bean的定义存放到beanDefinitions
                beanDefinitions.add(beanDefinition);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 利用反射创建bean实例，并存储在 singletons 中
     */
    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                // since java 9 newInstance() -> getDeclaredConstructor().newInstance()
                singletons.put(beanDefinition.getId(),
                        Class.forName(beanDefinition.getClassName())
                                .getDeclaredConstructor()
                                .newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 外部程序从容器中获取bean实例，会逐步演化成核心方法
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
