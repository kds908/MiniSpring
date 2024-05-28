package com.minis.bean.factory.config;

/**
 * @author abners.
 * @description
 * @date 2024/2/27 16:13
 */
public class ConstructorPropertyValue {
    private String type;
    private String name;
    private Object value;

    public ConstructorPropertyValue(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public ConstructorPropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
