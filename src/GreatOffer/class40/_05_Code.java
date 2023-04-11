package GreatOffer.class40;

import java.util.PriorityQueue;

// 来自去哪儿网
// 给定一个arr，里面的数字都是0~9 你可以随意使用arr中的数字，哪怕打乱顺序也行
// 请拼出一个能被3整除的，最大的数字，用str形式返回

public class _05_Code {

    public static String maxMod(int[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        if (arr.length == 1)
            return arr[0] % 3 == 0 ? String.valueOf(arr[0]) : "";
        int N = arr.length;
        int len = findMaxLen(arr);  // 找到能整除3的数的最大长度
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (heap.size() < len)
                heap.add(arr[i]);
            else if (heap.size() == len && arr[i] > heap.peek()) {
                heap.poll();
                heap.add(arr[i]);
            }
        }
        StringBuilder res = new StringBuilder();
        while (!heap.isEmpty())
            res.append(heap.poll());
        return res.reverse().toString();
    }

    private static int findMaxLen(int[] arr) {
        return 0;
    }
    // ===========================================================================================


    // 课程中的方法
    public static String maxMod2(int[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        if (arr.length == 1)
            return arr[0] % 3 == 0 ? String.valueOf(arr[0]) : "";
        String res = f(arr, 0, 0);
        return res.equals("$") ? "" : res;
    }


    // arr中的数字一定是0~9
    // arr是经过排序的，并且是从大到小排序，比如[9, 8, 7, 7, 7, 3, 1]等
    // 这个递归函数的含义:
    // 在arr[i...一直到最后]上做选择，arr[0... i-1]就当不存在
    // 每个位置的字符可以要、也可以不要，但是选出来的数字拼完之后的结果，在%3之后，余数一定要是mod!
    // 返回在上面设定的情况下，最大的数是多少? 如果存在这样的数，返回字符串的形式
    // 如果不存在这样的数，返回特殊字符串，比如"$"， 代表不可能
    private static String f(int[] arr, int i, int mod) {
        if (i == arr.length)
            return mod == 0 ? "" : "$";
        // 情况1  不要当前数
        String p1 = f(arr, i + 1, mod);
        // 情况2  要当前数
        int curMod = arr[i] % 3;
        int nextMod = compute(curMod, mod);
        String p2 = "$";
        String next = f(arr, i + 1, nextMod);
        if (!next.equals("$"))
            p2 = arr[i] + next; // 数字 + 字符串 的形式  数字直接被拼起来
        // 到这里，两种情况的字符串已经完成了，接下来的工作就是比大小了
        if (p1.equals("$") && p2.equals("$")) // 两个都无效 那就返回无效
            return "$";
        if (p1.equals("$") ^ p2.equals("$"))  // 但凡其中一个无效 一个有效  那就返回有效的那个
            return p1.equals("$") ? p2 : p1;
        // 直销到这里时说明两个都有效
        if (p1.length() != p2.length())
            return p1.length() > p2.length() ? p1 : p2;
        // 两个长度相等
        return p1.compareTo(p2) < 0 ? p2 : p1;
    }


    // 当前得到的余数是curMod，目标是凑出mod，那么还需要多少？将需要的值返回
    // 比如说，我们最后希望得到的余数是0，然后当前数arr[i] % 3 == 0  那么递归到i+1位置时还需要凑的余数就是0
    // 如果当前数arr[i] % 3 == 1  那么递归到i+1位置时还需要凑的余数就是2 才能让最后的结果%3==0
    // 如果当前数arr[i] % 3 == 2  那么递归到i+1位置时还需要凑的余数就是1

    private static int compute(int curMod, int mod) {
        int nextMod = 0;
        switch (mod) {
            case 0:
                if (curMod == 0)
                    nextMod = 0;
                else if (curMod == 1)
                    nextMod = 2;
                else if (curMod == 2)
                    nextMod = 1;
                break;

            case 1:
                if (curMod == 0)
                    nextMod = 2;
                else if (curMod == 1)
                    nextMod = 1;
                else if (curMod == 2)
                    nextMod = 0;
                break;

            case 2:
                if (curMod == 0)
                    nextMod = 1;
                else if (curMod == 1)
                    nextMod = 0;
                else if (curMod == 2)
                    nextMod = 2;
                break;
        }
        return nextMod;
    }
}
