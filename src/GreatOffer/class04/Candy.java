package class04;


public class Candy {

    // leetCode135
    // 问题一>>>>>>>>>>>>>>>>>>>>>>>>
    // n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。你需要按照以下要求，给这些孩子分发糖果：
    //  1.每个孩子至少分配到 1 个糖果;
    //  2.相邻两个孩子评分更高的孩子会获得更多的糖果;
    // 请你给每个孩子分发糖果，计算并返回需要准备的最少糖果数目。
    // eg: ratings=[1, 2, 2]  分发的糖果为：[1, 2, 1]  虽然两个2相等，但是没有规定相等情况下的约束。
    //     ratings=[5, 6, 9, 2, 1, 0]  分发的糖果为：[1, 2, 4, 3, 2, 1]


    // 思路：需要两个辅助数组left和right；从左遍历原数组生成left，从右遍历生成right数组。从左遍历时，
    //      将left[0]设置为1，如果arr[1]>arr[0]，那么left[1]就在left[0]的基础上+1，如果严格递增就一直加下去；
    //      如果arr[i]==arr[i-1]，直接让left[i]置为1，再重新累加下去。这样一来，left数组就记录了原数组中的严格递增
    //      关系，其实就相当于梯度信息。right也是一样，从右遍历，如果严格递增就+1，如果不是严格递增就回到1，这样一来，right
    //      就记录了原数组从右到左的严格递增关系。再对left和right中一一对应的元素取最大值就是结果。
    // eg:  arr  =  [5, 6, 9, 2, 2, 1, 0]
    //     left  =  [1, 2, 3, 1, 1, 1, 1]
    //    right  =  [1, 1, 2, 1, 3, 2, 1]
    //      res  =  [1, 2, 3, 1, 3, 2, 1]

    public static int candyV1(int[] ratings) {      // 时间复杂度：O(N)   空间复杂度：O(N)
        if (ratings == null || ratings.length == 0)
            return 0;
        if (ratings.length == 1)
            return 1;
        int N = ratings.length;
        int[] left = new int[N];
        left[0] = 1;
        int[] right = new int[N];
        right[N - 1] = 1;
        for (int i = 1; i < N; i++)
            left[i] = ratings[i] > ratings[i - 1] ? left[i - 1] + 1 : 1;
        for (int i = N - 2; i >= 0; i--)
            right[i] = ratings[i] > ratings[i + 1] ? right[i + 1] + 1 : 1;
        int res = 0;
        for (int i = 0; i < N; i++)
            res += Math.max(left[i], right[i]);
        return res;
    }
    // 这个方法还可以继续优化到空间复杂度为O(1)，只用有限几个变量，但对coding能力要求较高，可自行尝试。
    // =======================================================================================================


    // 问题二>>>>>>>>>>>>>>>>>>>>>>>>
    // 在上面的条件下，再额外加一个条件：相邻孩子的分数如果一样，那么必须分得相等数量的糖果。

    // 思路：和上面的思路一样，在填写left、right辅助数组时，如果arr[i]==arr[i-1]，那就让left[i]==left[i-1]，就这一点区别。
    //      相等时保持不变，递增就+1，递减就归1.
    public static int candyV2(int[] ratings) {      // 时间复杂度：O(N)   空间复杂度：O(N)
        if (ratings == null || ratings.length == 0)
            return 0;
        if (ratings.length == 1)
            return 1;
        int N = ratings.length;
        int[] left = new int[N];
        left[0] = 1;
        int[] right = new int[N];
        right[N - 1] = 1;
        for (int i = 1; i < N; i++)
            left[i] = ratings[i] > ratings[i - 1] ? left[i - 1] + 1 :
                    (ratings[i] < ratings[i - 1] ? 1 : left[i - 1]);
        for (int i = N - 2; i >= 0; i--)
            right[i] = ratings[i] > ratings[i + 1] ? right[i + 1] + 1 :
                    (ratings[i] < ratings[i + 1] ? 1 : right[i + 1]);
        int res = 0;
        for (int i = 0; i < N; i++)
            res += Math.max(left[i], right[i]);
        return res;
    }
}
