package com.minis.bean.factory.xml;

import com.minis.bean.factory.config.*;
import com.minis.bean.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 将解析好的 XML 转换成需要的 BeanDefinition
 */
public class XmlBeanDefinitionReader {
    AbstractBeanFactory beanFactory;

    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 加载 BeanDefinition
     * @param resource 资源resource
     */
    public void loadBeanDefinition(Resource resource) {
        System.out.println("load beanDefinition : 读取xml属性与结构，工厂bean definition");
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String initMethodName = element.attributeValue("init-method");

            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            // 处理属性
            List<Element> propertyElements = element.elements("property");
            ConstructorPropertyValues pvs = new ConstructorPropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pv = "";
                boolean isRef = false;
                if (pValue != null && !pValue.isEmpty()) {
                    pv = pValue;
                } else if (pRef != null && !pRef.isEmpty()) {
                    isRef = true;
                    pv = pRef;
                    refs.add(pRef);
                }
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
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);
            beanDefinition.setInitMethodName(initMethodName);
            this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }
}
