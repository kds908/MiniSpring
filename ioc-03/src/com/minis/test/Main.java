package com.minis.test;

import com.minis.bean.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) ctx.getBean("aService");
        aService.sayHello();
    }
}
