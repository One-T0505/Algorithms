package Basic.Sort;

// 桶排序是一种容器思想，无关乎具体的实现，桶排序是不基于比较的排序。基数排序、计数排序是具体的实现方法。

import java.util.Arrays;

public class BucketSort {

    // 计数排序
    public static void countSort(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        int max = Integer.MIN_VALUE;
        for (int item : arr) max = Math.max(item, max);
        int[] bucket = new int[max + 1];
        for (int k : arr) bucket[k]++;
        int index = 0;
        for (int i = 0; i < bucket.length; i++) {
            if (bucket[i] == 0)
                continue;
            for (int j = 0; j < bucket[i]; j++) {
                arr[index++] = i;
            }
        }
    }

    // 基数排序：数组中的值必须非负,默认是十进制整数。
    public static void radixSort_main(int[] arr) {
        if (arr == null || arr.length < 2)
            return;
        radixSort(arr, 0, arr.length - 1, maxDigits(arr, 0, arr.length - 1));

        }

    // 该方法为基数排序的高级实现版，无需队列。
    private static void radixSort(int[] arr, int l, int r, int maxDigits) {
        final int radix = 10; // 10进制数以10作为基底
        int[] help = new int[r - l + 1];
        int curDigit = 0;
        // 数组中最大的数有几位数，就做几次循环
        for (int d = 1; d <= maxDigits; d++) {  // d等于1时代表按照个位来排序
            int[] count = new int[radix];   // count中每个索引上的值表示：该轮循环中，数位上为索引的元素的个数
            // for循环处理好count数组
            for (int i = l; i <= r; i++) {
                curDigit = getDigit(arr[i], d);
                count[curDigit]++;
            }
            // for循环将count数组改造：每个元素都是从头到自己的累加和
            for (int i = 1; i < radix; i++)
                count[i] += count[i - 1];
            // 逆向遍历数组，反向定位该元素应该填在help哪里; 这是最难理解的一个for循环
            // 举例,假如此时是按照个位来操作：arr = {101, 267, 133, 104, 352, 601}
            // count本来是这样的 = [0, 2, 1, 1, 1, 0, 0, 0, 0, 0] 表示个位数分别为0-9的元素
            // 有几个。经过上面的累加处理，count就成这样的了：count = [0, 2, 3, 4, 5, 5, 5, 5, 5, 5]。这样理解：
            // 个位数<=0的有0个，个位数<=1的有2个，个位数<=2的有3个
            // 个位数<=3的有4个。。。 现在对arr从右至左遍历：第一个是601，个位为1，那就说明按照传统的基数排序进队列来说，
            // 601一定是1号队列的最后一个，那么出队列排好序后，
            // 601一定是放在help[1]的，因为从count中可以得知count[1]=2，所以601一定这两个数的最后一个.
            for (int i = r; i <= l; i--) {
                curDigit = getDigit(arr[i], d);
                help[count[curDigit] - 1] = arr[i];
                count[curDigit]--;
            }
            int i = 0;
            for (curDigit = 0, i = l; i <= r; i++, curDigit++)
                arr[i] = help[curDigit];
        }

    }

    // 得到val这个数第d位的数，其中d=1表示获得个位数上的值
    private static int getDigit(int val, int d) {
        int res = 0;
        for (int i = 0; i < d; i++) {
            res = val % 10;
            val /= 10;
        }
        return res;
    }


    // 从arr[l] 到 arr[r]之间返回最大数有几位数。比如：100有3位数
    public static int maxDigits(int[] arr, int l, int r) {
        int max = Integer.MIN_VALUE;
        for (int item : arr) max = Math.max(item, max);
        int digits = 0;
        while (max != 0){
            max /= 10;
            digits++;
        }
        return digits;
    }

    // 对数器
    public static int[] generateRandomArray(int maxSize, int range){
        int size = (int) (Math.random() * (maxSize + 1));
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (range + 1));
        }
        return arr;
    }


    public static void main(String[] args) {

        // 对数器测试计数排序 100,0000 次
        for (int i = 0; i < 1000000; i++) {
            int[] randomArray = BucketSort.generateRandomArray(30, 1000);
            int[] copy = new int[randomArray.length];
            for (int j = 0; j < copy.length; j++)
                copy[j] = randomArray[j];
            BucketSort.countSort(randomArray);
            Arrays.sort(copy);
            for (int j = 0; j < randomArray.length; j++) {
                if (copy[j] != randomArray[j])
                    throw new RuntimeException("出错！");
            }
        }
        System.out.println("成功！");
        System.out.println("===========================");

    }
}
