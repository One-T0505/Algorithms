package Basic.SlidingWindow;

import java.util.LinkedList;

// leetCode134
// 只有返回值略有不同，其他思路完全一样，所以如果用下面滑动窗口的算法去完成134题的话，需要修改返回值

// 加油站问题：给一个数组gas，分别表示每个加油站有多少单位的汽油；还有一个数组dis，分别表示从i号加油站到i+1号加油站的距离，
// dis中最后一个元素表示最后一个加油站到0号加油站的距离，所以是个环形距离。问从哪个加油站出发，能绕一圈回到出发点，找出所有
// 的出发点。
// eg：gas[1, 1, 3, 1]   dis[2, 2, 1, 1]
//             0-->1
//          2/   \1
//          /     \
//      1<-1       3-->1
//          \     /
//          2\   /1
//             2-->3
// 如图所示 0号加油站有1个油，并且距离1号加油站距离2； 1号加油站有1个油，并且距离2号加油站距离2
//         2号加油站有3个油，并且距离3号加油站距离1;  3号加油站有1个油，并且距离0号加油站距离1
// 只能按顺序去到下一个加油站，不能随便跑，在哪个加油站就可以把油站的油加完，油箱无限制
// 该图中，不能从0号出发，因为在0号加了1个油，在去往1号加油站的路上就熄火了。只能以2号加油站为起点能绕一圈回到2号。

// 思路：gas - dis = [-1, -1, 2, 0] 用这个新数组res更方便解答，假设从1号加油站出发，那么res[1]不断累加res[2],
//      res[3],res[0]  但凡累加和小于0了，那就不符合。所以暴力解法可以这样做
public class GasStation {

    // 暴力解法做对数器 时间复杂度：O(N2)
    public static int verify(int[] gas, int[] dis){
        if (gas == null || dis == null || gas.length != dis.length)
            return 0;
        int num = 0;
        int[] res = new int[gas.length];
        for (int i = 0; i < res.length; i++)
            res[i] = gas[i] - dis[i];
        for (int i = 0; i < res.length; i++) { // 每一个加油站尝试做起点
            int tmp = res[i];
            int j = i + 1 == res.length ? 0 : i + 1;
            while (tmp >= 0 && j != i){
                tmp += res[j];
                j = (j + 1) == res.length ? 0 : j + 1;
            }
            if (j == i)
                num++;
        }
        return num;
    }



    // 利用滑动窗口思想 时间复杂度：O(N) 额外空间复杂度：O(N). 这里的脑洞比较大
    // 还用之前的例子，gas=[1, 1, 3, 1]  dis=[2, 2, 1, 1]  得到res=[-1, -1, 2, 0] 我们要算的是所有加油站为起点，
    // 其累加和过程中是否小于0的出现。用暴力法就是要遍历数组，利用滑动窗口的话就需要对res进一步加工。下面一步一步来对res
    // 加工，首先：
    // 以0号加油站为起点，累加和为：sum0 = [-1, -2, 0, 0]
    // 以1号加油站为起点，累加和为：sum1 = [-1, 1, 1, 0]
    // 以2号加油站为起点，累加和为：sum2 = [2, 2, 1, 0]
    // 以3号加油站为起点，累加和为：sum3 = [0, -1, -2, 0]  是否能不通过遍历就能得到所有加油站作为起点的累加和数组呢？
    // all = [-1, -2, 0, 0, -1, -2, 0, 0]
    // 这个数组是怎么来的呢？是以0号加油站为起点算了两个res的累加和  all[4] = all[3] + res[0]
    // 可以发现：用一个固定大小为4的窗口依次滑动就能得到sum0, sum1, sum2, sum3.
    // all[0~3] - all[-1] 就是sum0，此时-1越界就当作是减0
    // all[1~4] - all[0] 就是sum1
    // all[2~5] - all[1] 就是sum2
    // all[3~6] - all[2] 就是sum3
    // all[4~7] - all[3] 就是sum0        每次窗口中的值都依次减去窗口前的一个值就能得到对应的累加和

    // all[1] = res[0] + res[1]
    // all[2] = res[0] + res[1] + res[2]
    // all[3] = res[0] + res[1] + res[2] + res[3]
    // all[4] = res[0] + res[1] + res[2] + res[3] + res[0]
    // all[5] = res[0] + res[1] + res[2] + res[3] + res[0] + res[1]
    // 所以，all[2~5] - all[1] 就得到了以2号加油站为起点的累加和
    // 当有个固定大小的窗口依次滑过all时，每次找出窗口中最小值，再减去窗口前一个值，就还原出了原累加和中最弱的点，若<0则该路径必不可能

    public static int gasStation(int[] gas, int[] dis){
        if (gas == null || dis == null || gas.length == 0 || dis.length == 0 ||gas.length != dis.length)
            return 0;
        int path = 0;
        // 准备res和all，这里的方法一开始不是这样写的，后来觉得现在这样写的方法更精简
        int[] all = new int[gas.length << 1];
        for (int i = 0; i < gas.length; i++) {
            all[i] = gas[i] - dis[i];
            all[i + gas.length] = gas[i] - dis[i];
        }
        // 此时all里面就是填好了两个res的结果
        for (int i = 1; i < all.length; i++)
            all[i] += all[i - 1];

        // 准备窗口
        int w = gas.length;
        LinkedList<Integer> min = new LinkedList<>();
        for (int R = 0; R < all.length - 1; R++) { // all中最后一个窗口和第一个窗口是一样的，所以这里选择不用最后一个窗口
            while (!min.isEmpty() && all[min.peekLast()] >= all[R])
                min.pollLast();
            min.addLast(R);
            if (R >= w - 1) { // 窗口初步形成
                if (all[min.peekFirst()] - (R - w >= 0 ? all[R - w] : 0) >= 0) // 判断最小值减窗口前驱是否不小于0
                    path++;
                if (R - w + 1 == min.peekFirst()) // 判断要过期的元素是否是队头元素
                    min.pollFirst();
            }
        }
        return path;
    }
    // --------------------------------------------------------------------------------------------





    // 现在讲最优解。在不修改distance、cost数组的情况下，时间复杂度O(N)，
    // 额外空间复杂度O(1)
    // 先构建一个纯能数组en，en[i]==gas[i]-cost[i]，如果en[i]<0，就说明i站的油不足以到达下一个站点；
    // 一开始要先找到en中一个大于等于0的站点作为起始点，随便一个都可以。定义两个变量rest、need。我们的总体流程是
    // 以该起点为开头，不断向后扩，看最多能到达哪个站点，我们把这些可以到的站点称为一个联通区，rest的含义就是从联通区的
    // 起点到终点，还能剩下多少油；当以某个点为起点将联通区扩到最大之后，就需要从起点的反方向，看能不能将新的站点作为
    // 新起点。能否纳入作为新起点就要依赖need这个变量。

    // 求索引i的上一个索引
    private static int lastIndex(int i, int N) {
        return i == 0 ? N - 1 : i - 1;
    }

    // 求索引i的下一个索引
    private static int nextIndex(int i, int N) {
        return i == N - 1 ? 0 : i + 1;
    }

    // 已知start的next方向上有一个良好出发点
    // start如果可以达到这个良好出发点，那么从start出发一定可以转一圈
    public static void connect(int[] en, int start, int init, boolean[] res) {
        int need = 0;
        while (start != init) {
            if (en[start] < need)
                need -= en[start];
            else {
                res[start] = true;
                need = 0;
            }
            start = lastIndex(start, en.length);
        }
    }

    public int canCompleteCircuit(int[] gas, int[] cost) {
        if (gas == null || gas.length == 0 || gas.length != cost.length)
            return -1;
        if (gas.length == 1)
            return gas[0] < cost[0] ? -1 : 0;
        // 该方法返回一个布尔型数组，我们统计了所有的站点是否可以作为起始点，但是leetCode只要求返回一个
        // 可以作为起点的站点，所以才有了下面的代码
        boolean[] goods = stations(gas, cost);
        for (int i = 0; i < gas.length; i++) {
            if (goods[i])
                return i;
        }
        return -1;
    }

    private boolean[] stations(int[] gas, int[] cost) {
        if (cost == null || gas == null || cost.length < 2 || cost.length != gas.length)
            return null;
        int init = getInit(gas, cost);
        // 如果init==-1。说明所有站点都不能作为起点，那么直接生成一个布尔型数组返回即可，因为默认值都为false
        return init == -1 ? new boolean[gas.length] : enlargeArea(cost, init);
    }

    // 返回一个可以作为起点的站点，如果都不能作为起点，那返回-1
    private int getInit(int[] gas, int[] cost) {
        int init = -1;
        for (int i = 0; i < cost.length; i++) {
            // 这就是那个纯能数组，我们不重新开辟空间，因为要保证额外空间复杂度O(1)，但是最后我们需要还原该数组
            cost[i] = gas[i] - cost[i];
            if (cost[i] >= 0)
                init = i;
        }
        return init;
    }

    // 扩充联通区  主方法中，调用该方法前还调用了getInit方法，里面对cost数组做了修改，
    // 所以此时的cost已经是纯能数组
    private boolean[] enlargeArea(int[] cost, int init) {
        int N = cost.length;
        boolean[] res = new boolean[N];
        int start = init;
        int end = nextIndex(init, N);
        int need = 0;
        int rest = 0;
        do {
            // 当前来到的start已经在连通区域中，可以确定后续的开始点一定无法转完一圈
            if (start != init && start == lastIndex(end, N))
                break;
            // 当前来到的start不在连通区域中，就扩充连通区域
            if (cost[start] < need)  // 当前start无法作为连通区的新头部
                need -= cost[start];
            else { // 当前start可以作为连通区的新头部，开始扩充连通区域的尾巴
                rest += cost[start] - need;
                need = 0;
                while (rest >= 0 && end != start) {
                    rest += cost[end];
                    end = nextIndex(end, N);
                }
                // 如果连通区域已经覆盖整个环，当前的start是良好出发点，进入第二阶段
                if (rest >= 0) {
                    res[start] = true;
                    connect(cost, lastIndex(start, N), init, res);
                    break;
                }
            }
            start = lastIndex(start, N);
        } while (start != init);
        return res;
    }
    // --------------------------------------------------------------------------------------------


    public static void main(String[] args) {
        int[] gas = {1, 2, 3, 5, 4, 6, 5};
        int[] dis = {5, 2, 3, 4, 3, 5, 3};
        System.out.println(gasStation(gas, dis));
    }
}
