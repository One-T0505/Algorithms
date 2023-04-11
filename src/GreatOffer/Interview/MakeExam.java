package GreatOffer.Interview;

import java.util.Arrays;

// 这道题是快手的原题。
// 给定一个正整数数组arr，每个元素表示一道题目的难度，可能会出现难度相等的情况，但是只要索引不同就说明是不同的题目。
// 现在让你出一份试卷，唯一的要求是：前一道题目的难度 不能超过 后一道题目的难度 + M
// 试卷上任意两道相邻的题目都需要满足，问一共有多少种出试卷的方法。试卷的总题量和arr长度一致。


public class MakeExam {

    // 这是一个从左到右的尝试模型。我们需要先对arr排序，让其难度递增。假设我们使用从0..i的所有题目编排出了一套
    // 不合法的试卷，那么不管i+1号题目难度如何，都改变不了这个事实。比如：arr排完序后 arr = [2, 12, 16]， M == 4
    // 只用前两道题，我们排出了一种错误的形式 [12, 2]  那么下一题难度为16，不管放在三个位置的哪个，结果都是不合法的。
    // 我们想说明的是，前面不合法的结果我们不需要考虑，只需要考虑前面还剩多少个合法的结果，在此基础上能增殖多少种
    // 新的可能。


    // 最重要的就是可能性分析，假设之前在0..i-1这些题目上找出了所有合法的试卷种类a种，现在来到了第i号题目，要重新整合
    // 前面a种合法结果，假设M==3。
    //  1.首先a种合法结果，每一种情况下，都把i号题目放在试卷最后，必然都是一种新的合法结果，因为题目难度排完序了，
    //    0..i-1的题目不可能有大于i号题目的，所以将i号题目放在最后必然合法。
    //  2.找出i号题目能插在哪些题目前面？ 假设i号题目难度为8，那么i号题目可以插在所有难度大于等于(8 - M)的题目前，
    //    8-M==5，假设0..i-1号题目有n个题目难度大于等于5，那么这n个位置，每个位置都可以插入i号题目，构成一种
    //    新的合法结果；因为有a种合法结果，所以每一种都有n个位置可以插入，因为每种合法结果使用的都是0..i-1这些题目，
    //    所以，种类一样，只是排列不同而已，所以一共可以新产生：n*a种新的结果。


    public static int designExam(int[] arr, int M) {
        if (arr == null || arr.length == 0 || M < 1)
            return 0;
        Arrays.sort(arr);
        int pre = 1;
        for (int i = 1; i < arr.length; i++) {
            int p1 = pre;
            int p2 = pre * mostLeftEag(arr, 0, i - 1, arr[i] - M);
            pre = p1 + p2;
        }
        return pre;
    }


    // 在arr[L..R]上找到 >= t的元素有多少个
    private static int mostLeftEag(int[] arr, int L, int R, int t) {
        int ori = R;
        int index = R + 1;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] >= t) {
                R = mid - 1;
                index = mid;
            } else
                L = mid + 1;
        }
        return ori - index + 1;
    }
    // ===================================================================================


    // 暴力方法，用作对数器  生成所有排列，一个一个验证
    public static int ways1(int[] arr, int m) {
        if (arr == null || arr.length == 0 || m < 1) {
            return 0;
        }
        return process(arr, 0, m);
    }

    private static int process(int[] arr, int index, int m) {
        if (index == arr.length) {
            for (int i = 1; i < index; i++) {
                if (arr[i - 1] > arr[i] + m) {
                    return 0;
                }
            }
            return 1;
        }
        int ans = 0;
        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            ans += process(arr, index + 1, m);
            swap(arr, index, i);
        }
        return ans;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    // =======================================================================================


    // 为了测试
    private static int[] randomArray(int len, int value) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (value + 1));
        }
        return arr;
    }

    private static void printArray(int[] arr) {
        for (int e : arr)
            System.out.print(e + " ");
        System.out.println();
    }


    private static void test() {
        int N = 10;
        int value = 20;
        int testTimes = 1000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int len = (int) (Math.random() * (N + 1));
            int[] arr = randomArray(len, value);
            int m = (int) (Math.random() * (value + 1));
            int ans1 = ways1(arr, m);
            int ans2 = designExam(arr, m);
            if (ans1 != ans2) {
                System.out.println("出错了!");
                System.out.println("暴力答案：" + ans1);
                System.out.println("优化方法：" + ans2);
                System.out.println("M：" + m);
                printArray(arr);
                return;
            }
        }
        System.out.println("测试结束");
    }


    public static void main(String[] args) {
        test();
//        int[] arr = {4, 8, 11, 12, 12, 13, 13, 14, 18, 20};
//        System.out.println(mostLeftEag(arr, 0, arr.length - 1, 21));
    }
}
