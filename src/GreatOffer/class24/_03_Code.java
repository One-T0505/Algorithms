package GreatOffer.class24;

import java.util.HashMap;

// 给定一个正数数组arr，长度一定大于6
// 一定要选3个数字做分割点，从而分出4个部分，并且每部分都有数
// 分割点的数字直接删除，不属于任何4个部分中的任何一个。
// 返回有没有可能分出的4个部分累加和一样大 如: {3,2,3,7,4,4,3,1,1,6,7,1,5,2}
// 可以分成{3,2,3}、{4,4}、 {1,1,6}、 {1,5,2}。 分割点是不算的!

public class _03_Code {

    // 先枚举第一刀的位置，如果第一刀的左侧的累加和为a，那么在第一刀的右侧和第二刀之间需要有一段子数组的累加和也要为
    // a，所以，如果第一刀可行的话，那0..s2-1的累加和应该为2a+arr[s1]，如果验证第一刀可行，那再继续找第二刀的位置.
    // s2为第二刀的位置，那么0..s3-1上的累加和应该为 3a+arr[s1]+arr[s2]，如果存在这样的累加和，那么第三刀也可以确定。
    //      |
    // ..a..s1..a..s2..a..           如果第一刀的位置不可以，那就选别的位置做第一刀。该问题出发的基础就是枚举第一刀的
    //      |                        位置；因为第一刀确定了，那么每一段的和都可以确定了。
    //
    public static boolean splitParts(int[] arr) {
        if (arr == null || arr.length < 7)
            return false;
        int N = arr.length;
        // key是累加和，value是对应的位置。如果存在这样的一条记录：(11, 5) 那表示：arr[0..4]的累加和为11
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = arr[0];
        // 记录前缀和，并记录对应的位置
        for (int i = 1; i < N; i++) {
            map.put(sum, i);
            sum += arr[i];
        }
        // 此时sum等于整个数组的累加和
        // 第一刀左侧的累加和
        int leftSum = arr[0];
        // 枚举第一刀的位置
        for (int s1 = 1; s1 < N - 5; s1++) {
            int checkSum = (leftSum << 1) + arr[s1];
            if (map.containsKey(checkSum)) {
                int s2 = map.get(checkSum);
                checkSum += leftSum + arr[s2];
                if (map.containsKey(checkSum)) {
                    int s3 = map.get(checkSum);
                    if (checkSum + arr[s3] + leftSum == sum)
                        return true;
                }
            }
            leftSum += arr[s1];
        }
        return false;
    }
}
