package class16;


// 给定整数power， 给定一个数组arr， 给定一个数组reverse，含义如下：
// arr的长度一定是2的power次方；reverse中的每个值一 定都在0~ power范围。
// 例如power= 2, arr= {3, 1, 4, 2}， reverse二{0, 1, 0, 2}
// 任何一个在前的数字可以和任何一个在后的数，构成一对数。可能是升序关系、相等关系或者降序关系。
// 比如arr开始时有如下的降序对：(3,1)、(3,2)、(4,2)，一共3个。
// 接下来根据reverse对arr进行调整：
// reverse[0] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
// [3,1,4,2]，此时有3个逆序对。
// reverse[1] = 1, 表示在arr中，划分每2(2的1次方)个数一组，然后每个小组内部逆序，那么arr变成
// [1,3,2,4]，此时有1个逆序对
// reverse[2] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
// [1,3,2,4]，此时有1个逆序对。
// reverse[3] = 2, 表示在arr中，划分每4(2的2次方)个数一组，然后每个小组内部逆序，那么arr变成
// [4,2,3,1]，此时有5个逆序对。
// 所以返回[3,1,1,5]，表示每次调整之后的逆序对数量。
//
// 输入数据状况：
// power的范围[0,20]
// arr长度范围[1,10^7]
// reverse长度范围[1,10^6]

public class ReversedPairs {

    // 逆序对的统计方式在归并排序时讲过，这里使用一种新的统计逆序对的方法。就按照上面的例子，arr=[3, 1, 4, 2],
    // power=2.那么可能的分组大小就是2，4(2的1～2次方)，生成一个预处理信息，为每个groupSize设置两个信息，
    // 一个是：以当前groupSize对arr分组，统计出的逆序对数量；另一个是：正序对数量。比如groupSize==2，就是说两个
    // 数一组，此时是可以直接判断是否为逆序对的。(3,1)是，(4,2)是；所以groupSize==2时，逆序对数量为2，正序对为0。
    // 当groupSize==4时，不能重复计算groupSize==2时的情况，所以此时将当前4个元素为一个分组切成两半，从前一半中任
    // 意选一个数，去算后面一组中有多少个数能和它构成逆序对关系。遍历前一组的所有元素，就能统计出以当前groupSize对arr
    // 分组的逆序对数量。只有这样计算才是不重复的。groupSize==4时，数组只能分成一组，将其分成两半，从前一半中选择3，那么
    // 后一半中能与其构成逆序对的2；从前一组选择1，后一半中能与其构成逆序对的没有。所以groupSize==4时，逆序对数量为1，
    // 正序对为3。将所有分组下的逆序对数量相加才是整体的结果。如下表格所示：
    //
    //    groupSize    increase    decrease
    //        2           0           2
    //        4           3           1
    //
    // 为啥要费这么多心思搞这个表格？当然是为了这个题目专门定制的预处理信息，方便后续不停逆序时能很快统计出结果。
    // 试想一下：reverse=2^1时，即每两个元素为一组，组内逆序，是不会影响groupSize==4的表格的数据的，因为回想一下，
    // groupSize==4时的数据是如何统计出来的？是将其分成前后两组，每组大小为2，从前部分挑一个，后部分挑一个，每次PK的元素
    // 来自不同的组，现在将其逆序，他们的相对次序不变，之前是位于前半部分的逆序后仍然都在前半部分，所以表格第二行数据不变。
    // 但是第一行数据会被影响，最重要的来了！！！！！！！！！！ 受影响，只需要要将受影响的那行，正逆序对的数量交换即可，
    //

    public static int[] reversePair(int[] arr, int[] reverse, int power) {
        // 制作原始数组的逆序数组，为什么需要这个逆序数组？因为用原始数组生成上面表格中的逆序对信息可以定义一个方法f，
        // 其正序对信息只需要对原始数组的逆序数组执行相同的f方法就可以得到正序对信息，就不用专门写一个统计正序对信息的方法。
        int[] reverseArr = reverseArray(arr);
        int N = arr.length;
        // 分别用两个一维数组记录逆序对和正序对信息
        int[] down = new int[power + 1];
        int[] up = new int[power + 1];
        // 统计信息
        f(arr, 0, N - 1, power, down);
        f(reverseArr, 0, N - 1, power, up);
        int[] res = new int[reverse.length];
        for (int i = 0; i < reverse.length; i++) {
            int curPower = reverse[i];
            // 从0～curPower都会受影响，正逆序对信息需要交换
            // 注意：这里j要从1开始，因为2的0次方==1，构不成对，所以两个信息数组的第0个元素都无意义
            for (int j = 1; j <= curPower; j++)
                swap(j, up, down);
            for (int j = 1; j <= power; j++)
                res[i] += down[j];
        }
        return res;
    }


    // 生成arr的逆序数组并返回
    private static int[] reverseArray(int[] arr) {
        int N = arr.length;
        int[] res = new int[N];
        for (int i = 0; i < N; i++) {
            res[i] = arr[N - 1 - i];
        }
        return res;
    }


    // 该方法其实就是归并排序方法，不过此时merge过程中还需要记录我们想要的信息
    // 目标是：arr[L...R]完成排序！并在排序过程中记录信息
    // L...M左  M...R右  merge
    // L...R的长度一定是2的power次方
    private static void f(int[] arr, int L, int R, int power, int[] info) {
        if (L == R)
            return;
        int mid = L + ((R - L) >> 1);
        f(arr, L, mid, power - 1, info);
        f(arr, mid + 1, R, power - 1, info);
        info[power] += merge(arr, L, mid, R);
    }

    private static int merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[r - l + 1];
        int i = 0, p1 = l, p2 = mid + 1;
        int res = 0;
        while (p1 <= mid && p2 <= r) {
            res += arr[p1] > arr[p2] ? (mid - p1 + 1) : 0;
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid)
            help[i++] = arr[p1++];
        while (p2 <= r)
            help[i++] = arr[p2++];
        for (i = 0; i < help.length; i++)
            arr[l + i] = help[i];
        return res;
    }


    private static void swap(int pos, int[] up, int[] down) {
        int tmp = up[pos];
        up[pos] = down[pos];
        down[pos] = tmp;
    }
}
