package 链表;

// https://leetcode-cn.com/problems/linked-list-cycle/
// 快慢指针思想, 快指针走两步, 慢指针走一步, 看是否相遇
public class _141_环形链表 {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;

        ListNode fast = head.next;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) return true;
        }

        return false;
    }
}
