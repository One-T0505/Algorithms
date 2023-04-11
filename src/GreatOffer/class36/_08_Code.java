package GreatOffer.class36;

import java.util.Arrays;

// 来自腾讯
// 给定一个数组arr，当拿走某个数a的时候，其他所有的数都+a
// 请返回最终所有数都拿走的最大分数
// 比如: [2,3,1]
//  当拿走3时，获得3分，数组变成[5,4]
//  当拿走5时，获得5分，数组变成[9]
//  当拿走9时，获得9分，数组变成[]
// 这是最大的拿取方式，返回总分17

public class _08_Code {

    // 思路就是每次都拿数组中最大的就能凑出最大值
    // 下面的公式也是总结出来的，比较简单。假如有4个数，排完序后是 [a, b, c, d]
    // 第一次拿d，   数组变成 [a+d, b+d, c+d]
    // d + c + d   数组变成 [a+c+2d, b+c+2d]
    // ...
    public static int pick(int[] arr) {
        int N = arr.length;
        Arrays.sort(arr);
        int res = 0;
        for (int i = N - 1; i >= 0; i--)
            res = (res << 1) + arr[i];

        return res;
    }
}
