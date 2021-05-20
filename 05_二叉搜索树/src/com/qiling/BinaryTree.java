package com.qiling;

import com.qiling.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class BinaryTree<E> implements BinaryTreeInfo {
    protected int size;
    protected BinarySearchTree.Node<E> root;

    protected static class Node<E>{
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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
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

    // 获取给定节点的前驱节点(中序遍历时给定节点的前一个节点称为前驱节点)
    protected Node<E> predecessor(Node<E> node) {
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
    protected Node<E> successor(Node<E> node) {
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
}
