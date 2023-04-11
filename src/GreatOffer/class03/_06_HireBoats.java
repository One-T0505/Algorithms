package GreatOffer.class03;

// leetCode881
// 给定一个正数数组arr，代表若干人的体重，再给定一个正数limit，表示每艘船的载重量。每艘船最多坐两人，
// 且不能超过载重。想让所有的人同时过河，并且用最好的分配方法让船尽量少。返回需要最少的船数。


import utils.arrays;

import java.util.Arrays;

public class _06_HireBoats {

    // 先将体重数组排序，然后找到<=(limit/2)最右的位置L，L的下一个位置记为R，R是>(limit/2)的。用下面的例子来
    // 讲解流程。假如arr排序完是这样的：
    //                                               |
    //            ✓   ✓   ✓   ✓   ✓                  |   ✓  ✓  ✓  ✓  ✓
    //    0   1   2   3   4   5   6   7   8   9  10  |  11 12 13 14 15
    //    1   1   1   1   1   2   3   5   5   5   5  |   6  6  8  8  9
    //                    L               <--           R
    //                                               |__|
    //                                                n个
    //
    // L R 的位置如图所示。先让L左滑找到第一个能和R相配且不超过载重的位置。于是L来到了6号位置。再让R往右滑动，找到
    // 当前L能配的最远的地方，于是R来到了12。这里用到了一点贪心：让现在的L包含自己往左数n个数，用这n个数去配R开始的
    // 位置～R现在的位置.相配的用✓表示。然后L就来到了4号位置，R来到了13号位置，同样的，让R向右扩找到能相配的最远位
    // 置15，R的新开辟的距离为13～15=3，所以让L包含自己再数3个和右边的相配.所有打勾的数量除2，这时是右边先用完的，
    // 左边剩余的没打勾的所有再除2，就是需要的船只。打勾的除2是因为他们找到了那个和自己做一条船的；左边没打勾的除2是
    // 因为一开始划分时左边就是<=(limit/2)的，所以左边随便两个人做一条船肯定不会超重。
    //
    // 如果此时右边最后多了一个10。那么此时L在1号位置需要继续向左找到能和10相配的，但是一直找不到，所以L先出界了，
    // 于是应该这样处理。打勾的还是直接除2就行，左侧划过但没打勾的也直接除2；右边没用完的剩多少直接加多少，因为每个
    // 人都没法和别人配，只能单独自己一艘船。
    //                                               |
    //            ✓   ✓   ✓   ✓   ✓                  |   ✓  ✓  ✓  ✓  ✓
    //    0   1   2   3   4   5   6   7   8   9  10  |  11 12 13 14 15  16
    //    1   1   1   1   1   2   3   5   5   5   5  |   6  6  8  8  9  10
    //        L                           <--                            R
    //                                               |__|
    //                                                n个

    public static int hireBoats(int[] arr, int limit) {
        if (arr == null || arr.length < 1)
            return 0;
        int N = arr.length;
        Arrays.sort(arr);
        if (arr[N - 1] > limit) // 如果有一个人的体重超过载重，不可能完成
            return -1;
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


    // 在arr中找到<=target的最右的位置
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
    // ====================================================================================================


    // 方法2
    public static int numRescueBoats1(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int N = arr.length;
        Arrays.sort(arr);
        if (arr[N - 1] > limit) {
            return -1;
        }
        int lessR = -1;
        for (int i = N - 1; i >= 0; i--) {
            if (arr[i] <= (limit / 2)) {
                lessR = i;
                break;
            }
        }
        if (lessR == -1) {
            return N;
        }
        int L = lessR;
        int R = lessR + 1;
        int noUsed = 0;
        while (L >= 0) {
            int solved = 0;
            while (R < N && arr[L] + arr[R] <= limit) {
                R++;
                solved++;
            }
            if (solved == 0) {
                noUsed++;
                L--;
            } else {
                L = Math.max(-1, L - solved);
            }
        }
        int all = lessR + 1;
        int used = all - noUsed;
        int moreUnsolved = (N - all) - used;
        return used + ((noUsed + 1) >> 1) + moreUnsolved;
    }
    // ====================================================================================================


    // 方法3：首尾双指针的解法
    public static int numRescueBoats2(int[] people, int limit) {
        Arrays.sort(people);
        int ans = 0;
        int l = 0;
        int r = people.length - 1;
        int sum = 0;
        while (l <= r) {
            sum = l == r ? people[l] : people[l] + people[r];
            if (sum > limit) {
                r--;
            } else {
                l++;
                r--;
            }
            ans++;
        }
        return ans;
    }


    // for test
    public static void test(int testTime, int maxLen, int maxVal, int maxLimit) {
        for (int i = 0; i < testTime; i++) {
            int[] array = randomPositiveArray(maxLen, maxVal);
            int limit = (int) (Math.random() * maxLimit) + 1;
            int res1 = hireBoats(array, limit);
            int res2 = numRescueBoats1(array, limit);
            if (res1 != res2) {
                System.out.println("Failed");
                System.out.println("自己的方法：" + res1);
                System.out.println("标准的方法：" + res2);
                arrays.printArray(array);
                System.out.println("船的载重：" + limit);
                return;
            }
        }
        System.out.println("AC");
    }


    private static int[] randomPositiveArray(int maxVal, int maxSize) {
        int len = (int) (Math.random() * maxSize) + 1;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * maxVal) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        test(5000000, 30, 50, 50);
//        int[] arr = {2, 4, 5, 7, 10, 10, 12, 24, 25, 27, 28, 28, 31, 32, 32, 37, 38, 39, 42, 46, 46};
//        int limit = 46;
//        System.out.println(hireBoats(arr, limit));
//        System.out.println(numRescueBoats1(arr, limit));
    }
}
