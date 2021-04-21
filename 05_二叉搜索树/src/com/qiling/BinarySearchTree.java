package com.qiling;

import com.qiling.printer.BinaryTreeInfo;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class BinarySearchTree<E> implements BinaryTreeInfo {
    private int size;
    private Node<E> root;
    private final Comparator<E> comparator;

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        return ((Node<E>) node).element;
    }

    private static class Node<E>{
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }
    }

    // 使用无参构造函数时使用传入类型内置默的认比较方法
    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return false;
    }

    public void clear() {}

    public void add(E element) {
        elementNotNullCheck(element);

        if (root == null) {
            // 第一次添加元素
            root = new Node<>(element, null);
            size++;
            return;
        }

        // 添加的不是第一个节点
        // 找到父节点(默认父节点为根节点)
        Node<E> parent = root;
        Node<E> node = root;

        // 需要这个比较值最后确定值插在父节点的哪一边
        int cmp = 0;
        // 为了判断应该插入哪儿, 需要一直跟节点比大小
        while (node != null) {
            parent = node;
            cmp = compare(element, node.element);
            if (cmp > 0) {
                // 用node右边节点再次比较
                node = node.right;
            } else if (cmp < 0) {
                // 用node左边节点再次比较
                node = node.left;
            } else {
                // 两值相等, 覆盖原来的值
                node.element = element;
                return;
            }
        }

        // 插入新节点
        Node<E> newNode = new Node<>(element, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
    }

    public void remove(E element) {}

    public boolean contains(E element) {
        return false;
    }

    // 前序遍历
    public void preorderTraversal() {
        preorderTraversal(root);
    }

    private void preorderTraversal(Node<E> node) {
        if (node == null) return;

        System.out.println(node.element);
        preorderTraversal(node.left);
        preorderTraversal(node.right);
    }

    // 检查传入值是否为空, 为空抛出异常
    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }

    /**
     * 比较传入两个值的大小
     * @author qiling
     * @date 2021-04-21 18:54
     * @param e1 - 比较的第一个值
     * @param e2 - 比较的第二个值
     * @return 如果返回的结果为0, 说明两个值相等, 结果为正数, 说明e1 > e2, 结果为负数, 说明e1 < e2
     */
    private int compare(E e1, E e2) {
        // 提供了比较器方法, 使用该方法比较大小
        if (comparator != null) return comparator.compare(e1, e2);

        // 没有提供比较器方法,将其转换为Comparable类型,如果转换失败说明是你传入数据的问题
        return  ((Comparable<E>) e1).compareTo(e2);
    }
}
