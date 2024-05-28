package com.minis.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author abners.
 * @description 单例Bean注册默认实现类
 * @date 2024/2/27 14:53
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    // 容器中存放所有 bean 名称列表
    protected List<String> beanNames = new ArrayList<>();
    // 容器中存放所有 bean 实例的 map
    protected final Map<String, Object> singletons = new ConcurrentHashMap<>(256);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletons) {
            this.singletons.put(beanName, singletonObject);
            this.beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return beanNames.toArray(new String[0]);
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletons) {
            this.beanNames.remove(beanName);
            this.singletons.remove(beanName);
        }
    }
}
