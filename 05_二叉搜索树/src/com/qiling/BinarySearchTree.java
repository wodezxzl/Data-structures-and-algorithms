package com.qiling;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class BinarySearchTree<E> extends BinaryTree<E>{
    private final Comparator<E> comparator;

    // 使用无参构造函数时使用传入类型内置默的认比较方法
    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

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

    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {
        if (node == null) return;

        size--;

        // 删除的节点度为2时
        if (node.hasTwoChildren()) {
            // 找到该节点的前驱或者后继节点, 用该节点来代替要被删除的节点
            Node<E> successor = successor(node);
            node.element = successor.element;
            // 删除后继节点(后面删除node节点就行)
            // 后继或者前驱节点一定是叶子节点, 这样改变指向后, 就和后面的删除叶子节点逻辑相同了
            // 所以直接后面就删除了
            node = successor;
        }

        // 删除的节点度为1或0时
        Node<E> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            // replacement不为null时, 说明node是度为1的节点

            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left或right指向
            if (node.parent == null) {
                // node是度为1的节点, 并且是根节点
                root = replacement;
            }else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else{
                node.parent.right = replacement;
            }
        } else {
            // 为null时说明node是度为0的节点, 那么直接删除它就可以了

            // 说明这是一个根节点
            if (node.parent == null) {
                root = null;
            } else {
                // 说明是一个普通叶子节点
                if (node == node.parent.right) {
                    node.parent.right = null;
                } else {
                    node.parent.left = null;
                }
            }
        }
    }

    public boolean contains(E element) {
        return node(element) != null;
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

    // 根据给的元素找到对应节点
    private Node<E> node(E element) {
        Node<E> node = root;

        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0 ) return node;
            if (cmp > 0) node = node.right;
            if (cmp < 0) node = node.left;
        }

        return null;
    }
}
