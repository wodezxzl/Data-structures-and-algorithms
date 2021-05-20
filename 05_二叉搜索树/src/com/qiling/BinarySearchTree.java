package com.qiling;

import com.qiling.printer.BinaryTreeInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

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

        // 是否是叶子节点
        public boolean isLeaf() {
            return  left == null && right == null;
        }

        // 是否有两个子节点
        public boolean hasTwoChildren() {
            return left != null && right != null;
        }
    }

    // 该抽象类定义如何处理访问到的元素, 和是否停止遍历
    public static abstract class Visitor<E> {
        boolean stop = false;
        // 返回true表示停止遍历
        abstract boolean visit(E element);
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
        return size == 0;
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
        return false;
    }

    public int height() {
        return nodeHeight(root);
    }

    // 获取任何节点的高度
    private int nodeHeight(Node<E> node) {
        /*if (node == null) return 0;
        return 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right));*/

        // 迭代实现(不过只能算出整棵树的高度)
        // 使用层序遍历实现
        if (node == null) return 0;

        int height = 0;
        // 每一层的元素数量
        int levelSize = 1;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        // 只要队列不为空就一直遍历, 分别将节点存在的左右子节点入队
        // 保证先进先出, 一层层从左向右遍历节点
        while (!queue.isEmpty()) {
            Node<E> queueNode = queue.poll();

            // 每取出一个元素这一层的数量减1
            levelSize--;

            if (queueNode.left != null) queue.offer(queueNode.left);
            if (queueNode.right != null) queue.offer(queueNode.right);

            if (levelSize == 0) {
                // 等于0之后,说明这一层已经遍历完成
                height++;
                levelSize = queue.size();
            }
        }

        return height;
    }

    // 是否为完全二叉树(利用层序遍历)
    public boolean isComplete() {
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            // 要求这个节点为叶子节点但是方法返回的又不是叶子,自然返回false
            if (leaf && !node.isLeaf()) return false;

            // 先将所有节点入队
            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                // 左子树为空或非空, 右子树为空
                // 那么之后的节点需要为叶子节点才行
                leaf = true;
            }
        }

        return true;
    }

    // 前序遍历
    public void preorderTraversal(Visitor<E> visitor) {
        if (visitor == null) return;
        preorderTraversal(root, visitor);
    }

    private void preorderTraversal(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;

        visitor.stop = visitor.visit(node.element);
        preorderTraversal(node.left, visitor);
        preorderTraversal(node.right, visitor);
    }

    // 中序遍历
    public void inorderTraversal(Visitor<E> visitor) {
        if (visitor == null) return;
        inorderTraversal(root, visitor);
    }

    private void inorderTraversal(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;

        preorderTraversal(node.left, visitor);
        // 这个判断是为了阻止不需要的操作
        if(visitor.stop) return;
        visitor.stop = visitor.visit(node.element);
        preorderTraversal(node.right, visitor);
    }

    // 后序遍历
    public void postorderTraversal(Visitor<E> visitor) {
        if (visitor == null) return;
        postorderTraversal(root, visitor);
    }

    private void postorderTraversal(Node<E> node, Visitor<E> visitor) {
        // 这个visitor.stop判断是为了终止递归
        if (node == null || visitor.stop) return;

        preorderTraversal(node.left, visitor);
        preorderTraversal(node.right, visitor);

        // 这个判断是为了阻止不需要的操作
        if(visitor.stop) return;
        visitor.stop = visitor.visit(node.element);
    }

    // 层序遍历
    public void levelOrderTraversal(Visitor<E> visitor){
        // 使用队列实现
        if (root == null || visitor == null) return;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        // 只要队列不为空就一直遍历, 分别将节点存在的左右子节点入队
        // 保证先进先出, 一层层从左向右遍历节点
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            // 由外界决定如何使用该元素
            // 返回true就停止遍历
            if (visitor.visit(node.element)) return;

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
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

    // 获取给定节点的前驱节点(中序遍历时给定节点的前一个节点称为前驱节点)
    private Node<E> predecessor(Node<E> node) {
        if (node == null) return null;
        Node<E> p = node.left;

        // 左子树不为空, 那么前驱节点一定在左子树
        if (p != null) {
            while (p.right != null) {
                // 一直找到最右边
                p = p.right;
            }
            return p;
        }

        // 左子树为空, 那么从祖父节点向上寻找
        // 父节点不为空, 并且当前节点是父节点的左子节点(说明此节点比父节点小)
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        // node.parent == null
        // node == node.parent.right
        // 这两种情况都可以返回node.parent
        return node.parent;
    }

    // 获取给定节点的后继节点(和上面反过来就行)
    private Node<E> successor(Node<E> node) {
        if (node == null) return null;
        Node<E> p = node.right;

        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
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
