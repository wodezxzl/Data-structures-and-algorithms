package com.qiling;

public class Main {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(12);
        stack.push(16);
        stack.push(15);
        stack.push(14);
        stack.push(13);
        stack.push(11);

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
