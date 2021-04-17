package com.qiling;

import java.util.ArrayList;
import java.util.List;

public class Stack<E>{
    private final List<E> list = new ArrayList<>();
    public void push(E element){
        list.add(element);
    }

    public E pop() {
        return list.remove(list.size() - 1);
    }

    public E top() {
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }
}
