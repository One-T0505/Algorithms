package GreatOffer.class36;


// 来自腾讯
// 给定一个正数数组arr,代表每个人的体重。给定一个正数limit代表船的载重，所有船都是同样的载重量
// 每个人的体重都一定不大于船的载重
// 要求:
//  1.可以1个人单独一艘船
//  2.一艘船如果坐2人，两个人的体重相加需要是偶数，且总体重不能超过船的载重
//  3.一艘船最多坐2人
// 返回如果想所有人同时坐船，需要最少几艘船

import java.util.Arrays;

public class _09_Code {

    // 这是大厂刷题班第三节原题。leetCode881 题。
    // 问题原型是没有题目中的要求2的，其他要求一样。对于要求2，如果两个人的体重和为偶数，那么只可能是两个偶
    // 数或者两个奇数相加，所以把体重数组分成两个，一个奇数数组，一个是偶数数组，就变成了问题原型。
    public static int minBoat(int[] arr, int limit) {
        Arrays.sort(arr);
        int N = arr.length;
        int odd = 0;
        int even = 0;
        for (int num : arr) {
            if ((num & 1) == 0)
                even++;
            else
                odd++;
        }
        int[] odds = new int[odd];
        int[] evens = new int[even];
        for (int i = N - 1; i >= 0; i--) {
            if ((arr[i] & 1) == 0) {
                evens[--even] = arr[i];
            } else {
                odds[--odd] = arr[i];
            }
        }
        // 上面都只是在做partition
        return hireBoat(odds, limit) + hireBoat(evens, limit);
    }

    private static int hireBoat(int[] arr, int limit) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return 1;
        int N = arr.length;
        int L = mostRightEal(arr, limit >> 1);
        // 如果一开始数组排完序是：[13, 14] 而limit是22，我们要找<=11的最右边界，最后得到L==-1，所有元素均超过一半
        if (L == -1)
            return N;
        int R = L + 1;
        int res = 0;
        int leftRest = 0, rightRest = 0;
        while (L >= 0 && R <= N - 1) {
            int preL = L, preR = R;
            while (L >= 0 && arr[L] > limit - arr[R])
                L--;
            leftRest += preL - L;
            if (L >= 0) {  // L还在没越界的情况，说明找到了可以适配的位置，此时L的位置是第一个可以适配的位置
                while (R <= N - 1 && arr[R] <= limit - arr[L])  // 现在开始找L能配对的最远的位置
                    R++;
                // 从右边找到的能匹配的船只不能超过左边可以提供的
                int match = Math.min(R - preR, L + 1);
                res += match;
                L -= match;
                R = match < (R - preR) ? preR + match : R;
                if (L < 0)
                    rightRest += N - R;
            } else  // L直接滑出范围，说明被滑过的位置只能两两配对，没有右边的船和自己配对
                rightRest += N - R;
        }
        if (L >= 0)
            leftRest += L + 1;
        res += ((leftRest + 1) >> 1) + rightRest; // +1是为了向上取整
        return res;
    }



    private static int mostRightEal(int[] arr, int target) {
        if (target <= 0)
            return -1;
        int L = 0, R = arr.length - 1;
        int index = -1;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] <= target) {
                index = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return index;
    }
}
