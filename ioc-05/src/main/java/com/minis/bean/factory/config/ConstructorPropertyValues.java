package com.minis.bean.factory.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abners.
 * @description
 * @date 2024/2/27 16:51
 */
public class ConstructorPropertyValues {
    private List<ConstructorPropertyValue> constructorPropertyValueList = new ArrayList<>();

    public ConstructorPropertyValues() {
    }

    public ConstructorPropertyValues(List<ConstructorPropertyValue> constructorPropertyValueList) {
        this.constructorPropertyValueList = constructorPropertyValueList;
    }

    public List<ConstructorPropertyValue> getPropertyValueList() {
        return constructorPropertyValueList;
    }

    public int size() {
        return constructorPropertyValueList.size();
    }

    public void addPropertyValue(ConstructorPropertyValue value) {
        this.constructorPropertyValueList.add(value);
    }

    public void addPropertyValue(String propertyName, Object propertyValue) {
        addPropertyValue(new ConstructorPropertyValue(propertyName, propertyValue));
    }

    public void removePropertyValue(ConstructorPropertyValue value) {
        this.constructorPropertyValueList.remove(value);
    }

    public void removePropertyValue(String propertyName) {
        this.constructorPropertyValueList.remove(getPropertyValue(propertyName));
    }

    public ConstructorPropertyValue[] getPropertyValues() {
        return this.constructorPropertyValueList.toArray(new ConstructorPropertyValue[0]);
    }

    public ConstructorPropertyValue getPropertyValue(String propertyName) {
        for (ConstructorPropertyValue value : this.constructorPropertyValueList) {
            if (value.getName().equals(propertyName)) {
                return value;
            }
        }
        return null;
    }

    public Object get(String propertyName) {
        ConstructorPropertyValue value = getPropertyValue(propertyName);
        return value == null ? null : value.getValue();
    }

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    public boolean isEmpty() {
        return this.constructorPropertyValueList.isEmpty();
    }
}
