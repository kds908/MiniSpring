package com.minis.context;

/**
 * @author abners.
 * @description 事件发布
 * @date 2024/2/27 15:35
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
