package Hard;

// leetCode1488
// 你的国家有无数个湖泊，所有湖泊一开始都是空的。当第 n 个湖泊下雨前是空的，那么它就会装满水。如果第 n 个湖泊
// 下雨前是满的 ，这个湖泊会发生洪水。你的目标是避免任意一个湖泊发生洪水。
// 给你一个整数数组 rains ，其中：
//  1.rains[i] > 0 表示第 i 天时，第 rains[i] 个湖泊会下雨。
//  2.rains[i] == 0 表示第 i 天没有湖泊会下雨，你可以选择一个湖泊并抽干这个湖泊的水。
// 请返回一个数组 ans ，满足：
//  1.ans.length == rains.length
//  2.如果 rains[i] > 0 ，那么ans[i] == -1 。
//  3.如果 rains[i] == 0 ，ans[i] 是你第 i 天选择抽干的湖泊。
// 如果有多种可行解，请返回它们中的任意一个。如果没办法阻止洪水，请返回一个空的数组。
// 请注意，如果你选择抽干一个装满水的湖泊，它会变成一个空的湖泊。但如果你选择抽干一个空的湖泊，那么将无事发生。

// 1 <= rains.length <= 10^5
// 0 <= rains[i] <= 10^9


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class RainsInLake {

    // 注意编号：天数是从0开始的，湖泊的编号是从1开始的。现在再来梳理下题意：
    // 当数组元素>0表示选择一个湖泊下雨，我们没有任何机会工作，我们唯一工作的机会就是等到第j天终于==0的时候，
    // 我们就可以在0～j-1中下过雨的湖里抽干水，避免之后再次在这个湖里下雨的时候溢出。
    // 所以我们要返回的数组就是：我们不能工作的时候，就填-1，能工作的时候就把选择抽干水的湖泊编号填入，
    // 但凡我们不能避免水灾了，就返回空数组，表示无意义。

    // 思路：准备一个集合，遍历到当前元素i时，set里存放的是已经装满水的湖泊；等到遍历到rains[j]==0时，就从
    // 集合里选一个湖泊抽干，选哪个湖泊呢？当然是从j+1开始往后，最先下雨，且在集合里的湖泊了，因为它迫在眉睫
    // 是最近的且会发生洪灾的湖泊。


    public static int[] avoidFlood(int[] rains) {
        int N = rains.length;
        int[] res = new int[N];
        // 需要一个集合记录哪些湖里已经有水了
        HashSet<Integer> set = new HashSet<>();
        // 需要一个小根堆 来抉择 选哪个湖的水抽干
        // Work实现了比较方法，按照谁先下雨谁在前面，所以堆顶就是当前日期之后最先下雨的，预示着最紧急的任务，
        // 所以用小根堆让其在最上面。
        PriorityQueue<Work> heap = new PriorityQueue<>();
        // 而且我们要重新制作一个预处理结构，做一张二维结构
        // key表示湖泊的编号  value表示该湖泊会下雨的日期
        HashMap<Integer, LinkedList<Integer>> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            if (rains[i] > 0) {
                if (!map.containsKey(rains[i]))
                    map.put(rains[i], new LinkedList<>());
                // 用队列串起来，这样一个湖泊对应下雨的日期也是递增的
                map.get(rains[i]).addLast(i);
            }
        }

        // 正式的算法流程
        for (int i = 0; i < N; i++) {
            if (rains[i] > 0) {
                if (set.contains(rains[i])) // 直接gg
                    return new int[0];
                // 下雨的湖是干的
                set.add(rains[i]);
                res[i] = -1;
                // 因为已经来到了自己下雨的时候，那必然是就是队列开头的日期，此时就可以把它丢了
                // 因为已经正在下雨了  然后新的头部就是当前湖泊下一次下雨的时候
                map.get(rains[i]).pollFirst();
                // 如果丢弃之后，该队列为空了，说明该湖泊再也不会下雨了；又因为上面的else已经表明
                // 该湖泊此时是干的，当前下雨也只是把它填满，之后再也不会下雨了，那么我们在下次选择湖抽水
                // 时就可以完全不考虑该湖泊了，因为之后再也不会下雨了，该湖泊必然不会发生洪水。
                // 如果还有，那说明该湖泊之后还会下雨，我们还需要把它放在堆里
                if (!map.get(rains[i]).isEmpty())
                    heap.add(new Work(rains[i], map.get(rains[i]).peekFirst()));
            } else { // 要选择湖抽水
                if (!heap.isEmpty()) {
                    Work work = heap.poll();
                    set.remove(work.lake);
                    res[i] = work.lake;
                } else { // 默认选择1号湖抽水  题目规定
                    res[i] = 1;
                }
            }
        }
        return res;
    }

    // ===========================================================================================
    // 下面的方法是自己写的方法，但是没完全AC，只跑过了一大半的例子。
    public static int[] avoidFlood2(int[] rains) {
        int N = rains.length;
        int[] res = new int[N];
        int p = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < N; i++) {
            if (rains[i] == 0) { // 选择一个湖抽干
                // 这是一个边界情况：有可能有几个连续的0出现，比如有3个连续的0出现，但是有水的湖泊只有2个
                // 当没有湖泊有水的时候，还需要选择湖抽水，其实就是题干说的无用功，这里题目规定默认选择1号湖泊
                if (!set.isEmpty()) {
                    for (int j = i + 1; j < N; j++) {
                        if (rains[j] > 0 && set.contains(rains[j])) {
                            set.remove(rains[j]);
                            res[p++] = rains[j];
                            break;
                        }
                    }
                } else // 默认选择1号湖泊
                    res[p++] = 1;
            } else { // rains[i] > 0  天要下雨
                if (!set.contains(rains[i])) {
                    res[p++] = -1;
                    set.add(rains[i]);
                } else
                    return new int[]{};
            }
        }
        return res;
    }

    public static class Work implements Comparable<Work> {

        public int lake; // 湖泊编号
        public int next; // 下一次下雨的日期

        public Work(int lake, int next) {
            this.lake = lake;
            this.next = next;
        }

        @Override
        public int compareTo(Work o) {  // 比较规则是谁越早下，谁就在前面
            return next - o.next;
        }

    }


}
