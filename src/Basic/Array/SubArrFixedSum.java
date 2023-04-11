package Basic.Array;


import java.util.HashMap;

public class SubArrFixedSum {
    // 1.給定一个非负数组成的无序数組arr，給定一个正整数値k，找到arr的所有子数组里，哪个子数組的累加和等于k, 并且是长度
    // 最大的返回其长度

    // 思路：用滑动窗口做。如果此时窗口内的和等于k，收集答案，并且R++；如果窗口内的和大于k，L++；如果窗口内的和小于k，R++，
    // 这样一定能找到答案。
    public static int longestSubArrUnderFixedSum(int[] arr, int k){
        if (arr == null || arr.length == 0 || k < 0)
            return -1;
        int L = 0, R = 0, sum = arr[0];
        int N = arr.length;
        int res = 0;
        while (R <= N - 1){
            if (sum < k){  // 小于
                R++;
                if (R == N)
                    break;
                sum += arr[R];
            } else if (sum == k) { // 相等
                res = Math.max(res, R - L + 1);
                R++;
                if (R == N)
                    break;
                sum += arr[R];
            } else { // 大于
                sum -= arr[L++];
            }
        }
        return res;
    }
    // 分析下这样做为什么合理？一开始L、R都为0，窗口一直向右扩大，直到超过或等于；等于的时候就收集答案并且继续向右扩，因为
    // 题目中说了非负，所以可能有0；如果超过了就说明以L为头的子数组都不可能组出k了，因为没有负数，所以再往右扩窗口内的值
    // 也不会减少，所以要换头。实际上这样做的逻辑就是：以某个元素为头，寻找所有符合的子数组，如果累加和一旦超过了，那就说明
    // 再也不可能以你为头找出适合的子数组了，所以L++，换一个新的头继续找出所有符合的子数组。所以说，如果想用滑动窗口，必然存在
    // 某种单调性，否则是不能使用的。如果数组中含有负数，那就不可以使用。
    // ========================================================================================================


    // 2.给定一个整数组成的无序数组arr,值可能正、可能负、可能0。给定一个整数值k，找到arr的所有子数组里，哪个子数组的累加和
    // 等于k，并且是长度最大的。返回其长度

    // 思路：此时元素值有负数，所以不能使用滑动窗口。一般找符合某种标准的子数组，通常用到的思维定式就是：以某个数结尾去找
    //      所有的子数组。我们需要做一个整个数组的累加和数组help，寻找以arr[i]结尾累加和为k的最长子数组，就是在找一个
    //      最小的x，0<=x<=i，使得help[x] == help[i] - k。

    public static int longestSubArrUnderFixedSumV2(int[] arr, int k){
        if (arr == null || arr.length == 0)
            return 0;
        int sum = 0;
        int res = -1;
        // key：某个累加和  value：最早出现的位置(从0到这个位置)
        HashMap<Integer, Integer> map = new HashMap<>();
        // 假如k=8，help[i]==8, 此时的x就是-1，因为我们要从x+1～i算长度
        map.put(0, -1);
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (map.containsKey(sum - k))
                res = Math.max(res, i - map.get(sum - k));
            // 这里不需要考虑替换的问题，因为是从左到右的，如果出现了相同的累加和，后出现的肯定更长所以不用更新
            if (!map.containsKey(sum))
                map.put(sum, i);

        }
        return res;
    }
    // 这个模型很重要，因为有很多题都可以转换为此。比如，一个数组，元素可正可负可为0，求一个最长子数组，其中-1的数量和1的数量
    // 要一样多，子数组中可以包含别的元素。 将数组中除了-1，1以外的元素全部置为0，这样问题就变成了在这个数组中求累加和为0的
    // 最长子数组了，就回到了这个模型。
    // ========================================================================================================



    public static void main(String[] args) {
        int[] arr = {3, 5, -1, -3, 6, 8, 4, 0, -2, 1};
        int k = 2;
        System.out.println(longestSubArrUnderFixedSumV2(arr, k));
    }
}
