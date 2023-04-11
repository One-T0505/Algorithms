package class07;

import utils.arrays;

// 给定一个非负数组成的数组，长度一定大于1。想知道数组中哪两个数&的结果最大。返回这个最大结果。

public class MaxAnd {
    // 思路：希望高位是1才能得到尽量大的结果。因为整个数组非负，所以最高位符号位都为0。遍历数组，统计第30位上为1的元素个数，
    //      如果少于2个，则再统计下一位；如果刚好等于两个，那么结果就是这两个数与的结果；如果大于2个，则将数组剩余元素全部删除。
    //      删除这件事可以在原数组上完成，将被删除的元素都放在数组最后面，用一个指针指向有效区结尾即可。

    public static int maxAnd(int[] arr) {
        if (arr == null || arr.length < 2)
            return -1;
        int res = 0;
        int trash = arr.length;  // 垃圾区，被淘汰元素的区域
        for (int move = 30; move >= 0; move--) {
            int i = 0;   // 每一轮都重新从0位置开始遍历
            // 每一轮开始时要先记录trash区的位置，因为在当前遍历时，会改变trash的位置，
            // 但是最后遍历完后发现符合要求的不足2个，那么在当前轮次被换进垃圾区的不应该真的被删除。可以看下面的例子。
            int tmp = trash;
            while (i < trash) {
                if ((arr[i] & (1 << move)) == 1)
                    i++;
                else
                    arrays.swap(arr, i, --trash);
            }
            if (trash == 2)
                return arr[0] & arr[1];
            else if (trash < 2)
                trash = tmp;
            else
                res |= (1 << move);
        }
        return res;
    }
    // 假如数组arr=[9, 7, 5, 6]. 遍历完后发现只有9的第3位为1，所以 trash == 1，此时结果小于2个，于是trash应该恢复至
    // 3，即恢复至上一次的位置。为什么要这样做呢？因为与操作的结果和数值大小无关，这4个数中，与结果最大的是 7 & 6；如果最高位
    // 不足两个，那么就要重新推翻这种关系，所有元素都变成同一起跑线上了。
}
