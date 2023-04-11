package Basic.BitOperation;

import java.util.HashMap;
import java.util.HashSet;
import utils.arrays;

public class XOR {
//    1.问题描述：一个数组中，只有一个值出现过奇数次，其他数值都出现过偶数次，用O(n)的时间复杂度求出该出现过奇数次的值。
//    只需要初始化一个值为0，让它和所有元素都异或运算一遍，就能求出唯一一个出现过奇数次的数
    public int one_odd_times_num(int[] arr){
        int eor=0;
        for (int j : arr)
            eor ^= j;
        return eor;
    }

    // 2.问题描述：一个数组中，有两个值出现过奇数次，其他数值都出现过偶数次，求出这两个现过奇数次的值。
    // 思路：假设两个出现过奇数次的值为a和b，令eor=0，与所有元素异或后，eor=a^b。既然a！=b 那么eor肯定不为0，
    // eor中为1的值代表的是 a和b对应数位上的值不同，肯定是一个1，一个0.以eor最低位的1为分割线将a和b区分开

    // 这个就是 leetCode260的原题
    public void two_odd_times_num(int[] arr){
        int eor = one_odd_times_num(arr);  // eor==a^b
        int right = eor & (~eor + 1);//固定操作：这样可以取出该数值的第一个不为空的1。eg： eor=10 （二进制：1010）-> right=0010
        int eor_1 = 0;
        for (int j : arr) {
            if ((j & right) == 0) { // 将right这个数值位上的值为0的数都选出来了
                eor_1 ^= j;        // 得到其中一个奇数次的值，eor_1可能是a，也可能是b
            }
        }
        eor ^= eor_1;     // 得到出现奇数次的值，存在eor中
        System.out.println("两个出现了奇数次的值为：\t"+ eor + "\t" + eor_1);
    }


    // 用异或实现数据交换
    public void swap(int a, int b){
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
    }

    // 3.根据上面的题目进一步加大难度：给一个数组，里面只有一种数出现了 k 次，其他数都出现了 m 次，并且 m>1, k<m。
    // 请找出是哪个数出现了 k 次。要求：时间复杂度O(N)  空间复杂度O(1)
    // 思路：int型整数每个都是32位，申请一个固定长度为32的数组bit，从0-31，每一个索引上的值表示其二进制对应的位上
    // 为1的数量。eg：遍历给的数组，假如有个数为9-->1001，所以 bit[0]++, bit[3]++。遍历完整个数组，bit中就包含
    // 了这些数据各个位上为1的总和。如果一个索引上的值不为 m 的整数倍，就说明出现 k 次的数在该索引对应的位上为1，将
    // 这些索引都找出来就能算出出现 k 次数的值。
    // 用这个方法可以轻松解决 leetCode137
    public static int kM(int[] arr, int k, int m){
        int[] bitCount = new int[32];
        for (int data : arr) {
            for (int i = 0; i < 32; i++) {
                // 向右移动 i 位再和 1 与操作就能判断数据原来第 i 位是否为1，不为1则结果为0，相加也不会影响
                bitCount[i] += (data >> i) & 1;
            }
        }
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if (bitCount[i] % m != 0)
                // 1 向左移动 i 位，再和其他数做或运算就相当于给该数在第 i 位上加1
                res |= (1 << i);
        }
        return res;
    }



    // leetCode137
    public static int singleNumber(int[] nums) {
        int[] bit = new int[32];
        int N = nums.length;
        for (int e : nums){
            for (int move = 0; move < 32; move++) {
                bit[move] += (e >> move) & 1;
            }
        }
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if (bit[i] % 3 != 0){
                res |= 1 << i;
            }
        }
        return res;
    }



    //为上述 kM 方法制作对数器,就是用另一种方法实现该过程。
    public static int testForKM(int[] arr, int k, int m){
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int data : arr) {
            if (hashMap.containsKey(data))
                hashMap.put(data, hashMap.get(data) + 1);
            else
                hashMap.put(data, 1);
        }
        for (int key : hashMap.keySet()) {
            if (hashMap.get(key) == k)
                return key;
        }
        return -1;
    }

    public static int[] generateRandomArray(int maxKinds, int range, int k, int m) {
        //实际不同种类的数最少得有2种
        int numKinds = (int) (Math.random() * maxKinds) + 2;
        // 随机确定那个唯一出现 k 次的数
        int kTimeNum = arrays.generateRandomNum(range);
        //数组的长度：k + (numKinds - 1) * m
        int[] arr = new int[k + (numKinds - 1) * m];
        int index = 0;
        // 先填充出现 k 次的那个数
        for (; index < k; index++)
            arr[index] = kTimeNum;
        numKinds--; // 剩下种类的数还没填入数组

        // 用于存放剩下 numKinds-1 种数，为什么用hashSet，因为要确保他们之间都各不相同
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(kTimeNum);
        while (numKinds != 0){
            int curNum = 0;
            do {
                curNum = arrays.generateRandomNum(range);
            }while (hashSet.contains(curNum));
            hashSet.add(curNum);
            numKinds--;
            for (int i = 0; i < m; i++) {   // 将新生成的没出现过的数立刻填入数组
                arr[index++] = curNum;
            }
        }
        // 当跳出 while 循环后， arr 数组已经全部填好，但是太有规律了，接下来把数组打乱
        for (int i = 0; i < arr.length; i++) {
            int j = (int) (Math.random() * arr.length);
            arrays.swap(arr, i, j);
        }
        return arr;
    }


    // 4.给一个数，计算它的二进制形式下有多少个1
    public static int oneBitNum(int N){
        int count = 0;
        while (N != 0){
            int mostRightOne = N & ((~N) + 1);  // 提取出最右位的1
            count++;
            N ^= mostRightOne;  // 将最右边的位计入count之后就没用了， 异或让原数最右位的1抹为0
        }
        return count;
    }

    public static void main(String[] args) {
        // maxKinds表示该数组最多有多少种不同的数，max表示一个数最多出现的次数
        int maxKinds = 10, range = 200, testTime = 1000000, max = 9;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            // 生成两个随机数，并确保 k < m
            int a = (int) (Math.random() * max) + 1; // 1-9
            int b = (int) (Math.random() * max) + 1; // 1-9
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            if (k == m)
                m++;

            int[] arr = generateRandomArray(maxKinds, range, k, m);
            int res1 = kM(arr, k, m);
            int res2 = testForKM(arr, k, m);
            if (res1 != res2)
                System.out.println("出错了！");
        }
        System.out.println("测试结束");
    }

}
