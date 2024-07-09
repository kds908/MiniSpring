package com.minis.core;

import java.util.Iterator;

/**
 * 把外部配置信息当成 Resource 来进行抽象
 * 通过实现该类实现扩展
 */
public interface Resource extends Iterator<Object> {
}
