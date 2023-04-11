package Basic.Greedy;

// 输入:正数数组costs、正数数组profits、正数K、正数M
// costs[j]表示i号项目的花费, profits[i]表示i号项目在扣除花费之后还能挣到的钱(利润)
// K表示你只能串行的最多做k个项目，M表示你初始的资金
// 说明: 每做完一个项目，马上获得的收益，可以支持你去做下一个项目。不能并行的做项目。
// 输出:你最后获得的最大钱数。

// 思路：申明一个项目类，有两个成员变量cost和profit。申请一个小根堆，将所有的项目按照成本排序，根据当前的资金M，弹出所有你能做的
// 项目，并把他们一一加入一个按照利润从大到小排序的大根堆，然后弹出大根堆堆顶元素，这样得到的就是在资金范围内能做的且利润最高的

import java.util.PriorityQueue;

public class MakeProfits {

    static class Project{
        public int cost;
        public int profit;

        public Project(int cost, int profit) {
            this.cost = cost;
            this.profit = profit;
        }
    }

    public static int makeProfits(int[] costs, int[] profits, int K, int M){
        PriorityQueue<Project> costHeap = new PriorityQueue<>(((o1, o2) -> o1.cost - o2.cost));
        PriorityQueue<Project> profitHeap = new PriorityQueue<>(((o1, o2) -> o2.profit - o1.profit));

        for (int i = 0; i < costs.length; i++)
            costHeap.add(new Project(costs[i], profits[i]));
        for (int i = 0; i < K; i++) {
            while (!costHeap.isEmpty() && costHeap.peek().cost <= M)
                profitHeap.add(costHeap.poll());
            // 没一个可以做的项目
            if (profitHeap.isEmpty())
                return M;
            M += profitHeap.poll().profit;
        }
        return M;
    }
}
