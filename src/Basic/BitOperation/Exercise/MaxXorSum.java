package Basic.BitOperation.Exercise;

import utils.arrays;


// 给一个数组，找出拥有最大异或和的子数组，返回最大异或和。一个数组的异或和就是所有元素都异或之后的结果。

public class MaxXorSum {

    // 思路：这个问题和找出一个数组中最大累加和子数组的解题思路类似。以任意一个元素作为结尾去寻找一批子数组，从中选出累加和最大的
    //      子数组；如何从以某个元素结尾的一批子数组中选出累加和最大的？
    //      这就要提前准备好一个前缀累加和数组help，help[i]表示arr[0~i]的累加和。如果要找出以i结尾的子数组中累加和最大的，
    //      只需要找到一个0<=j<i，使得help[j]最小即可。对于原数组中的每个元素都这样做一次，就能找到全局最优解。
    //      本题采取同样的思路来选取每批子数组，只不过这里有个问题就是，我知道从0~i所有元素的异或和，如果按照
    //      之前的思路就是，找个0<=j<i使得0～j的异或和最小，然后这样就能得到j+1~i最大的异或和，从而找到以i结尾的子数组中
    //      最大的异或和。但是问题就在这里，你怎么保证找到一个最小的，剩下的就是最大的？异或可不是加法，两个很大的数异或可能
    //      得到非常小的结果。所以就是对于每个help[i]，得逐一去和自己前面的异或和去做个判断才能找到，而不是O(1)的时间复杂度
    //      就能找到，所以找子数组最大累加和我们可以O(N)做到，但是求最大异或和得O(N^2)。
    //      这里我们引进前缀树来优化，使得时间复杂度降低为O(N)，选取子数组的模式仍然不变。

    // 一个小问题：假如我们知道了前5个数的异或和为x，即：arr[0] ^ arr[1] ^ arr[2] ^ arr[3] ^ arr[4] == x
    //           然后知道前2个数的异或和为y，即：arr[0] ^ arr[1] == y，那么如何得到后3个数的异或和？
    //           很简单，x ^ y 就是的。因为：
    //           x ^ y == (arr[0] ^ arr[1] ^ arr[2] ^ arr[3] ^ arr[4]) ^ (arr[0] ^ arr[1])   1⃣️
    //           我们知道，异或满足结合律，并且两个相同的数异或为0，所以 x ^ y == arr[2] ^ arr[3] ^ arr[4]


    // 如何用前缀树加速？
    // 常规思路就是对于每个help[i]，我们需要对它前面的所有元素异或，从而得到剩下的元素的异或和；对于每个help[i]都来
    // 时间复杂度就是O(N^2). 假设help[i] == 0110，对于help[0~i-1]的每个值我们都将其写成二进制形式加入前缀树，最高位
    // 是第一个结点。我们希望在树中找到一个串，让该串与help[i]异或后结果最大。异或的含义是：
    // 相同为0，不同为1。所以基于help[i]的每一位，除了最高符号位，我希望的是尽量和每一位值相反，这样才能尽可能得让1多。
    // 比如：0110，假设这不包含符号位，那我最希望和1001异或，这样得到的结果才最大，为1111.
    // 再看符号位，如果符号位为0表示正，1表示负数，所以符号位为0希望碰到0，符号位为1希望碰到1。所以符号位希望找到相同的，
    // 数值位希望碰到相反的。

    // 主方法
    public static int maxSumXor(int[] arr) {
        if (arr == null || arr.length == 0)
            return Integer.MIN_VALUE;
        int res = Integer.MIN_VALUE;
        int xor = 0;
        PrefixTree tree = new PrefixTree();
        // 为什么要提前添加一个0？这个很重要，因为当我们把数组第一个元素添加进去时，
        // 树上是空的。但是实际结果是，以arr[0]作为结尾的最大异或和子数组就是它本身，所以我们需要
        // 在树上加个0，让它在树上寻找时，只能找到0，和0异或后还是本身，所以才达到效果。
        // 而当后面元素添加进来时，他们都包含一个子数组就是从0～到自己本身，那么前面的异或和只剩0了。举个例子，
        // 如果help[i]和0异或，表示的就是刚好选择0～i的子数组；0为每个元素提供了选择从0到自己本身的子数组。
        tree.add(0);
        for (int e : arr) {  // 该for循环里的两个方法都是O(1)，所以时间复杂度为：O(N)
            xor ^= e;
            res = Math.max(res, tree.maxXor(xor));
            tree.add(xor);
        }
        return res;
    }

    // 我们现在需要的这个前缀树，不需要什么end，pass了，该前缀树只需要提供两个方法：给你一个数的二进制形式，你把它
    // 加入到树中；给你一个数，你在树中找到能和它异或后结果最大的值返回出来。
    public static class Node {
        public Node[] nexts = new Node[2];  // 为什么长度为2？因为它的后继只可能是0或1，刚好用索引0表示0
    }

    public static class PrefixTree {

        public Node root = new Node();

        // 将一个数的二进制形式加入到树中
        public void add(int num) {
            Node cur = root;
            for (int move = 31; move >= 0; move--) { // int型数据固定为32位，所以要把32位上的每位上的值记录下来
                int path = (num >> move) & 1;   // 拿到每位上的值，这个值也刚好反映了应该去往哪里
                if (cur.nexts[path] == null)
                    cur.nexts[path] = new Node();
                cur = cur.nexts[path];
            }
        }
        // 所以该树上每个记录的长度都固定为32,那么该方法的时间复杂度就是：O(1)

        // 该结构之前收集了一票数字，并且建好了前缀树
        // num和树上的谁 ^ 有最大的结果（把结果返回）
        public int maxXor(int num) {
            Node cur = root;
            int res = 0;
            for (int move = 31; move >= 0; move--) { // 取出num中第move位的状态，path只有两种值0就1，整数
                int path = (num >> move) & 1;
                // expected表示当前位的值希望遇到的值：如果是符号位，则希望遇到和当前值path相同的；
                // 如果不是符号位，则希望遇到和当前位上的值path异或能得到1的值。
                int expected = move == 31 ? path : (path ^ 1);
                // 实际遇到的值。如若想去的地方不存在，那只能expected ^ 1，就又变回了path本身
                expected = cur.nexts[expected] == null ? (expected ^ 1) : expected;
                // expected ^ path 就得到了第move位真实的值，现在要把该位置上的值添加到res上
                res |= (expected ^ path) << move;
                cur = cur.nexts[expected];
            }
            return res;
        } // 该方法的for循环固定32次，所以时间复杂度为：O(1)

    }
    // ==================================================================================================



    // 暴力方法：O(N^2)
    public static int maxSumXorV2(int[] arr) {
        if (arr == null || arr.length == 0)
            return Integer.MIN_VALUE;
        int res = Integer.MIN_VALUE;
        int N = arr.length;
        int[] help = new int[N];
        help[0] = arr[0];
        for (int i = 1; i < N; i++)
            help[i] = arr[i] ^ help[i - 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= i; j++)
                res = Math.max(res, help[i] ^ (j == i ? 0 : help[j]));
        }
        return res;
    }

    // for test
    public static void test(int testTime, int maxSize, int maxVal) {
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.RandomArr(maxSize, maxVal);
            int res1 = maxSumXor(arr);
            int res2 = maxSumXorV2(arr);
            if (res1 != res2) {
                System.out.println("Failed");
                arrays.printArray(arr);
                System.out.println("前缀树方法：" + res1);
                System.out.println("暴力方法：" + res2);
                return;
            }
        }
        System.out.println("AC");
    }

    public static void main(String[] args) {
        test(10000, 50, 100);
    }

}
