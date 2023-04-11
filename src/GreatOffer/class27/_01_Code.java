package GreatOffer.class27;

import java.util.Arrays;

// 每一个项目都有三个数，[a,b.c]表示一个项目，其中a和b表示参演的乐队编号，请这两支乐队花费为c。
// 给定很多个项目int[][] programs，每一个乐队可能在多个项目里都出现了，但是一支乐队只能参加一个项目。
// nums是可以挑选的项目数量，乐队编号是从0～nums*2-1， 并且一定会有nums*2只乐队被挑选出来。
// 返回一共挑nums轮(也就意味着一定请到所有的乐队)，最少花费是多少?

// nums < 9, programs长度小于500，每组测试乐队的全部数量一定是nums*2，且标号一定是0 ~ nums*2-1

public class _01_Code {

    // 首先要对给的int[][] programs 数据进行重组清洗。因为nums<9，所以乐队数量不超过16支，两两组合C(16,2)
    // == 120种，但是programs的长度<=500，所以很大概率下，programs中给的两支乐队是重合的，对于重合的组合，我们只留下
    // 那个花费最小的。其次，我们规定让每个项目的两支乐队编号升序，也就是说让编号小的在前，不可能是两个一样的乐队
    // 在同一个项目里，我们想达到的整体排序效果是：
    // 项目按照第一支乐队的编号递增排序，第一支乐队编号相同的组里，第二支乐队的编号按照递增排序，第二支乐队也相同的组里，
    // 按照花费递增排序。

    public static int minCost = Integer.MAX_VALUE;
    // ===============================================================================================

    private static int clean(int[][] programs) {
        // 第一步：先把每个项目的两支乐队排好序
        int x = 0, y = 0;
        for (int[] program : programs) {
            x = Math.min(program[0], program[1]);
            y = Math.max(program[0], program[1]);
            program[0] = x;
            program[1] = y;
        }
        // 整体排序
        // 项目按照第一支乐队的编号递增排序，第一支乐队编号相同的组里，第二支乐队的编号按照递增排序，第二支乐队也相同的
        // 组里，按照花费递增排序。
        Arrays.sort(programs, ((a, b) -> a[0] != b[0] ? a[0] - b[0] :
                (a[1] != b[1] ? a[1] - b[1] : a[2] - b[2])));
        // 第三步：每一种组合只留下花费最低的那组，其余删除
        // 并且，经过上一步的排序后，第0号元素一定是会保留下来的
        x = programs[0][0];
        y = programs[0][1];
        for (int i = 1; i < programs.length; i++) {
            // 组合重复了，就让其指为空
            if (programs[i][0] == x && programs[i][1] == y)
                programs[i] = null;
            else { // 每次出现第一个不同于上一组的组合的时候，这个新组合一定是会被保留下来的
                x = programs[i][0];
                y = programs[i][1];
            }
        }
        // 经过上一步，让无效项目指空后，我们需要统计真实有效的项目数；并且让后面的有效项目填补之前空的位置
        int size = 1;  // programs[0]肯定保留了
        for (int i = 1; i < programs.length; i++) {
            if (programs[i] != null)
                programs[size++] = programs[i];
        }
        return size;
    }


    // 上一步清洗数据完成后，才开始最关键的递归。最多有16支乐队，所以我们可以用int型数据的低16位表示这16支乐队，
    // 对应位上为0，表示该编号的乐队没被选，如果为1，则说明该乐队被选过了；如果可以选8轮，那就是16支乐队，所以
    // 16支乐队都被选择完的状态应该是：0....0 1111 1111 1111 1111  最后面是16个1，该值等于：
    // (1 << 16) - 1 --> (1 << (nums * 2)) - 1 --> (1 << (nums << 1)) - 1

    // programs是清洗过后的数据，size就是清洗时返回的有效项目数, 0..size-1是有效的位置
    // done 是固定参数表示最终完成的状态，如果可以选4轮，done == 0..0 1111 1111
    // index表示当前来到的项目编号，要决策programs[index]这个项目
    // rest表示还剩几轮可以选
    // pick表示之前0..index-1个项目决策完后，选择到的乐队组成的状态，如果只选中了1号和3号乐队，那pick== 0..0 1010
    // cost表示之前的决策总共的花费
    private static void f(int[][] programs, int size, int done, int index, int rest,
                          int pick, int cost) {
        if (rest == 0) {  // 不能再选了
            if (pick == done)
                minCost = Math.min(minCost, cost);
        } else { // 还有轮次可以选
            if (index != size) { // 还剩项目可以选
                // 两种决策，考虑当前项目和不考虑当前项目
                // 不考虑当前项目
                f(programs, size, done, index + 1, rest, pick, cost);
                // 考虑当前项目是有条件的
                int[] p = programs[index];
                int x = p[0];
                int y = p[1];
                // 选择当前项目的两支乐队，那么状态位应该是怎么样的
                int choice = (1 << x) | (1 << y);
                if ((pick & choice) == 0) { // 说明之前的选择里，这两支乐队乐队都没被选过
                    f(programs, size, done, index + 1, rest - 1,
                            pick | choice, cost + p[2]);
                }
            }
        }
    }
    // 该方法是可以优化的，在考虑当前项目的情况里，调用递归的时机是当前项目的两个乐队没有被选过的时候才调用，
    // 全程只有在这里rest会减少，也就是说rest每减少1，对应的pick状态位就有两个0变成了两个1，所以在最上面
    // 判断rest==0的里面，就不用再判断pick == done，因为rest==0的时候，pick必然等于done


    // 第一次优化后的方法，省了一个done参数
    private static void g(int[][] programs, int size, int index, int rest, int pick, int cost) {
        if (rest == 0) {  // 不能再选了
            minCost = Math.min(minCost, cost);
        } else { // 还有轮次可以选
            if (index != size) { // 还剩项目可以选
                // 两种决策，考虑当前项目和不考虑当前项目
                // 不考虑当前项目
                g(programs, size, index + 1, rest, pick, cost);
                // 考虑当前项目是有条件的
                int[] p = programs[index];
                int x = p[0];
                int y = p[1];
                // 选择当前项目的两支乐队，那么状态位应该是怎么样的
                int choice = (1 << x) | (1 << y);
                if ((pick & choice) == 0) { // 说明之前的选择里，这两支乐队乐队都没被选过
                    g(programs, size, index + 1, rest - 1,
                            pick | choice, cost + p[2]);
                }
            }
        }
    }
    // programs清洗过后最多还剩120条，rest最多是8，因为在递归中rest==0就会终止递归，所以时间复杂度:
    // C(120, 8) > 10^8  所以该方法是通不过的
    // ===========================================================================================


    // 这道题目的最终方法，是在上面的方法的基础上进行分治的。大体思路：
    // 假如nums==8，就是说要选到16支乐队，我们可以用上面的g方法但是选择的轮数只传入一半也就是4轮，
    // 跑完该方法，16支球队里任意选择8支乐队的情况，只要programs里提供了，那么该方法就全部能找得到；
    // 可以认为programs里，所有任意8支乐队的组合都能找得到；也就是说状态位里低16中任意8位1的情况都找得到，
    // 那么我们只需要在每一种状态下取反，即可找到剩下需要找到的状态位。比如，nums==8，也就是选择16支乐队，
    // 那么状态位里最后16位是有效的，我们先选择4轮，将所有8支乐队的组合都找出来，有一种组合是:
    // 0010 1101 0001 1011，那我们只需要取反：1101 0010 1110 0100 就得到剩下的一种组合，这个组合肯定是
    // 已经计算过的，因为刚才就说了，所有8支乐队的组合都找到了，于是我们只需要对应找到取反的状态即可了。
    // 如果nums是奇数，比如nums==5，那就先调用g选2轮，再调用g选3轮即可。
    // 所以我们需要空间来记录每种状态下对应的花费，为了能达到快速查询另一半的效果。

    public static int pickBands(int[][] programs, int nums) {
        if (nums < 1 || programs == null || programs.length == 0)
            return 0;
        int size = clean(programs);
        // 记录每种状态下的最低花费，初始全部为系统最大值
        int[] dp1 = new int[1 << (nums << 1)];
        Arrays.fill(dp1, Integer.MAX_VALUE);
        int[] dp2 = null;  // 如果nums为奇数会用到dp2
        if ((nums & 1) == 0) { // 偶数
            h(programs, size, 0, 0, 0, nums >> 1, dp1);
            dp2 = dp1;
        } else {  // 奇数
            h(programs, size, 0, 0, 0, nums >> 1, dp1);
            dp2 = new int[1 << (nums << 1)];
            Arrays.fill(dp2, Integer.MAX_VALUE);
            // 奇数还要再跑一遍，因为两次选的乐队数量不一样，如果是偶数，那么跑一遍递归，所有情况都存在了
            // 如果是奇数，比如7，那么状态位最低14位是有效的，第一次选择3轮，那么所有结果都是只有6个1，剩下的8个1，
            // 没办法在这张表里找到。偶数的话，比如8，选择16支乐队，那么跑一次选择8支乐队的递归，任意一种选择的剩
            // 下的选择也在表里
            h(programs, size, 0, 0, 0, nums - (nums >> 1), dp2);
        }
        // 取反操作，别忘了前面还有一堆0
        int mask = (1 << (nums << 1)) - 1;
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < dp1.length; i++) {
            // 相反的状态位  如果最后14位为有效，假如i：0..0 10 0010 1001 0001
            //                         那么oppose：0..0 01 1101 0110 1110
            int oppose = mask ^ i;
            if (dp1[i] != Integer.MAX_VALUE && dp1[oppose] != Integer.MAX_VALUE)
                res = Math.min(res, dp1[i] + dp1[oppose]);
        }

        return res == Integer.MAX_VALUE ? -1 : res;
    }
    // 该方法利用了分治，如果要选择8轮，那么时间复杂度就变成了：C(120,4) 约为：8*10^6 可以过了


    private static void h(int[][] programs, int size, int index, int pick, int cost, int rest,
                          int[] dp) {
        if (rest == 0)
            dp[pick] = Math.min(dp[pick], cost);
        else {
            if (index != size) {
                h(programs, size, index + 1, pick, cost, rest, dp);
                int choice = (1 << programs[index][0]) | (1 << programs[index][1]);
                if ((choice & pick) == 0) {
                    h(programs, size, index + 1, pick | choice, cost + programs[index][2],
                            rest - 1, dp);
                }
            }
        }
    }


}
