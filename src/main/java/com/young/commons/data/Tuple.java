package com.young.commons.data;

import com.young.commons.function.TupleConsumer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Tuple {
    private final List<Object> values;
    private final List<String> names = new LinkedList<>();

    public Tuple() {
        values = new ArrayList<>();
    }

    public Tuple(int initialCapacity) {
        values = new ArrayList<>(initialCapacity);
    }

    public Tuple(Tuple otherTuple) {
        this.values = new ArrayList<>(otherTuple.values.size() + 8);
        this.values.addAll(otherTuple.values);
        this.names.addAll(otherTuple.names);
    }

    public int size() {
        return values.size();
    }

    public boolean contains(String name) {
        return names.contains(name);
    }

    public boolean containsValue(Object value) {
        return values.contains(value);
    }

    public Object getValue(int index) {
        return values.get(index);
    }

    public Object getValue(String name) {
        if (name == null) {
            throw new IllegalArgumentException("not contains empty or null name.");
        }
        int index = names.indexOf(name);
        if (index < 0) {
            return null;
        }
        return values.get(index);
    }


    public void add(Object value) {
        values.add(value);
        names.add(null);
    }

    public void add(int index, Object value) {
        values.add(index, value);
        names.add(index, null);
    }

    public void add(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        // 如果有相同名称的值，先将其移除
        int index = names.indexOf(name);
        if (index > -1) {
            names.remove(index);
            values.remove(index);
        }
        values.add(value);
        names.add(name);
    }

    public boolean remove(Object value) {
        int index = values.indexOf(value);
        boolean result = values.remove(value);
        if (result) {
            names.remove(index);
        }
        return result;
    }

    public Object removeName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        int index = names.indexOf(name);
        if (index >= 0) {
            return values.remove(index);
        }
        return null;
    }

    public Object removeIndex(int index) {
        Object result = values.remove(index);
        names.remove(index);
        return result;
    }

    public void forEach(TupleConsumer<String, Integer, Object> action) {
        if (action == null) {
            return;
        }
        for (int i = 0, len = values.size(); i < len; i++) {
            action.apply(names.get(i), i, values.get(i));
        }
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        int limit = size() - 1;
        forEach((name, index, value) -> {
            result.append("{");
            if (name != null) {
                result.append("\"").append(name).append("\"");
            } else {
                result.append(index);
            }
            result.append(":").append(value).append("}");
            if (index < limit) {
                result.append(",");
            }
        });
        result.append("]");
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Tuple other = (Tuple) obj;
        return values.equals(other.values) && names.equals(other.names);
    }
}
