package 链表;

// https://leetcode-cn.com/problems/reverse-linked-list/
public class _206_反转链表 {
    /*
     * 递归实现: 1 -> 2 -> 3 -> 4 -> 5 -> null 变成 5 -> 4 -> 3 -> 2 -> 1 -> null
     * 递归的关键就是搞清楚递归函数的作用和一个子问题的实现和递归终止条件
     *      作用: reverseList_01的作用就是得到将传入节点作为头结点的反转链表
     *      子问题实现:
     *          传入head.next 得到 4 -> 3 -> 2 -> 1 -> null链表
     *          此时需要将5链接起来, 5.next为4, head.next.next = head将4.next指向5
     *          head.next = null将5.next指向null
     *      终止条件: 传入的head为null, 或者只有一个节点, 直接返回head
     */
    public ListNode reverseList_01(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = reverseList_01(head.next);
        head.next.next = head;
        head.next = null;

        return newHead;
    }

    /*
     * 迭代实现: 迭代实现一般都需要额外的指针来获取信息
     *      这个题目只有输入的头节点信息, 只能先从头节点开始从前往后一个个串起来
     * newHead表示最终返回链表的头节点, 开始是null
     * tmp用来保证循环的执行(获取下一个节点)
     */
    public ListNode reverseList_02(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = null;

        while (head != null) {
            // 一开始tmp就要指向头节点的下一个节点
            ListNode tmp = head.next;
            // 头节点.next指向null(第一次)
            // 旧链表头节点指向指向新链表头节点(之后)
            head.next = newHead;
            // 此时就已经串好一个节点, 那么newHead指向head, 表示更新为新链表头节点
            newHead = head;
            // 开始串下一个节点, head指向下一个节点
            head = tmp;
        }

        return newHead;
    }
}
