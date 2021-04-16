package com.qiling.doubleLinked;

public class DoubleLinkedlist<E> extends AbstractList<E> {
    private Node<E> first;
    private Node<E> last;

    private static class Node<E>{
        E element;
        Node<E> next;
        Node<E> prev;

        public Node(E element, Node<E> next, Node<E> prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    // 清空所有元素
    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
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

        if (index == size) {
            // 往最后(size处, elements.length)处添加元素
            Node<E> oldLast = last;

            // 它的prev应该指向之前的last
            last = new Node<>(element, null, oldLast);

            if (oldLast == null) {
                // 链表添加的第一个元素
                // first和last都指向新添加的节点
                // 由于前面的逻辑此时prev, last都为null
                first = last;
            } else {
                // 之前最后一个元素的next指向新添加的元素
                oldLast.next = last;
            }
        } else {
            // 要添加元素的上一个节点和下一个节点
            Node<E> next = getNode(index);
            Node<E> prev = next.prev;

            // 创建新的节点, 确定前后节点的指向
            Node<E> node = new Node<>(element, prev, next);

            // 确定前后节点的prev和next指向
            next.prev = node;
            if (prev == null) {
                // index == 0
                first = node;
            } else {
                prev.next = node;
            }
        }
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
        rangeCheck(index);

        Node<E> node = getNode(index);
        Node<E> next = node.next;
        Node<E> prev = node.prev;
        // 改变前后两个节点指针就行
        if (prev == null) { // index == 0
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) { // index == size - 1
            last = prev;
        } else {
            next.prev = prev;
        }

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

        // 判断所查找的元素在链表的哪一半
        Node<E> node;
        if (index < (size << 1)) {
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
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
