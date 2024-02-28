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
    private List<ArgumentValue> argumentValues = new ArrayList<>();

    public ArgumentValues() {
    }

    public void addArgumentValue(ArgumentValue newValue) {
        this.argumentValues.add(newValue);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValues.get(index);
    }

    public int getArgumentCount() {
        return this.argumentValues.size();
    }

    public boolean isEmpty() {
        return this.argumentValues.isEmpty();
    }
}
