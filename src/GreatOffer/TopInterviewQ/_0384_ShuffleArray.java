package GreatOffer.TopInterviewQ;


// 给你一个整数数组 nums，设计算法来打乱一个没有重复元素的数组。打乱后，数组的所有排列应该是等可能的。
// 实现 Solution class:
//  1.Solution(int[] nums) 使用整数数组 nums 初始化对象
//  2.int[] reset() 重设数组到它的初始状态并返回
//  3.int[] shuffle() 返回数组随机打乱后的结果

public class _0384_ShuffleArray {

    private int[] ori;
    private int[] shuffle;
    private int N;

    public _0384_ShuffleArray(int[] nums) {
        ori = nums;
        N = nums.length;
        shuffle = new int[N];
        System.arraycopy(ori, 0, shuffle, 0, N);
    }

    public int[] reset() {
        return ori;
    }

    public int[] shuffle() {
        for (int i = N - 1; i >= 1; i--) {
            int des = (int) (Math.random() * (i + 1));
            int tmp = shuffle[des];
            shuffle[des] = shuffle[i];
            shuffle[i] = tmp;
        }
        return shuffle;
    }
}
