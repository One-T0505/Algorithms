package Recurse;

// 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
// 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
// "123"  "132"  "213"  "231"  "312"  "321"
// 给定 n 和 k，返回第 k 个排列。

import java.util.ArrayList;
import java.util.HashSet;

public class _0060_Code {


    // 这是自己写的暴力递归，虽然方法最终是写对了，但是超时了，补过这个方法给了一个很好的指引
    public static String getPermutation(int n, int k) {
        if (n < 1 || k < 1)
            return null;
        HashSet<Integer> set = new HashSet<>();
        ArrayList<String> list = new ArrayList<>();
        dfs(0, n, "", list, set);
        return list.get(k - 1);
    }

    private static void dfs(int i, int n, String pre, ArrayList<String> list, HashSet<Integer> set) {
        if (i == n)
            list.add(pre);
        else {
            StringBuilder sb = new StringBuilder(pre);
            for (int j = 1; j <= n; j++) {
                if (!set.contains(j)){
                    set.add(j);
                    sb.append(j);
                    dfs(i + 1, n, sb.toString(), list, set);
                    sb.deleteCharAt(sb.length() - 1);
                    set.remove(j);
                }
            }
        }
    }
    // ============================================================================================




    // 方法2  递归  该方法是我自己写的，题解中没有给出
    // 需要用到两个预处理结构，一个是阶乘和数组factor，一个是标记元素是否使用过的数组used

    public static int TOP;

    public static String getPermutation2(int n, int k) {
        if (n < 1 || k < 1)
            return null;
        TOP = n;
        int[] factor = new int[n + 1];  // 阶乘预处理数组
        factor[0] = 1;
        for (int i = 1; i <= n; i++) {
            factor[i] = factor[i - 1] * i;
        }
        // used[i] 表示i这个数字是否使用过了
        boolean[] used = new boolean[n + 1];
        return dfs(n, k, factor, used);
    }


    // n表示要组装几位数    k表示要找出这n位数的所有排列下的第k个排列
    // 返回值：返回n位数的排列下第k个排列
    private static String dfs(int n, int k, int[] factor, boolean[] used) {
        if (n == 0)
            return "";
        // 这里要用到一些数学技巧来将问题规模变小。举个例子：比如n==5  k==67  这时候应该怎么处理呢？
        // 我们知道5位数的排列，最高位可以分别是1、2、3、4、5，每种情况下都对应 4! 种排列；所以，k==67，
        // 我们可以确定出最高位必然是3，算法是：[67 / 4!]向上取整。然后就可以把问题规模缩小成了：在n==4的，
        // 情况下，去找出k==67-48==19的排列，然后以此类推，递归下去

        int p = factor[n - 1]; // 先取出阶乘
        // cur就是确定当前位应该填多少，但是cur表示的是，在1～n之间，在没使用的那些数中找到第cur个小的。
        // 这个完全可以自己举个例子就知道了。比如 n==5，k==90
        // 那么我们可以确定最高位应该填4，然后k就变成了18，次高位：[(90 - 72) / 3!]向上取整是 4，但是最高位已经
        // 填过4了，这里应该填的是5

        int cur = (k + p - 1) / p;  // 向上取整
        int rest = k - (cur - 1) * p;  // 将k缩小
        // 下面的for循环就是用来寻找，1～n中第cur个没使用过的数
        // 这里是可以优化的点,怎么让找到目标值更快呢？
        for (int i = 1; i <= TOP; i++) {
            if (!used[i])
                cur--;
            if (cur == 0){
                cur = i;
                break;
            }
        }
        used[cur] = true;
        return cur + dfs( n - 1, rest, factor, used);
    }
    // ===========================================================================================




    // 方法3用的也是上面递归的思想，只不过做了一些优化
    public static String getPermutation3(int n, int k) {
        if (n < 1 || k < 1)
            return null;
        int[] factor = new int[n + 1];  // 阶乘预处理数组
        factor[0] = 1;
        for (int i = 1; i <= n; i++) {
            factor[i] = factor[i - 1] * i;
        }
        boolean[] used = new boolean[n + 1];
        ArrayList<Integer> elem = new ArrayList<>();
        // 为什么要加0进去，这样下标就一致了，elem[i]==i  递归里算cur的时候就可以直接取了，不用下标转换
        for (int i = 0; i <= n; i++) {
            elem.add(i);
        }
        return dfs(n, k, factor, used, elem);
    }


    private static String dfs(int n, int k, int[] factor, boolean[] used, ArrayList<Integer> elem) {
        // 经过分析发现，当n==1时，k必然为1，所以不需要将n==0作为base case了
        if (n == 1){
            String res = String.valueOf(elem.get(1));
            elem.remove(1);
            return res;
        }
        int p = factor[n - 1]; // 先取出阶乘
        int cur = (k + p - 1) / p;  // 向上取整
        int rest = k - (cur - 1) * p;  // 将k缩小
        // 用ArrayList来装元素，每次删除掉使用的元素，那么就会自动填补上去。这里只是写代码方便了，ArrayList里
        // remove也是要花时间的。
        int fact = elem.get(cur);
        elem.remove(cur);
        used[cur] = true;
        return fact + dfs( n - 1, rest, factor, used, elem);
    }



    public static void main(String[] args) {
        System.out.println(getPermutation2(5, 90));
    }
}
