package Hard;

// leetCode875
// 珂珂喜欢吃香蕉。这里有 n 堆香蕉，第 i 堆中有 piles[i] 根香蕉。警卫已经离开了，将在 h 小时后回来。
// 珂珂可以决定她吃香蕉的速度 k（单位：根/小时）。每个小时，她将会选择一堆香蕉，从中吃掉 k 根。如果这
// 堆香蕉少于 k 根，她将吃掉这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉。
// 珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。
// 返回她可以在 h 小时内吃掉所有香蕉的最小速度 k（k 为整数）。
// 注意：速度可控的意思说，没确定速度是多少时，是可以选择的，但是一旦确定了速度，之后吃每一堆香蕉时的速度就固定了；
//      不是说吃不同堆的香蕉时速度可变。

// 1 <= piles.length <= 10^4
// piles.length <= h <= 10^9
// 1 <= piles[i] <= 10^9

public class EatBananas {

    public static int minEatingSpeed(int[] piles, int h) {
        int L = 1;
        int R = 0;
        // 让R表示数组的最大值
        for (int pile : piles)
            R = Math.max(R, pile);
        // 那么上下界就找到了[L, R]  表示吃完这堆最多的香蕉，最小的速度是1根/小时，最快且比较实际
        // 的是R根/小时，因为超过R了就没意义了，反正都是1小时，那为什么不遵循懒惰的思想呢？题意说越慢越好。
        // 这样就可以在[L, R]范围上二分了，确定一个mid速度，然后看吃完所有香蕉的最小速度speed是多少
        int res = 0;
        int mid = 0;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (hours(piles, mid) <= h) {
                res = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return res;
    }


    // 速度为speed，吃完所有香蕉需要多少小时
    private static long hours(int[] piles, int speed) {
        long res = 0;
        for (int pile : piles)
            res += (pile + speed - 1) / speed;  // 向上取整

        return res;
    }
}
