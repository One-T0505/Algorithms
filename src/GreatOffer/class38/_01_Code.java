package GreatOffer.class38;

// 来自字节
// 给定两个数a和b  第1轮，把1选择给a或者b   第2轮，把2选择给a或者b   第i轮，把i选择给a或者b
// 想让a和b的值一样大，请问至少需要多少轮?

public class _01_Code {

    // 一开始判断a、b的大小，假设大的那个是max，小的那个是min，max-min的差值为s。
    // 假设分到第i轮完成了任务，一共分给a、b的和就是1～i的累加和，让 (i*(i+1))/2 == sum，
    // 如果sum里分给max的数是x，分给min的数是y，那么一定有 y - x == s。
    // y - x == s    1
    // y + x == sum  2
    // 利用这两个方程可以求出：y == (s + sum)/2   x == (sum - s)/2  这里的除以2是数学意义，不是计算机上的除以2
    // y>x，x>=0  y>0   y和x都是整数，所以说， sum-s 必然是偶数才行，s是固定的，所以就是说要找到最小的sum
    // 使得 sum - s为偶数就行，sum中是包含i的，所以找出sum就能解题。
    // 这里还有一个优化的点：比如一开始给的两个数的差值比较大，比如s==200，我们要找最小的sum使得 sum - s 最小的偶数，
    // 那首先至少要让 sum >= s 才行，较快的方法是不断让sum*2，这样能很快达到，并且让找sum这个事变成O(logN)，
    // 比如当sum==128时，sum < s，而sum==256时，sum>=s了，那么s就必然在128和256之间，然后再二分

    public static int minStep(int a, int b) {
        if (a == b)
            return 0;
        int s = Math.abs(a - b);
        // search方法就是找到满足 sum >= s 的最小的i
        // 因为 sum == (i * (i + 1)) / 2   其实就是找到满足 i * (i + 1) >= 2s 的最小的i
        // 所以在下面的方法里，将 s<<1 扩大了一倍
        int i = search(s << 1);
        while ((((i * (i + 1)) >> 1) - s) % 2 != 0) {
            i++;
        }
        return i;
    }


    // 此时传入的s其实已经是2s了
    private static int search(int s2) {
        int L = 0;
        int R = 1;
        while (R * (R + 1) < s2) {
            L = R;
            R *= 2;
        }
        // 出来这个循环时，L是小于s2的最大2的次幂，R是>=s2的最小的2的次幂
        // 要在此时的L～R二分查找s2
        int res = 0;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if ((mid * (mid + 1) >= s2)) {
                res = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return res;
    }
}
