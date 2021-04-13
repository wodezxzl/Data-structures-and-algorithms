package com.qiling;

import com.qiling.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(10);
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(new Person(18, "kobe"));
        personArrayList.add(new Person(12, "jams"));
        personArrayList.add(new Person(11, "qiling"));
        personArrayList.add(null);
        System.out.println(personArrayList.indexOf(null));
        System.out.println(personArrayList);
    }
}
