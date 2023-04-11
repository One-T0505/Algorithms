package Basic.SlidingWindow;

// arr是货币数组，其中的值都是正数，并且该数组无序且可重复。再给定一个正数aim。
// arr的每个元素都认为是一张货币，返回组成aim的最少货币数
// 注意:因为是求最少货币数，所以每一张货币认为是相同或者不同就不重要了

public class exercise04 {
    // 最传统的做法可以是从左到右尝试类型的暴力递归到动态规划，每一张货币选择要或不要
    public static int dp1(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim < 0)
            return -1; // 返回-1表示无结果
        if (aim == 0)
            return 0;
        return process(arr, 0, aim);
    }


    // 该方法返回：从index处开始做决策，拼凑出rest面额最少的货币数量
    private static int process(int[] arr, int index, int rest) {
        if (rest < 0)
            return -1;
        if (index == arr.length)
            return rest == 0 ? 0 : -1;
        // 不要
        int p1 = process(arr, index + 1, rest);
        // 要
        int p2 = process(arr, index + 1, rest - arr[index]);
        // 为什么v1不用判断？ 因为在当前方法中，如果能走到这里，就说明rest不小于0，所以肯定不会为-1
        if (p2 != -1)
            p2++;
        return Math.min(p1, p2);
    }
}
