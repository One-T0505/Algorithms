package class04;

// leetCode2080
// 请你设计一个数据结构，它能求出给定子数组内一个给定值的出现次数。
// 请你实现 RangeFreqQuery 类：
//  1.RangeFreqQuery(int[] arr) 用下标从 0 开始的整数数组 arr 构造一个类的实例。
//  2.int query(int left, int right, int value) 返回子数组 arr[left...right] 中 value 的频率。

// 1 <= arr.length <= 10^5
// 1 <= arr[i], value <= 10^4
// 0 <= left <= right < arr.length
// 调用 query 不超过 10^5 次。

import utils.arrays;

import java.util.ArrayList;
import java.util.HashMap;

public class RangeFreqQuery {

    // 方法1：建立一张表，key为数组中的值，value为一个数组，存放的是该key出现的位置。对于要查询的元素，我们只需要在
    //       表中key对应的value数组中二分查找即可。比如(0, 3, 2)，先从表中找出2对应的位置数组，然后用二分分别找>=0的
    //       最左位置，以及<=3的最右元素。

    public HashMap<Integer, ArrayList<Integer>> map;

    public RangeFreqQuery(int[] arr) {
        map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (!map.containsKey(arr[i]))
                map.put(arr[i], new ArrayList<>());
            map.get(arr[i]).add(i);
        }
    }

    // 返回数组中>=target的最左的位置，若不存在返回-1
    private static int mostLeftGae(ArrayList<Integer> arr, int target) {
        int L = 0, R = arr.size() - 1;
        int index = -1;
        int mid;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr.get(mid) >= target) {
                index = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return index;
    }

    // 返回数组中<=target的最右的位置，若不存在返回-1
    private static int mostRightLae(ArrayList<Integer> arr, int target) {
        int L = 0, R = arr.size() - 1;
        int index = -1;
        int mid;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr.get(mid) <= target) {
                index = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return index;
    }

    public static int frequentQueryV2(int[] arr, int[] query) {
        if (arr == null || arr.length == 0 || query.length != 3 || query[0] > query[1] || query[0] < 0 ||
                query[1] >= arr.length)
            return 0;  // 返回0可能产生歧义
        int N = arr.length;
        HashMap<Integer, int[]> map = new HashMap<>();
        for (int j : arr) {
            if (!map.containsKey(j))
                map.put(j, new int[N]);
        }
        for (Integer key : map.keySet()) {
            int[] help = map.get(key);
            help[0] = arr[0] == key ? 1 : 0;
            for (int i = 1; i < N; i++) {
                help[i] = arr[i] == key ? help[i - 1] + 1 : help[i - 1];
            }
        }
        if (!map.containsKey(query[2]))
            return 0;
        int[] des = map.get(query[2]);
        return query[0] == 0 ? des[query[1]] : des[query[1]] - des[query[0] - 1];
    }
    // ======================================================================================================


    // 对于arr中每个不同值的元素都新建一个和arr等长的辅助数组。比如arr[2]=5，那就为这个5建立一个和arr等长的数组help5。
    // help5[i]表示：从0～i号位置有几个5；对于arr中每个不同的元素都需要建立一个数组。比如查询(4, 8, 2)，只需要用
    // help2[8] - help2[3]即可。做到查询是O(1)，但是预处理需要的时间会长点，并且空间比较浪费。假设数组长为N，并且每个
    // 元素都不同，那么额外空间复杂度就是: O(N^2)，这两种方法都可以，根据具体题目要求选择。

    // for test
    public static void test(int testTime, int maxLen, int maxVal) {
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.randomNoNegativeArr(maxLen, maxVal);
            int N = arr.length;
            int[] query = new int[3];
            int num1 = (int) (Math.random() * N);
            int num2 = (int) (Math.random() * N);
            query[0] = Math.min(num1, num2);
            query[1] = Math.max(num1, num2);
            query[2] = arrays.randomNoNegativeNum(maxVal);
            RangeFreqQuery fq = new RangeFreqQuery(arr);
            int res1 = fq.query(query[0], query[1], query[2]);
            int res2 = frequentQueryV2(arr, query);
            if (res1 != res2) {
                System.out.println("Failed");
                arrays.printArray(arr);
                System.out.println("查询是：" + "(" + query[0] + ", " + query[1] + ", " + query[2] + ")");
                System.out.println("方法1：" + res1);
                System.out.println("方法2：" + res2);
                return;
            }
        }
        System.out.println("AC");
    }
    // ======================================================================================================

    public static void main(String[] args) {
        test(1000000, 20, 40);
    }

    public int query(int left, int right, int value) {
        if (!map.containsKey(value))
            return 0;
        ArrayList<Integer> positions = map.get(value);
        int R = mostRightLae(positions, right);
        int L = mostLeftGae(positions, left);
        if (L == -1 || R == -1)
            return 0;
        return R - L + 1;
    }

}
