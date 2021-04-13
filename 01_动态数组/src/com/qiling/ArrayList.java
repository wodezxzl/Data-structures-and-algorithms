package com.qiling;

import java.util.Arrays;

@SuppressWarnings({"unchecked", "UnusedReturnValue"})
public class ArrayList<E> {
    // 元素的数量
    private int size;
    // 所有的元素
    private E[] elements;

    private static final int DEFAULT_CAPACITY = 10;
    private static final int ELEMENT_NOT_FOUNT = -1;

    public ArrayList() {
        // 默认长度为10
        // this可以调用自身构造方法
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int capacity) {
        // 传入的值比默认值小就还是使用默认值
        capacity = Math.max(capacity, DEFAULT_CAPACITY);
        elements = (E[])new Object[capacity];
    }

    /**
     * 清空数组
     * @author qiling
     * @date 2021-04-12 11:48
     */
    public void clear() {
        // 里面全部存放数字时可以单独这样写, 保证别人访问不到元素,来实现数组清空
        // size = 0;

        // 当里面存放的是对象类型值时想要清空数组就要保证数组每个位置的引用为null
        // 没有必要elements = null 让数组销毁, 再次重建浪费资源
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 返回元素数量
     * @author qiling
     * @date 2021-04-12 12:25
     */
    public int size() {
        return size;
    }

    /**
     * 获取数组元素值
     * @author qiling
     * @date 2021-04-12 11:32
     * @param index - 数组索引
     * @return 数组值
     */
    public E get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    /**
     * 设置数组元素值
     * @author qiling
     * @date 2021-04-12 11:38
     * @param index - 设置数组元素位置
     * @param element - 设置的元素
     * @return 原来的元素
     */
    public E set(int index, E element) {
        rangeCheck(index);
        E oldElement = elements[index];
        elements[index] = element;

        return oldElement;
    }

    /**
     * 数组末尾添加元素值
     * @author qiling
     * @date 2021-04-12 12:08
     * @param element - 要添加的元素
     */
    public void add(E element) {
        add(size, element);
    }
    /**
     * 在数组指定位置添加元素,包含扩容操作,可以添加null
     * @author qiling
     * @date 2021-04-12 12:21
     * @param index - 添加指定位置
     * @param element - 添加的元素
     */
    public void add(int index, E element) {
        // 此处不需要等于size, 可以在size处插入元素
        rangeCheckForAdd(index);

        // 增加一个元素后size加一, 要确保此时数组容量够, 再进行添加操作
        ensureCapacity(size + 1);

        /*for (int i = size -1; i >= index; i--) {
            elements[i + 1] = elements[i];
        }*/

        // 相比于上面就是少了一次size - 1 的操作(小小的优化)
        for (int i = size; i > index; i--) {
            elements[i] = elements[i -1];
        }

        elements[index] = element;
        size++;
    }

    /**
     * 删除数组元素值
     * @author qiling
     * @date 2021-04-12 12:10
     * @param index - 要删除值的索引
     * @return 删除的值
     */
    public E remove(int index) {
        rangeCheck(index);
        E oldElement = elements[index];

        // 数组元素逐个向前挪动
        for (int i = index + 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }

        // 由于向前移动了元素, 所以应该将最后一个元素索引清空
        // 同时注意size应该减少1
        elements[--size] = null;

        return oldElement;
    }
    public void remove(E element) {
        remove(indexOf(element));
    }

    /**
     * 查看元素索引
     * @author qiling
     * @date 2021-04-12 11:42
     * @param element - 查找的元素
     * @return 查找的元素的索引
     */
    public int indexOf(E element) {
        // 注意在这里需要判断element是否为null, null无法调用equals方法
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                // 判断对象是否相等使用对象自己重写的equals方法
                if (element.equals(elements[i])) return i;
            }
        }
        return ELEMENT_NOT_FOUNT;
    }

    /**
     * 数组是否包含元素
     * @author qiling
     * @date 2021-04-12 11:46
     * @param element - 是否包含的元素
     * @return boolean
     */
    public boolean isContains(E element) {
        return indexOf(element) != ELEMENT_NOT_FOUNT;
    }

    // 抛出索引越界错误
    private void outOfBounds(int index) {
        throw new IndexOutOfBoundsException("Index" + index + ", Size" + size);
    }
    // 索引越界检测
    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            outOfBounds(index);
        }
    }
    // 索引越界检测(不需要等于size, 可以在size处添加元素)
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            outOfBounds(index);
        }
    }
    // 确保elements中容量足够, 如果不够进行扩容
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;

        // 现有容量不够才需要扩容
        // 新容量为旧容量1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[])new Object[newCapacity];
        // 一个for循环移动所有元素
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("size = ").append(size).append(", [");
        for (int i = 0; i < size; i++) {
            // 先拼接逗号再拼接元素
            if (i != 0) {
                str.append(", ");
            }
            str.append(elements[i]);
        }
        str.append("]");

        return str.toString();
    }
}
