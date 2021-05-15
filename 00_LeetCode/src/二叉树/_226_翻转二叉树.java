package 二叉树;

// https://leetcode-cn.com/problems/invert-binary-tree/
public class _226_翻转二叉树 {
     // 前序和后序都是这个套路
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) return root;

        TreeNode node = root.left;
        root.left = root.right;
        root.right = node;

        invertTree(root.left);
        invertTree(root.right);

        return root;

        // 中序遍历是有问题的, 先遍历左子树,后交互左右子树,那么你再遍历右子树时实际上还是遍历的之前左子树
        /*invertTree(root.left);
        TreeNode node = root.left;
        root.left = root.right;
        root.right = node;
        invertTree(root.right);*/

        // **改进中序遍历, 将原来的right换为left
        /*invertTree(root.left);
        TreeNode node = root.left;
        root.left = root.right;
        root.right = node;
        invertTree(root.left);*/
    }

    // 层序遍历实现
    public void test(TreeNode root) {
//        if (root == null) return root;
//
//        Queue<Node<E>> queue = new LinkedList<>();
//        queue.offer(root);
//
//        while (!queue.isEmpty()) {
//            Node<E> node = queue.poll();
//
//            在此处交互左右节点即可
//            TreeNode node = root.left;
//            root.left = root.right;
//            root.right = node;
//
//            if (node.left != null) queue.offer(node.left);
//            if (node.right != null) queue.offer(node.right);
//        }
    }
}
