package com.minis.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abners.
 * @description
 * @date 2024/2/27 16:16
 */
public class ArgumentValues {
    private Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<>();
    private List<ArgumentValue> genericArgumentValues = new ArrayList<>();

    public ArgumentValues() {
    }

    private void addArgumentValue(Integer key, ArgumentValue newValue) {
        this.indexedArgumentValues.put(key, newValue);
    }

    private boolean hasIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.containsKey(index);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.get(index);
    }

    public void addGenericArgumentValue(ArgumentValue newValue) {
        genericArgumentValues.removeIf(argumentValue ->
                newValue.getName().equals(argumentValue.getName()));
        this.genericArgumentValues.add(newValue);
    }

    public ArgumentValue getGenericArgumentValue(String requiredName) {
        for (ArgumentValue valueHolder : this.genericArgumentValues) {
            if (valueHolder.getName() != null && valueHolder.getName().equals(requiredName)) {
                return valueHolder;
            }
        }
        return null;
    }

    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }
}
