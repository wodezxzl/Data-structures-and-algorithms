package com.qiling;

public  class Linkedlist<E> extends AbstractList<E> {
    private Node<E> first;

    // 利用构造方法添加一个虚拟头结点, 有便于一些操作
    public Linkedlist() {
        first = new Node<>(null,  null);
    }

    private static class Node<E>{
        E element;
        Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    // 清空所有元素
    @Override
    public void clear() {
        size = 0;
        first = null;
    }

    //获取index位置的节点值
    @Override
    public E get(int index) {
        return getNode(index).element;
    }

    /**
     * 设置节点新值
     * @author qiling
     * @date 2021-04-13 13:36
     * @param index - 要设置节点值的位置
     * @param element - 设置的节点值
     * @return 之前的节点
     */
    @Override
    public E set(int index, E element) {
        Node<E> node = getNode(index);
        E oldElement = node.element;
        node.element = element;

        return oldElement;
    }

    // 在index位置添加一个元素
    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        /*if(index == 0) {
            // 为o时需要特殊处理(0 - 1 = -1)
            // 新节点的next指向为first的指向, 为null或者节点
            // first指向新节点
            first = new Node<>(element, first);
        } else {
            // 先获取要添加位置的前一个节点
            Node<E> prevNode = getNode(index - 1);
            // 前一个节点next指向新节点
            // 新节点next指向为前一个节点的next
            prevNode.next = new Node<>(element, prevNode.next);
        }*/

        // !使用虚拟头结点之后
        // !如果index为0时返回虚拟头结点, 这样在0位置插入元素时就和在其他位置插入逻辑一样了
        Node<E> prevNode = index == 0 ? first : getNode(index - 1);
        prevNode.next = new Node<>(element, prevNode.next);

        size++;
    }

    /**
     * 删除一个节点
     * @author qiling
     * @date 2021-04-13 13:52
     * @param index - 删除节点的位置
     * @return 删除的节点的值
     */
    @Override
    public E remove(int index) {
        // 虽然之后可能在getNode中会调用rangeCheck, 但是删除方法需要在一开始就进行index检查
        rangeCheck(index);

        // 要被删除的元素, 初始为first节点
        // Node<E> node = first;

        /*if (index == 0) {
            first = first.next;
        } else {
            Node<E> prevNode = getNode(index - 1);
            node = prevNode.next;
            prevNode.next = prevNode.next.next;
        }*/

        // !使用虚拟头结点之后
        Node<E> prevNode = index == 0 ? first : getNode(index - 1);
        Node<E>  node = prevNode.next;
        prevNode.next = prevNode.next.next;

        size--;
        return node.element;
    }
    public E remove(E element) {
        return remove(indexOf(element));
    }

    // 查看元素的索引
    @Override
    public int indexOf(E element) {
        // 获取头结点, 向下不断遍历找到符合的节点值
        Node<E> node = first;
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (node.element == null) return i;
                node = node.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                // 判断对象是否相等使用对象自己重写的equals方法
                if (element.equals(node.element)) return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 根据传入的节点位置返回此位置的节点
     * @author qiling
     * @date 2021-04-13 13:29
     * @param index - 节点位置
     * @return node
     */
    private Node<E> getNode(int index) {
        rangeCheck(index);

        // 获得第一个节点, 然后从前向后找到index位置节点
        // !由于增加了虚拟头结点, 需要从first.next开始获取
        Node<E> node = first.next;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        return node;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("size = ").append(size).append(", [");
        Node<E> node = first.next;
        for (int i = 0; i < size; i++) {
            // 先拼接逗号再拼接元素
            if (i != 0) {
                str.append(", ");
            }
            str.append(node.element);
            node = node.next;
        }
        str.append("]");

        return str.toString();
    }
}
