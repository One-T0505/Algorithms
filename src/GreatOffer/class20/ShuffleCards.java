package GreatOffer.class20;

// 完美洗牌问题
// 给定一个长度为偶数的数组arr，假设长度为N*2
// 左部分: arr[L1.....Ln]  右部分: arr[R1....Rn]
// 请把arr调整成arr[L1,R1,L2,R2,L3,R3,...,Ln,Rn]
// 要求:时间复杂度O(N)， 额外空间复杂度0(1)

public class ShuffleCards {

    // 要解决这个问题，就要先了解一个子问题模型。给一个数组arr，分成了左右两部分，如何原地交换左右两部分。
    // 比如arr=[4, 2, 5, 6, 1, 7] 假设左部分是前4个，右部分是后两个，想让其变成：[1, 7, 4, 2, 5, 6]
    // 但是只能用有限几个变量。方法：先将左部分逆序，再将右部分逆序，最后将整个数组逆序

    // L..M 为左部分  M+1..R 为右部分
    private static void exchange(int[] arr, int L, int M, int R) {
        reverse(arr, L, M);
        reverse(arr, M + 1, R);
        reverse(arr, L, R);
    }


    // 原地将arr[L..R]逆序
    private static void reverse(int[] arr, int L, int R) {
        while (L < R) {
            int tmp = arr[L];
            arr[L++] = arr[R];
            arr[R--] = tmp;
        }
    }
    // =============================================================================================




    // 有了上面的计算最终位置的方法后，我们就可以在原始数组上循环怼出下标，完成所有元素的调整.比如数组长度为4，那么
    // 左右各两个元素。1 2 3 4  最终要达到：3 1 4 2
    // 首先1要去2，然后把2处的元素怼出去，然后2处的元素要去4，把4位置的元素怼出去，4位置的要去3位置的，3要去1位置的，
    // 就完成了整个的循环，最终就把整个数组调整好了。但是真的是这样吗？
    // 比如数组长度为6：1 2 3 4 5 6  循环是这样的：1->2->4->1  这是个死循环，没办法完成整个数组的迭代
    // 而 3->6->5->3  3 5 6 这三个数自己又是一个单独的循环，所以对于这样的一个数组，他就需要两个起始点才能完成整个迭代
    // 但是数组长度是一个任意的偶数，换一个长度可能需要的起始点个数以及位置都不一样。其实，这样的问题已经被研究过了，有最后
    // 总结出的公式和规律，我们只需要背住就行了。规律是这样的：
    //
    // 当数组长度刚好满足：N == 3^k - 1(2, 8, 26) 时，起始点为：3^0...3^(k-1)
    //   比如N==2，那么起始点就是：3^0==1 只有1这个位置作起始点就够了
    //   比如N==8，那么起始点就是：3^0, 3^1 只有1和3两个位置作起始点就够了
    //
    // 当N是不满足上述公式的偶数时，比如N==12，那就找<=N，且符合上述公式的数，那么<=12，且符合上述公式的长度就是8。
    // L1L2L3L4L5L6 R1R2R3R4R5R6  一开始是这样的。因为我们想弄出长度为8的这么一个长度的数组，所以我们需要
    // 让左右各4各元素来拼凑，于是这里就用到了exchange方法，范围是L5~R4，变换之后：
    // L1L2L3L4R1R2R3R4L5L6R5R6  前8各元素就是刚好长度为8的数组情况，于是就可以用上面的定理了。剩下的部分再去解决，
    // 剩下的L5L6R5R6 长度为4，想找到最近的符合公式的长度为2，于是左右各留出1各元素，所以再让L6～R5旋转一下；最后剩余
    // 的L6R6长度刚好为2，直接用定理。
    // 所以当N为其他偶数时，就需要拆分成符合公式长度的几个部分，逐一解决


    public static void shuffleCards(int[] arr) {
        if (arr != null && arr.length != 0 && ((arr.length & 1) == 0))
            shuffle(arr, 0, arr.length - 1);
    }


    // 在arr[L..R]上做完美洗牌的调整（arr[L..R]范围上一定要是偶数个数字）
    private static void shuffle(int[] arr, int L, int R) {
        while (R - L + 1 > 0) {
            int len = R - L + 1;
            int radix = 3;
            int k = 0;
            // 计算小于等于len并且是离len最近的，满足(3^k)-1的数
            while (radix <= len + 1) {
                radix *= 3;
                k++;
            }
            // 此时已找到最大的k  如果长度为12，那么到这里radix==27，half是每边要选几个元素
            int half = (radix / 3 - 1) >> 1;
            int M = (L + R) >> 1;
            exchange(arr, L + half, M, M + half);  // 这里下标是从0开始的
            // 旋转完成后，从L开始算起，长度为2*half的部分进行下标连续怼
            cycle(arr, L, half << 1, k);
            // 解决了前2*half个元素，剩下的继续处理
            L += half << 1;
        }
    }


    // 从L位置开始，往右N的长度这一段，做下标连续推
    // 出发位置依次为1,3,9...
    private static void cycle(int[] arr, int L, int N, int k) {
        // 找到每一个出发位置trigger，一共k个
        // 每一个trigger都进行下标连续推
        // 出发位置是从1开始算的，而数组下标是从0开始算的。
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
            int preVal = arr[L + trigger - 1];  // 被怼出去的那个值
            int cur = destination(trigger, N);
            while (cur != trigger) { // 循环
                int tmp = arr[L + cur - 1];
                arr[L + cur - 1] = preVal;
                preVal = tmp;
                cur = destination(cur, N);
            }
            arr[L + cur - 1] = preVal;
        }
    }


    // arr中每一个位置的元素最终要去的位置其实可以用公式计算出来。我们现在的目标是将原始数组调整为：
    // R1L1R2L2...RnLn，如果得到了这样的结果，只需要再两两交换就能得到最终结果。 假设下标从1开始，数组长度为8：
    // 1 2 3 4 5 6 7 8      前4个为左部分，后4个为右部分    最终想要达到的效果
    // 5 1 6 2 7 3 8 4
    // 如果i位置属于左部分，那么i位置最终要到的位置是：2*i
    // 如果i位置属于右部分，那么i位置最终要到的位置是：(i-N/2)*2-1
    // 下面的方法就是i计算最终目标位置
    private static int destination(int i, int len) { // len一定是偶数，并且i是从1开始的
        if (i <= (len >> 1))
            return i << 1;
        else
            return ((i - (len >> 1)) << 1) - 1;
    }


    public static void main(String[] args) {
        int[] arr = {7, 2, 4, 9, 1, 5, 3, 8, 6, 0, 13, 10};
        shuffleCards(arr);
        for (int e : arr)
            System.out.print(e + " ");
        System.out.println();
    }
}
