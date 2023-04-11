package class16;

// leetCode330
// 给定一个已排序的正整数数组 nums ，和一个正整数 n。从 [1, n] 区间内选取任意个数字补充到 nums 中，使得 [1, n]
// 区间内的任何数字都可以用 nums 中某几个数字的和来表示。
// 请返回满足上述要求的最少需要补充的数字个数。

public class SupplyArray {

    // 你有没有发现，任意给一个正整数，都一定可以拆分成二进制形式。比如：73 == 64 + 8 + 1
    // 假如nums为空的情况下，任意给一个n，用二进制的形式去增加数字是最优解。假设nums为空，并且n=73，初始化一个变量
    // range=0。range和上一题的含义一样，表示从1～range都是可以完成的。我们需要填充1、2、4、8、16、32、64。
    // 通过上面的讲解应该大概有了一点感觉。现在就来一个更普通的情况。nums=[4, 7, 13, 20] n=59.
    // 当我们来到4时，我们希望range能达到3，表示1～3都完成了，但此时range==0，所以需要补数，每次补一个 range+1 是最
    // 高性价比的，所以 0 += 0 + 1 == 1,此时补了一个1之后，range就变成了1，还没到期望的3，于是再补一个range+1
    // 1 += 1 + 1 == 3，来到了3，于是补了两个数让range到了希望的位置，此时range可以直接累加上4了，直接将范围扩充到7。
    // 这样是最有效的使用数组元素的情况。所以每到一个元素时，就需要先做一些操作使得range==nums[i]-1，并且在整个遍历数
    // 组期间还需要判断 range是否>=n，如果为真，则直接结束。来到7时，发现 7 <= range + 1，直接加上元素，使得
    // range==14，然后来到了13，发现 13 <= range + 1，于是也可以直接加上去，此时range==27，同样，直接加上20，
    // range==47，此时还没达到要求，就只能自己增长了，没办法使用数组里的元素了。自增长和最开始是一样的，此时一直 ：
    // range += range + 1，直到range >= n

    public static int minPatches(int[] nums, int n) {
        int patches = 0; // 缺多少个数字
        long range = 0;  // 已经完成了1~range范围上的每个数字
        for (int num : nums) {
            while (range < num - 1) {
                range += range + 1;
                patches++;
                if (range >= n)
                    return patches;
            }
            // 此时range == num - 1，可以直接扩充成 range += num
            range += num;
            if (range >= n)
                return patches;
        }
        while (range < n) {
            range += range + 1;
            patches++;
        }
        return patches;
    }

}
