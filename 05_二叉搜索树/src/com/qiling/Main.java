package com.qiling;

import com.qiling.printer.BinaryTrees;
import com.qiling.BinarySearchTree.Visitor;

public class Main {
    public static void main(String[] args) {
        BinarySearchTree<Object> bst = new BinarySearchTree<>();
        int[] arr = new int[16];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.floor(Math.random() * 100) + 1);
            bst.add(arr[i]);
        }
        BinaryTrees.println(bst);
        bst.levelOrderTraversal(new Visitor<Object>() {
            @Override
            public boolean visit(Object element) {
                System.out.print("_" + element + "_");
                return false;
            }
        });
        System.out.println();
        System.out.println(bst.height());
        System.out.println(bst.isComplete());
    }
}
