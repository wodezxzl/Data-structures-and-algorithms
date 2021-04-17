package 栈;

import java.util.Stack;

// https://leetcode-cn.com/problems/valid-parentheses/
public class _20_有效的括号 {
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        int length = s.length();

        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if ( c == '{' || c == '(' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;

                char top = stack.pop();
                if (top == '{' && c != '}') return false;
                if (top == '(' && c != ')') return false;
                if (top == '[' && c != ']') return false;
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println(isValid("()"));
    }
}
