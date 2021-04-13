package com.qiling;

// 抽象类可以不全部实现接口中的方法
// 使用AbstractList是因为Arraylist和LinkedList还是有一些公共代码的, 可以抽取出来
public abstract class AbstractList<E> implements List<E>{
    protected int size;

    // 元素的数量
    @Override
    public int size() {
        return size;
    }

    // 是否为空
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 是否包含某个元素
    @Override
    public boolean contains(E element) {
        return indexOf(element) != ELEMENT_NOT_FOUND;
    }

    // 在末尾添加元素
    @Override
    public void add(E element) {
        add(size, element);
    }

    // 抛出索引越界错误
    protected void outOfBounds(int index) {
        throw new IndexOutOfBoundsException("Index" + index + ", Size" + size);
    }
    // 索引越界检测
    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            outOfBounds(index);
        }
    }
    // 索引越界检测(不需要等于size, 可以在size处添加元素)
    protected void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            outOfBounds(index);
        }
    }
}
