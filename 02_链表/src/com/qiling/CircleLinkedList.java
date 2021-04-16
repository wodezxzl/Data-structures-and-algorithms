package com.qiling;

public class CircleLinkedList<E> extends AbstractList<E>{
    private Node<E> first;

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

        if(index == 0) {
            // 为o时需要特殊处理(0 - 1 = -1)
            // 新节点的next指向为first的指向, 为null或者节点
            // 创建额外变量newFirst指向新节点, 这里不能直接用first,会导致getNode找节点错误
            // 因为getNode中需要使用first, 但是你改变了first的指向. 最后一步该first就行
            Node<E> newFirst = new Node<>(element, first);

            // !实现单向循环列表
            // 拿到最后一个位置节点, 它的next指向第一个节点
            // 第一次插入节点时将first当做最后一个几点, 那么实现节点next指向自己
            Node<E> last = (size == 0) ? newFirst : getNode(size - 1);
            last.next = newFirst;
            first = newFirst;
        } else {
            // 先获取要添加位置的前一个节点
            Node<E> prevNode = getNode(index - 1);
            // 前一个节点next指向新节点
            // 新节点next指向为前一个节点的next
            prevNode.next = new Node<>(element, prevNode.next);
        }

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
         Node<E> node = first;

        if (index == 0) {
            // !实现单向循环列表
            if (size == 1) {
                first = null;
            } else {
                Node<E> last = getNode(size - 1);
                first = first.next;
                // 最后一个节点的next指向删除节点的下一个节点
                last.next = first;
            }
        } else {
            Node<E> prevNode = getNode(index - 1);
            node = prevNode.next;
            prevNode.next = prevNode.next.next;
        }


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
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        return node;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("size = ").append(size).append(", [");
        Node<E> node = first;
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
