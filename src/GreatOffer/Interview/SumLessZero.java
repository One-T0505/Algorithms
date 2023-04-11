package GreatOffer.Interview;

// 来自微软面试
// 给定一个正数数组arr,长度为n,正数x和y 你的目标是让arr整体的累加和<=0
// 你可以对数组中的数num执行以下两种操作中的一种，且每个数最多能执行一次操作:
//  1) 可以选择让num变成0，承担x的代价
//  2) 可以选择让num变成-num,承担y的代价
//  3) 不做任何操作，那就不用承担代价
// 返回你达到目标的最小代价
// 数据规模:面试时面试官没有说数据规模

import java.util.Arrays;

public class SumLessZero {

    // 暴力尝试
    public static int minCost(int[] arr, int x, int y) {
        int sum = 0;
        for (int e : arr)
            sum += e;
        return f(arr, x, y, 0, sum);
    }


    // 先算出数组整体累加和sum，定义一个递归函数f(i, rest)  i表示现在来到了arr[i]做决定，
    // rest表示i～N-1这些元素的累加和是多少
    // 返回值表示将i～N-1的元素凑出累加和<=0时的最小代价
    private static int f(int[] arr, int x, int y, int i, int rest) {
        if (rest <= 0) // 如果已经<=0了，那就不用在做任何操作了
            return 0;
        if (i == arr.length) // 如果rest > 0，并且已经全部决策完了，那就说明不可能搞定
            return Integer.MAX_VALUE;
        // rest > 0 并且 还有元素
        // 情况1：arr[i]不做任何操作，直接跳过
        int p1 = f(arr, x, y, i + 1, rest);
        // 情况2：arr[i]变成0
        int p2 = Integer.MAX_VALUE;
        int next2 = f(arr, x, y, i + 1, rest - arr[i]);
        // 如果将当前元素arr[i]变成0后，后续仍有可行方法，那么就修改p2为真实的代价，否则p2就表示不可行
        if (next2 != Integer.MAX_VALUE)
            p2 = next2 + x;
        // 情况2：arr[i]变成-arr[i]
        int p3 = Integer.MAX_VALUE;
        int next3 = f(arr, x, y, i + 1, rest - (arr[i] << 1));
        if (next3 != Integer.MAX_VALUE)
            p3 = next3 + y;

        return Math.min(p1, Math.min(p2, p3));
    }
    // 这就是背包模型，从左到右尝试，每个元素有不同的决策。有了这个暴力尝试就可以改记忆化搜索和动态规划
    // ====================================================================================================


    // 最优解：贪心
    // 上面那个递归有两个可变参数i和rest，如果给的数组长度过大或者元素数值过大，这个方法就不行了。
    // 首先先把数组按从大到小排序。假设本来存在一个最优代价，那么和元素顺序没有关系，所以排序没影响。
    // 1.如果 x >= y  那么没有必要对一个元素做x操作，因为其代价更大，并且x操作只是让arr[i]变成0，而y操作
    //   可以让arr[i]变成相反数，所以y操作能更快地让数组累加和<=0，并且需要的代价更少。所以，我们只需要
    //   从开头依次让若干个元素变成相反数，直到数组累加和<=0停止即可。

    public static int minCost2(int[] arr, int x, int y) {
        Arrays.sort(arr);  // 现在是从小到大的
        int N = arr.length;
        for (int l = 0, r = N - 1; l <= r; l++, r--) {
            int tmp = arr[r];
            arr[r] = arr[l];
            arr[l] = tmp;
        }
        // 统计数组累加和
        int sum = 0;
        for (int e : arr)
            sum += e;
        // 上面的操作完成了数组的逆序排序
        if (x >= y) {
            int cost = 0;
            for (int i = 0; i < N && sum > 0; i++) {
                sum -= arr[i] << 1;
                cost += y;
            }
            return cost;
        } else { // x < y
            // x < y时，有一个结论得知道：
            // 如果某个数不做y操作了，那么其后面的所有数都不可能再做y操作了，因为承担的代价一样，但是后面的数必然是<=当前数
            // 了，所以后面的数再做y操作对整体的结果效果没有当前数做y操作的效果大。所以，要做y操作的元素必然是排序完的数组
            // 最前面的一批连续的数，因为其数值最大，能让数组最快地满足要求。
            // 假设0～4这些元素是做y操作的，那么5～N-1这些元素都不可能再做y操作了，他们只能做x操作或不做。
            // 同理，做x操作的一批数是5~N-1中最前面且连续的一批数，原因和上面的一样。
            // 所以这个结论就是：在排序完的数组是分成三段的：第一段都是做y操作的，第二段都是做x操作的第三段都是不做操作的。
            // 当然，第一段、第二段都有可能不存在，总之，如果存在，那么必然是连续的。
            // 比如： 9 2 2 1 1   9做了y操作后，数组累加和就成了-1已经达标了，后面的数都不用做任何操作了，所以做x操作的
            //    区间就不存在。
            // 再比如：  3 2 1   x==2  y==50   那么肯定是让这三个数都做x操作了，因为总代价小，才为6，虽然让第一个数做y
            //    操作就能达标，但是代价大为50。

            // 再了解了这个结论后，我们只需要用两个for循环枚举两个分界点即可了。下面枚举的是每个区间的长度
            int cost = Integer.MAX_VALUE;
            int[] pre = new int[N];  // 前缀和数组
            pre[0] = arr[0];
            for (int i = 1; i < N; i++)
                pre[i] = pre[i - 1] + arr[i];

            for (int yLen = 0; yLen < N; yLen++) {  // 不可能让所有数都做y操作的。最后一个数做x操作必然优于所有做y
                for (int xLen = 0; xLen <= N - yLen; xLen++) {
                    // 当确定了各个区域后，需要实际计算下当前数组的累加和看是否达标  减去的两个部分分别是y区间和x区间的收益
                    int yP = yLen == 0 ? 0 : pre[yLen - 1] << 1;
                    int xP = xLen == 0 ? 0 : pre[yLen + xLen - 1];
                    int cur = sum - yP - xP;
                    if (cur <= 0) // 如果达标了就记录答案
                        cost = Math.min(cost, yLen * y + xLen * x);
                }
            }
            return cost;
        }
    }
    // ===================================================================================================


    // 贪心优化
    // 这个方法和上面的初版贪心思路一模一样，优化的点在于枚举两个分界点时不用一个一个尝试
    // 比如数组：9 7 7 6 5 3 3 2 1
    // 假设我们确定了y区间的分界点之后，比如确定了y区间长度为1，就是只有9做y操作，那么能带来9的收益，因为虽然
    // 9变成-9变化了18，但是从9～0只够解决它自己的问题，剩下的9才是能提供给别人的。他能解决 3 3 2 1
    // 我们从最后往前找，看y区间的收益能让哪些数不做任何操作也能使累加和<=0，可以找到5位置的3，如果再继续向前加一个5，那么
    // y区间的收益9就不能覆盖住了，因为5+3+3+2+1>=9。
    // 只有y区间的数可以帮助那些不做任何操作的数，这两个区间是互补的，而x区间只能顾自己。所以y区间的收益要尽量覆盖最多的数
    // 让他们不做操作，这样代价就能就可能低。
    // 我们需要一个后缀数组post，post[i]表示i～N-1的累加和。
    // 所以其实就只用枚举y区间的分界点，然后在后面的区间里二分找出最左的位置l，使得post[l] <= y区间的收益
    // 这样枚举的代价从O(N^2) -> O(N*logN)
    public static int minCost3(int[] arr, int x, int y) {
        Arrays.sort(arr);  // 现在是从小到大的
        int N = arr.length;
        for (int l = 0, r = N - 1; l <= r; l++, r--) {
            int tmp = arr[r];
            arr[r] = arr[l];
            arr[l] = tmp;
        }
        // 上面的操作完成了数组的逆序排序
        // 统计数组累加和
        int sum = 0;
        for (int e : arr)
            sum += e;

        if (x >= y) {
            int cost = 0;
            for (int i = 0; i < N && sum > 0; i++) {
                sum -= arr[i] << 1;
                cost += y;
            }
            return cost;
        } else { // x < y
            // 直接将arr改成后缀和数组
            for (int i = N - 2; i >= 0; i--)
                arr[i] += arr[i + 1];
            // 枚举y区间分界点
            int profit = 0;   // y区间的收益
            // 最开始y区间为空，所以0～p-1都是做x操作的
            int p = mostLeft(arr, 0, profit);
            int cost = p * x;
            for (int yLen = 1; yLen < N; yLen++) {
                profit += arr[yLen - 1] - arr[yLen];
                p = mostLeft(arr, yLen, profit);
                // 所以数组分成了：0..yLen-1  yLen..p-1  p..N-1
                cost = Math.min(cost, yLen * y + (p - yLen) * x);
            }
            return cost;
        }
    }


    // 在post[L..N-1]上找出最左边且<=t的位置
    private static int mostLeft(int[] post, int L, int t) {
        int R = post.length - 1;
        int pos = R + 1;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (post[mid] <= t) {
                pos = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return pos;
    }
    // 这样的贪心摆脱了数值大小对时间复杂度的影响
}
