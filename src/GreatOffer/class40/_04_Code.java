package GreatOffer.class40;

// 给定两个数组A和B，长度都是N
// A[i]不可以在A中和其他数交换，只可以选择和B[i]交换(0<=i<N)
// 你的目的是让A有序，返回你能不能做到

public class _04_Code {

    public static boolean isSorted(int[] a, int[] b) {
        // 主方法调用递归时第一个要做决策的位置就是0位置，其前面没有任何元素，所以last可以设为系统最小值，
        // 因为第一个数可以是任意值
        return f(a, b, 0, Integer.MIN_VALUE);
    }

    // 现在来到i位置决策了，前面决策完的数已经排序好了，且最大值达到了last，所以i位置的目标就是>=last
    private static boolean f(int[] a, int[] b, int i, int last) {
        if (i == a.length)
            return true;
        if (a[i] >= last && f(a, b, i + 1, a[i]))
            return true;
        if (b[i] >= last && f(a, b, i + 1, b[i]))
            return true;
        return false;
    }
    // 这个题目要求的是让a升序，如果让a降序，只需要把递归中的一些比较符号修改下即可，再把系统最小值改成最大值。


    // 如果将题目再升级下难度，变成最终能否让a和b都有序  那就在递归中再多设一个参数，lastB就是b[i]要满足的要求
    private static boolean g(int[] a, int[] b, int i, int lastA, int lastB) {
        if (i == a.length)  // 说明两个数组已经同步遍历完了
            return true;
        // 不用交换
        if (a[i] >= lastA && b[i] >= lastB && g(a, b, i + 1, a[i], b[i]))
            return true;
        // 需要交换
        if (a[i] >= lastB && b[i] >= lastA && g(a, b, i + 1, b[i], a[i]))
            return true;
        return false;
    }
}
