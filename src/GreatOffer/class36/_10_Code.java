package GreatOffer.class36;


// 来自腾讯
// 给定一个字符串str,和一个正数k  返回长度为k的所有子序列中，字典序最大的子序列

public class _10_Code {

    // 该题目要使用单调栈结构。总体流程是：遍历到当前字符cur时，如果cur<=栈顶元素top，那么cur可以直接入栈，
    // 否则就要弹出top，直到碰到一个元素>=cur或者栈空为止。
    // 需要注意的边界情况：
    //  1.如果遍历完了整个字符串，此时栈里元素个数超过了k个，比如k=4 此时栈底到栈顶是：z y x d b a
    //    那么取最底下的k个就是答案 z y x d 字典序大于 y x d b
    //  2.如果当前字符cur，可以让栈里连续弹出若干元素，cur及字符串后面所有的字符数量为n，当cur让栈里不停弹出元素，
    //    直到栈里剩下k-n个时，即便还可以弹出，那也可以停止了，因为再弹出，就不可能再凑出k个元素了。
    //    比如栈里的情况是：z e d c b a   k==5，字符串还剩 [f b c] 这些没遍历，当前来到了f，本来f可以让
    //    e d c b a 都弹出，但是当弹出a b c d后，栈里就只剩了 z e，而剩下的字符串就只剩f b c了，如果再把
    //    d弹出，就不可能凑出5个元素了，所以如果碰到这种情况，弹出c后就停止。

    public static String maxSubsequence(String s, int k) {
        if (k < 1 || s == null || s.length() < k)
            return "";
        char[] chs = s.toCharArray();
        int N = chs.length;
        char[] stack = new char[N];  // 手动模拟栈
        int top = -1;
        for (int i = 0; i < N; i++) {
            // top > -1 说明栈不为空
            // (top + 1) + (N - 1 - i + 1) 栈里元素个数+剩余字符数 > k
            while (top > -1 && stack[top] < chs[i] && top + 1 + N - i > k)
                top--;
            if (top + 1 + N - i == k)
                return String.valueOf(stack, 0, top + 1) + s.substring(i);
            stack[++top] = chs[i];
        }
        return String.valueOf(stack, 0, k);
    }
}
