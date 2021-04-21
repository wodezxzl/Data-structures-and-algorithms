package com.qiling;

import com.qiling.printer.BinaryTrees;

public class Main {
    public static void main(String[] args) {
        BinarySearchTree<Object> bst = new BinarySearchTree<>();
        int[] arr = new int[16];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.floor(Math.random() * 100) + 1);
            bst.add(arr[i]);
        }
        BinaryTrees.println(bst);
        bst.preorderTraversal();
    }
}
