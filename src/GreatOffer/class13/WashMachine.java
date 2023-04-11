package class13;

// leetCode517
// 假设有 n 台超级洗衣机放在同一排上。开始的时候，每台洗衣机内可能有一定量的衣服，也可能是空的。
// 在每一步操作中，你可以选择任意 m (1 <= m <= n) 台洗衣机，与此同时将每台洗衣机的一件衣服送到相邻的一台洗衣机。
// 相邻是说当前选择的m台洗衣机必须向同一个方向转移衣服，下一轮转移衣服的方向随意，而不是说每一轮的m台机器有的向左有的
// 向右转移。给定一个整数数组 machines 代表从左至右每台洗衣机中的衣物数量，请给出能让所有洗衣机中剩下的衣物的数量相
// 等的最少的操作步数 。如果不能使每台洗衣机中衣物的数量相等，则返回 -1 。

public class WashMachine {

    // 这个题目属于见过就会，没见过就不会的类型。所以解法记住就行。首先一开始，machines的累加和就是衣服总数量，
    // 并且始终不变，因为洗衣机只是在转移衣服，并不会使总量减少，所以衣服总量sum是可以求出来的，我们的最终目标
    // 是使每台洗衣机中衣服的数量相等，所以这个值我们也可以直接求出来为t。对于任意一个位置的洗衣机i，其左边所有的
    // 洗衣机现有总数量可以求出来，要达到的数量也可以求出来，还差多少也就知道了。如果结果为负数，说明左边整体需要
    // 有衣服转入，如果为正数，说明左边整体可以像右侧转移衣服。对于一个i，可能性划分为了三种：
    //  1.一侧<0，假如为-a，一侧>0，假如为b，那么就需要 Math.max(a, b) 轮
    //  2.两侧都>0，假设为a，b，那么就需要 Math.max(a, b)
    //  3.两侧都<0，假设为-a，-b，说明只能由i位置向两侧转移衣服, 就需要 a + b 轮
    // 对于每个位置都求出自己的结果，然后全局取最大值即可。

    public static int superMachine(int[] machines) {
        if (machines == null || machines.length == 0)
            return -1;
        int sum = 0;
        for (int elem : machines)
            sum += elem;
        int N = machines.length;
        if (sum % N != 0)
            return -1;
        int avg = sum / N;
        int res = 0;
        int leftSum = 0;
        for (int i = 0; i < N; i++) {
            int left = leftSum - i * avg;
            int right = (sum - leftSum - machines[i]) - (N - i - 1) * avg;
            if (left < 0 && right < 0)
                res = Math.max(res, Math.abs(left) + Math.abs(right));
            else
                res = Math.max(res, Math.max(Math.abs(left), Math.abs(right)));
            leftSum += machines[i];
        }
        return res;
    }
}
