package com.qiling;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new DoubleLinkedlist<>();
        list.add(12);
        list.add(10);
        list.add(0, 1000);
        list.add(list.size(), 33);
        System.out.println(list);
    }
}
