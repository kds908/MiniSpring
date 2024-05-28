package com.minis.bean.factory.xml;

import com.minis.bean.factory.config.*;
import com.minis.bean.factory.support.SimpleBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.List;

/**
 * 将解析好的 XML 转换成需要的 BeanDefinition
 */
public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    /**
     * 加载 BeanDefinition
     * @param resource 资源resource
     */
    public void loadBeanDefinition(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            // 处理属性
            List<Element> propertyElements = element.elements("property");
            ConstructorPropertyValues pvs = new ConstructorPropertyValues();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                pvs.addPropertyValue(new ConstructorPropertyValue(pType, pName, pValue));
            }
            beanDefinition.setPropertyValues(pvs);
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues avs = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                avs.addArgumentValue(new ConstructorArgumentValue(aType, aName, aValue));
            }
            beanDefinition.setConstructorArgumentValues(avs);
            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
