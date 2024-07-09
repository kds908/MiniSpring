package com.minis.core.env;

/**
 * 继承PropertyResolver 接口，获取属性
 * 所有的 ApplicationContext 都实现该接口
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2024/6/3 15:46
 */
public interface Environment extends PropertyResolver {
    String[] getActiveProfiles();
    String[] getDefaultProfiles();
    boolean acceptsProfiles(String... profiles);
}
