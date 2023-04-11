package GreatOffer.class18;

// 给定一个数组arr,长度为N，arr中 的值只有1, 2，3三种
// arr[j]== 1,代表汉诺塔问题中，从上往下第i个圆盘目前在左
// arr[j]== 2,代表汉诺塔问题中，从上往下第i个圆盘目前在中
// arr[j] ==3,代表汉诺塔问题中，从上往下第i个圆盘目前在右
// 那么arr整体就代表汉诺塔游戏过程中的一个状况
// 如果这个状况不是汉诺塔最优解运动过程中的状况，返回-1
// 如果这个状况是汉诺塔最优解运动过程中的状况，返回它是第几个状况

public class Hanoi {

    // 首先要知道一个结论：N层汉诺塔问题需要 2^N - 1 步完成。之前写汉诺塔问题的时候将整个过程抽象成了三大步：
    //  1> 将1～N-1从from移动到other   需要 2^(N-1) - 1 步
    //  2> 将N从from移动到to           需要 1 步
    //  2> 将1～N-1从other移动到to     需要 2^(N-1) - 1 步

    public static int kThStep(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        // 只有一个塔，那么其状态只有两种，要么是1在左边，要么是3在右边
        if (arr.length == 1)
            return arr[0] == 1 ? 1 : (arr[0] == 3 ? 2 : -1);
        return step(arr, arr.length - 1, 1, 3, 2);

    }


    // 有0～i这些圆盘，即说明这是个i+1层汉诺塔问题；需要把0～i这些圆盘从from移动到to
    // 该方法返回arr[0..i]这些状态是i+1层汉诺塔问题最优解的第几步
    private static int step(int[] arr, int i, int from, int to, int other) {
        if (i == -1)   // base case
            return 0;
        if (arr[i] == other) // i+1层汉诺塔问题，最底下的圆盘i从始至终都不可能在other上
            return -1;
        // 执行到这里，arr[i] == from || arr[i] == to
        if (arr[i] == from)  // 说明第一大步还没执行完
            return step(arr, i - 1, from, other, to);
        // 执行到这里，arr[i] == to
        int step1 = (1 << i) - 1;  // 第一大步已经执行完了，就是将0～i-1这个i层汉诺塔问题，移动完毕需要的步数
        int step2 = 1;             // 第二大步只用一步
        int step3 = step(arr, i - 1, other, to, from);
        if (step3 == -1)    // 在执行中如果得到-1，那么直接无效
            return -1;
        return step1 + step2 + step3;
    }


    public static void main(String[] args) {
        int[] arr = {3, 3};
        System.out.println(kThStep(arr));
    }

}
