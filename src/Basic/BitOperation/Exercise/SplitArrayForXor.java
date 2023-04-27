package Basic.BitOperation.Exercise;

import utils.arrays;

import java.util.ArrayList;
import java.util.HashMap;

// 数组中所有数都异或起来的结果，叫做异或和。给定一个数组arr，可以任意切分成若干个不相交的子数组。
// 其中一定存在一种最优方案，使得切出异或和为0的子数组最多，返回这个最多数量。

// 数组切分  eg：arr=[3, 5, 2, 7] 长度为4。该数组可以切分成1个数组，只有一种，就是全部；也可以切分成2个数组，
//          从中间切开，或者切成[3]、[5, 2, 7]，或者[3, 5, 2]、[7]。也可以切成3个数组：[3]、[5]、[2, 7];
//          [3]、[5, 2]、[7];   [3, 5]、[2]、[7]
//          最多只能切分成4个数组，每个元素单独成一个数组。
public class SplitArrayForXor {

    // 思路：这道题总共分两步：1> 将数组切分  2>求每个子数组的异或和
    //      第二步很容易直接做一个前缀异或和数组，算子数组的异或和时可以直接利用该辅助数组获得。主要是如何切分的问题。


    // 先来个暴力方法，用递归实现所有的切分方式。
    public static int splitXor(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        int N = arr.length;
        int[] eor = new int[N];
        eor[0] = arr[0];
        for (int i = 1; i < N; i++)
            eor[i] = arr[i] ^ eor[i - 1];
        return f(eor, 1, new ArrayList<>());
    }


    // eor 是前缀异或和数组
    // parts 是切分区间的索引。如果parts中存放的是：2 5 7  那么表示将原数组切分成了：[0,2) [2,5) [5,7) [7,arr.length)
    //       是前闭后开的区间。所以主方法调用时初始传入的index为1，因为index决定的是index-1那里是否切断。
    // index 表示来到了哪个位置做决定，如果在index处决定切开，实际上表示的是index-1处那里被切断了；index这里是否被切断
    // 是由index+1来决定的。
    private static int f(int[] eor, int index, ArrayList<Integer> parts) {
        int res = 0;
        if (index == eor.length) { // 说明决策做完了
            parts.add(index);  // 最后一个边界
            res = zeroParts(eor, parts);
            parts.remove(parts.size() - 1);   // 还原数据，最后一个值是自己加的
        } else {
            int p1 = f(eor, index + 1, parts);   // 不切，现在待定的有index-1，index
            // 要切
            parts.add(index);
            int p2 = f(eor, index + 1, parts);
            // 还原数据，删除刚才加的index，如果不保持数据干净，那接下来的递归用到的part是，就不是按照规则去划分了
            parts.remove(parts.size() - 1);
            res = Math.max(p1, p2);
        }
        return res;
    }

    // parts中已经将数组切分好的索引全部存放完毕，现在要求现有的这种切分方案里，有几个子数组的异或和为0
    private static int zeroParts(int[] eor, ArrayList<Integer> parts) {
        int res = 0;
        int start = 0;
        for (int end : parts) {
            res += (eor[end - 1] ^ (start == 0 ? 0 : eor[start - 1])) == 0 ? 1 : 0;
            start = end;
        }
        return res;
    }
    // =====================================================================================================


    // 动态规划。这里用到的思想是假设答案法，很有难度，后续还会碰到。定义一张一维缓存表，dp[i]表示从0～i划分数组
    // 能得到的最优解。即：在0～i的最优划分下能得到的最多异或和为0的子数组数量。现在来分析dp[i]的可能性。现在假设0～i
    // 我们已经取得了最优划分，现在应如何讨论情况呢？讨论划分的最后一个部分的异或和是否为0。
    //  1.如果最后一个部分为0，那么剩余部分的异或和是等于0～i的异或和的，所以我们需要记录每一种异或和上一次出现的位置。
    //    如果最后一个部分为0，其实就是在找i之前最晚出现的前缀异或和等于0～i的异或和，假设为j，可以得到第一种转移方程：
    //    dp[i] = dp[j] + 1   1就表示剩下的最后那个异或和为0的部分。
    //  2.如果最优划分下，最后一部分的异或和不为0。这种情况下，最后一部分的状况包含两种情况：最后一部分仅包含arr[i]一个元素；
    //    包含不止一个元素。如果仅包含一个元素，那很简单 dp[i] = dp[i-1]，因为最后一个元素搞不出异或和为0并且还只有一个元素。
    //    如果最后一部分包含一些元素，比如k～i，那就再把最后这部分再切分成k～i-1，i～i，就是说单独把最后一个元素切出来。
    //    首先要明白我们已经是从0～i的最优解了，如果我们把最后一部分切开后，两边存在异或和为0的情况不可能存在，如果存在就不是最优解。
    //    所以切开的这两个部分的异或和都不为0，那就也是一样，dp[i] = dp[i-1]。所以，只要最后一个部分异或和不为0，不管最后一个
    //    部分包含几个元素，都能得到转移方程：dp[i] = dp[i-1]

    public static int splitXorV2(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        int N = arr.length;
        int[] dp = new int[N];
        int xor = 0;
        // key：某个前缀异或和   value：该前缀异或和上次出现的位置。
        // 比如0～i的异或和为x，而0～j的异或和也为x，那么记录就是：(x, j)
        // 如果value为-1，就说明没出现过
        // 因为是从左到右的，所以每一个key对应的value，都表示这个前缀异或和最晚出现的位置
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);    // 很关键
        for (int i = 0; i < N; i++) {
            xor ^= arr[i];
            if (map.containsKey(xor)) {  // 如果存在，就说明最后一个划分的部分异或和为0
                int pre = map.get(xor);
                dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
            }
            if (i > 0)
                dp[i] = Math.max(dp[i - 1], dp[i]);
            map.put(xor, i);
        }
        return dp[N - 1];
    }


    // for test
    public static void test(int testTime, int maxSize, int maxVal) {
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.randomNoNegativeArr(maxSize, maxVal);
            int res = splitXorV2(arr);
            int comp = splitXor(arr);
            if (res != comp) {
                succeed = false;
                arrays.printArray(arr);
                System.out.println(res);
                System.out.println(comp);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
    }


    public static void main(String[] args) {
        test(10000, 15, 30);
    }

}
