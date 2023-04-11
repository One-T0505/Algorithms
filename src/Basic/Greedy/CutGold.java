package Basic.Greedy;

// 一块金条切成两半，是需要花费和长度数值一样的铜板的。比如长度为20的金条，不管怎么切，
// 都要花费20个铜板。一群人想整分整块金条，怎么分最省铜板?
// 例如，给定数组{10,20,30}，代表一共三个人， 整块金条长度为60，金条要分成10，20, 30三个部分。
// 如果先把长度60的金条分成10和50，花费60;再把长度50的金条分成20和30，花费50;-共花费110铜板。
// 但如果先把长度60的金条分成30和30，花费60;再把长度30金条分成10和20，花 费30;-共花费90铜板。
// 输入一个数组，返回分割的最小代价。

// 这个题目的经典贪心方法就是哈夫曼编码方法。申请一个小根堆，将这个数组放进去，按照从小到大排序，然后每次弹出两个数，
// 相加之后再入堆，直到堆里只剩下一个元素。eg：[12, 3, 5, 7, 2, 8, 6]
// 放入小根堆排序后就是：[2, 3, 5, 6, 7, 8, 12]   弹出2，3，入堆5 -> [5, 5, 6, 7, 8, 12]
// 弹出两个5，入堆10 -> [6, 7, 8, 10, 12]       弹出6，7，入堆13 -> [8, 10, 12, 13]
// 弹出8，10，入堆18 -> [12, 13, 18]           弹出12，13，入堆25 -> [18, 25]
// 弹出18，25，入堆43 -> [43]
//
//                        43
//                    /       \
//                  18         25
//                /    \     /    \
//               8     10   12    13
//                    /  \       /  \
//                   5    5     6    7
//                  / \
//                 2   3
//
// 形成了这样一棵哈夫曼树，所有非叶子结点的和即为这道题目要求的最小代价。没有为什么要这么做，这就是一种经验

import java.util.PriorityQueue;

public class CutGold {

    public static int solution(int[] arr){
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int j : arr)
            heap.add(j);
        int cost = 0, sum = 0;
        while (heap.size() > 1){
            sum = heap.poll() + heap.poll();
            cost += sum;
            heap.add(sum);
        }
        return cost;
    }

    // 暴力解法当作对数器
    public static int verify(int[] arr){
        if (arr == null || arr.length == 0)
            return 0;
        return process(arr, 0);
    }

    // 该方法表示待合并的数都还在arr里， cost表示之前的合并行为产生的代价总和
    // arr中还剩一个数字的时候，停止合并，返回总代价
    private static int process(int[] arr, int cost) {
        if (arr.length == 1)
            return cost;
         int res = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                res = Math.min(res, process(mergeTwo(arr, i, j), cost + arr[i] + arr[j]));
            }
        }
        return res;
    }

    private static int[] mergeTwo(int[] arr, int i, int j) {
        if (arr == null || arr.length < 2)
            return null;
        int[] res = new int[arr.length - 1];
        res[0] = arr[i] + arr[j];
        int index = 1;
        for (int k = 0; k < res.length; k++) {
            if (k != i && k != j)
                res[index++] = arr[k];
        }
        return res;
    }
}
