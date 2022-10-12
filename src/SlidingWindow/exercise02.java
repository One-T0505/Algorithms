package SlidingWindow;
import java.util.LinkedList;
import utils.arrays;


// 给定一个整型数组arr, 和一个整数num。某个arr中的子数组sub,如果想达标，必须满足:
// sub中最大值 - sub中最小值 <= num。 返回arr中达标子数组的数量。
// 注意sub的长度是不固定的，sub可以是arr的任意一个子数组，所以考虑的时候要仔细。
// eg：[1, 5, 3] 该数组的子数组有[1] [5] [3] [1, 5] [5, 3] [1, 5, 3] 一共6个


// 利用滑动窗口思想 将时间复杂度优化至：O(N)
// 首先有两个小技巧得明白：
//   1.当一个子数组 L..R 符合规定后，那么该子数组的所有子数组都必定满足规定
//   2.当一个子数组 L..R-1 符合规定，并且 L..R 就不符合规定了，那么，从 L..R+1  L..R+2 都不可能满足
// 思路：先确定左边界L=0，然后让R不断向右扩，扩到第一个不符合规定的位置，此时就可以确定以0开头的子数组一共有多少个了，
//      但是这里不要考虑技巧1，因为虽然此时我们也可以确定从 L+1.. R-3这样的子数组也是符合的，但是如果现在计算了，后续计算中可能
//      会重复计算，所以每次就算以左边界开头的符合要求的子数组有多少个就行。
//      计算好后，再让L右移一个位置，此时左边界换成新的了，那就让R从之前那个不符合的位置继续扩，看现在换了新边界后，能扩到哪里，
//      然后又到了第一个不符合的地方，此时又可以计算出以1为左边界的子数组有多少个符合要求的了。

public class exercise02 {

    public static int limitedSubArray(int[] arr, int target){
        if (arr == null || arr.length == 0 || target < 0)
            return 0;
        // 利用两个双端队列分别记录当前窗口的最大值和最小值
        LinkedList<Integer> maxQ = new LinkedList<>();
        LinkedList<Integer> minQ = new LinkedList<>();
        int N = arr.length, count = 0, R = 0;
        for (int L = 0; L < N; L++) {
            while (R < N){
                // 更新最大值情况
                while (!maxQ.isEmpty() && arr[maxQ.peekLast()] <= arr[R])
                    maxQ.pollLast();
                maxQ.addLast(R);
                // 更新最小值情况
                while (!minQ.isEmpty() && arr[minQ.peekLast()] >= arr[R])
                    minQ.pollLast();
                minQ.addLast(R);
                if (arr[maxQ.peekFirst()] - arr[minQ.peekFirst()] <= target)
                    R++;
                else
                    break;
            }
            count += (R - L);
            // 如果即将要过期的左边界L此时是队列中的头部就弹出
            if (maxQ.peekFirst() == L)
                maxQ.pollFirst();
            if (minQ.peekFirst() == L)
                minQ.pollFirst();
        }
        return count;
    }

    // 暴力方法用作对数器 时间复杂度：O(N3)
    public static int verify(int[] arr, int target){
        if (arr == null || arr.length == 0 || target < 0)
            return 0;
        int count = 0;
        for (int L = 0; L < arr.length; L++) { // 左边界
            for (int R = L; R < arr.length; R++) {
                int max = Integer.MIN_VALUE;
                int min = Integer.MAX_VALUE;
                for (int index = L; index <= R; index++) {
                    max = Math.max(max, arr[index]);
                    min = Math.min(min, arr[index]);
                }
                if (max - min <= target)
                    count++;

            }
        }
        return count;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            int[] src = arrays.generateRandomArray(10, 50);
            int[] copy = arrays.copyArray(src);
            int res1 = limitedSubArray(src, 15);
            int res2 = verify(copy, 15);
            if (res1 != res2){
                System.out.println("Failed!");
                System.out.println(res1 + "\t" + res2);
                arrays.printArray(src);
                return;
            }
        }
        System.out.println("AC");
    }
}
