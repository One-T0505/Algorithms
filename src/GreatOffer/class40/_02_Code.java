package GreatOffer.class40;

import java.util.Arrays;

// 来自腾讯
// 比如arr = {3,1,2,4} 下标对应是:0123
// 你最开始选择一个下标进行操作，一旦最开始确定了是哪个下标，以后都只能在这个下标上进行操作
// 比如你选定1下标，1下标上面的数字是1,你可以选择变化这个数字，比如你让这个数字变成2
// 那么arr = {3,2,2,4} 下标对应是:0123
// 因为你最开始确定了1这个下标，所以你以后都只能对这个下标进行操作，但是，和你此时下标上的数字一样的、且位置
// 连成一片的数字，会跟着一起变
// 比如你选择让此时下标1的数字2变成3, 那么arr = {3,3,3,4} 可以看到下标1和下标2的数字一起变成3，这是规则!
// 一定会一起变   下标对应是:0123
// 接下来，你还是只能对1下标进行操作，那么数字一样的、且位置连成一片的数字( arr[0~2]这个范围)都会一起变
// 决定变成4  那么arr = {4,4,4,4}  下标对应是:0123
// 至此，所有数都成一样的了， 你在下标1上做了3个决定(第一次变成2, 第二次变成3,第三次变成4),
// 因为联动规则，arr全刷成一种数字了
// 给定一个数组arr,最开始选择哪个下标，你随意 你的目标是一定要让arr都成为一种数字，注意联动效果会一直生效
// 返回最小的变化数
// arr长度 <= 200， arr中的值 <= 10^6

public class _02_Code {

    // arr[0...left] 还没被拉进同一阵营   arr[right...n-1] 也还没被拉近阵营
    // left+1...right-1 是已经全部都是同一个数了，same表示这些被拉入同一阵营的数是什么
    // 返回: arr整体都刷成一祥至少変几次
    private static int f(int[] arr, int l, int r, int same) {
        int N = arr.length;
        // 这两个循环是想看旁边有没有元素能拉进来一起，是在决定变化之前做的
        // 这两个循环有两种理解方式：
        //  1.最开始调用该方法时，是选择一个下标开始运行，那么选择完了一个下标后，先要看看左右两边有没有本来就是
        //    一样的可以一起
        //  2.在递归的过程中，执行这两个循环的时机是上一次同一阵营的元素刚刚变化成了一个新的值，此时需要重新查看
        //    有没有新的元素可以将阵营扩充
        while (l >= 0 && arr[l] == same)
            l--;
        while (r < N && arr[r] == same)
            r++;
        // 开始决策将同一阵营的元素变成什么值 该阵营的元素必然是从其左侧或者右侧选一个值变，比如：
        // [2, 3, 3, 3, 3, 5] 肯定选择变成2或者5，除了这两个数以外的变化都是浪费机会，因为不会扩充阵营
        if (l == -1 && r == N) // 说明已经将全部元素变成同一种了
            return 0; // 变化次数为0
        int p1 = Integer.MAX_VALUE;
        if (l >= 0) // 说明阵营左侧还有没被拉进来的元素，那么才有可能让阵营变成左侧的值
            p1 = f(arr, l, r, arr[l]);
        int p2 = Integer.MAX_VALUE;
        if (r < N)
            p2 = f(arr, l, r, arr[r]);
        return Math.min(p1, p2) + 1;
    }


    // 主方法
    public static int minChange(int[] arr) {
        if (arr == null || arr.length < 2)
            return 0;
        // 长度为N的数组，最差情况仅需要N-1次变化即可完成，每次拉1个元素进入阵营
        int res = arr.length - 1;
        for (int i = 0; i < arr.length; i++) { // 枚举每个位置作为决定的下标
            res = Math.min(res, f(arr, i - 1, i + 1, arr[i]));
        }
        return res;
    }
    // 分析下时间复杂度：递归函数f有3个可变参数，l最差情况就是N，r也一样，same最大就是数组的最大值max
    // 所以时间复杂度为O(N^2*max) N<=200  max<=10^6  这种方法过不了
    // 仔细想想看可以发现，数组中的每个值其实跟它本身具体是多少无关，关键是相对大小。比如：[298, 67, 10034]
    // 可以将其变成：[1, 0, 2]  因为67是数组最小的，298是第二小的。。于是我们把数组变成了更经济的模式，
    // 并且不会影响结果，但是这种改变可以让第三个可变参数的范围大大缩小，改变后max最大也是N
    // 所以时间复杂度优化为了O(N^3) <= 8 * 10^6 可以过了


    // 按照上面优化后的方法改成动态规划
    public static int minChange2(int[] arr) {
        if (arr == null || arr.length < 2)
            return 0;
        int N = arr.length;
        // 将数组修改成经济模式
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int e : arr) {
            min = Math.min(min, e);
            max = Math.max(max, e);
        }
        for (int i = 0; i < N; i++)
            arr[i] -= min;
        // 第一个N+1是表示l的范围，虽然实际上N+1表示的下标是0~N，但是在递归中l的可到的范围是-1~N-2，所以
        // 让dp[i]表示l在i-1，并且弃用dp[N]
        // r的实际可行范围是1～N 刚好就让dp[][j]表示r在j,并弃用dp[][0]
        int[][][] dp = new int[N + 1][N + 1][max - min + 1];
        for (int[][] m : dp) {
            for (int[] a : m)
                Arrays.fill(a, Integer.MAX_VALUE);
        }
        return 1;
    }


    public static void main(String[] args) {
        int[] arr = {3, 1, 2, 4};
        System.out.println(minChange(arr));
    }
}
