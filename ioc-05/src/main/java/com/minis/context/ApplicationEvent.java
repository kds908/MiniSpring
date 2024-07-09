package com.minis.context;

import java.io.Serial;
import java.util.EventObject;

/**
 * @author abners.
 * @description 继承了 java util EventObject
 * 在 Java 事件监听的基础上进行简单封装
 * @date 2024/2/27 15:35
 */
public class ApplicationEvent extends EventObject {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
