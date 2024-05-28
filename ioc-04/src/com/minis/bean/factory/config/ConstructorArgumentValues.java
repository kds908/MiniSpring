package com.minis.bean.factory.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abners.
 * @description
 * @date 2024/2/27 16:16
 */
public class ConstructorArgumentValues {
    private List<ConstructorArgumentValue> argumentValues = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue newValue) {
        this.argumentValues.add(newValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValues.get(index);
    }

    public int getArgumentCount() {
        return this.argumentValues.size();
    }

    public boolean isEmpty() {
        return this.argumentValues.isEmpty();
    }
}
