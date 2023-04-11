package Hard;

// leetCode475
// 冬季已经来临。你的任务是设计一个有固定加热半径的供暖器向所有房屋供暖。在加热器的加热半径范围
// 内的每个房屋都可以获得供暖。现在，给出位于一条水平线上的房屋 houses 和供暖器 heaters 的位置，
// 请你找出并返回可以覆盖所有房屋的最小加热半径。
// 说明：所有供暖器都遵循你的半径标准，加热的半径也一样。

// 实例:
// 输入: houses = [1,2,3], heaters = [2]
// 输出: 1
// 解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。

// 输入: houses = [1,2,3,4], heaters = [1,4]
// 输出: 1
// 解释: 在位置1, 4上有两个供暖器。我们需要将加热半径设为1，这样所有房屋就都能得到供暖。

import java.util.Arrays;

public class Heater {


    // 思路：先将两个数组排好序。
    // houses =  [1, 4, 6, 9, 14, 21, 22, 25]
    // heaters = [2, 5, 8, 14, 17, 19, 21, 23]
    // 运用双指针技巧。我们的目的是针对于每个房屋，找到其对应的最优供火点，求此时的供火半径，每个房屋都有其
    // 最优的供火点和产生的供火半径，对所有的这些最优供火半径求最大值就是结果。因为所有火炉都得是同样的半径，所以
    // 得选最大的，才能覆盖所有。
    // 2号火炉对1号房屋供暖需要半径为1，5号火炉对1号房屋供暖需要半径4，至此我们就可以断定2号火炉是
    // 1号房屋的最优供暖点，因为5号到房屋1的距离比2到1的更长，那么后面的火炉也不可能再是了。
    // 4号房屋从2号火炉开始，发现5号火炉比2号火炉更优。但是不能确定5号是不是房屋4的最优，所以还要继续往后看，
    // 8号火炉相比于5到4的距离上升了，至此我们可以确认5号火炉就是房屋4的最优供火点。就这样一直循环下去，
    // 指向火炉的指针是不会回退的。

    // 这里有个边界问题
    // houses =  [1, 6, 7, 9, 14, 21, 22, 25]
    // heaters = [2, 4, 8, 8, 17, 19, 21, 23]
    // 当我们为6号房屋找最佳供火点的时候，是会发现4到6和8到6的距离一样，那是否还能确定4号是房屋6的最佳供火点呢？
    // 不能，如果碰见相等，就得指向下一个，就不能认为4号是6的最佳供火点，而是暂且把8号当作最优，然后继续往后找，
    // 如果后续的距离依然不变，那就继续让最佳点后移。也就是说碰见相等的时候也得继续往后找。
    // houses =  [3, 25, 45, 50]
    // heaters = [1, 1, 1, 1]
    // 如果相等的时候不跳过，那么0位置的1会是25的最佳供火点，也是45的最佳供火点。。。 火炉数组的指针就会卡住不动

    // 这个边界问题是在写代码的时候发现的，leetCode给的测试用例的最后一个没通过，才暴露出这个问题，并且那个
    // 实例的长度巨长，根本不可能通过那个实例来找出代码的问题，所以写对数器非常地重要！！！！
    // 笔试的时候不要依赖系统给你的错误实例，要自己写对数器！！！！！！

    public int findRadius(int[] houses, int[] heaters) {
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int res = 0;
        int N = houses.length;
        for (int i = 0, j = 0; i < N; i++) {
            while (!best(houses, i, heaters, j))
                j++;
            res = Math.max(res, Math.abs(heaters[j] - houses[i]));
        }
        return res;
    }


    // 判断heaters[j]是不是houses[i]的最佳供火点
    private boolean best(int[] houses, int i, int[] heaters, int j) {
        // 如果j是最后一个火炉，那必然是i的最佳供火点
        return j == heaters.length - 1 ||
                // 要么就是当前火炉的距离小于下一个火炉到房屋的距离才能认为当前火炉是最佳供火点
                // 如果相等，都不能认为当前的是最佳的，原因上面说过了，相等时，就得换成下一个了
                Math.abs(heaters[j] - houses[i]) < Math.abs(heaters[j + 1] - houses[i]);

    }
}
