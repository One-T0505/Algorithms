package Basic.UnionFind;

import java.util.HashMap;


// leetCode952
// 给定一个由不同正整数的组成的非空数组 nums ，考虑下面的图：
//  1.有 nums.length 个节点，按从 nums[0] 到 nums[nums.length - 1] 标记；
//  2.只有当 nums[i] 和 nums[j] 共用一个大于 1 的公因数时，nums[i] 和 nums[j]之间才有一条边。
// 返回图中最大连通组件的大小 。

// 1 <= nums.length <= 2 * 10^4
// 1 <= nums[i] <= 10^5
// nums 中所有值都不同

public class CommonFactor {

    // 读完题目之后感觉很像连通性问题，很像之前做过的那个朋友圈问题，所以很自然联想到并查集。
    // 先想下暴力解法：套两层循环寻找类数，这样的时间复杂度为O(N^2)，根据数据规模，这个方法肯定过不了
    // 优化的解法：我们需要构造一个因子表，key是因子，value是元素在数组的索引。
    // 遍历每个元素，算出该元素除了1以外的所有因子，如果某个因子已经存在于因子表中了，那就可以和对应位置的元素合并
    // 成一个类。最后返回因子表中记录条数即可。
    // 时间复杂度：遍历数组需要O(N)，对于每个元素需要算出所有因子，所以需要O(V)，V是元素值的大小
    // 所以大总的时间复杂度就是：O(N*sqrt(V))
    // 根据数据规模合理选择方法，如果数组长度比较小，但是数值很大，可以选择暴力方法


    // 求两个正整数的最大公约数：gcd方法，必须背住. 用的是辗转相除法 O(1)
    // 这个方法虽然没在本题中用到，但是是个很重要的方法
    public static int gcd(int a, int b) {  // 要求a、b都必须树正整数
        return b == 0 ? a : gcd(b, a % b);
    }

    public int largestComponentSize(int[] nums) {
        if (nums == null || nums.length < 2)
            return 0;
        int N = nums.length;
        UnionFind unionFind = new UnionFind(N);
        // 因子表
        HashMap<Integer, Integer> factors = new HashMap<>();
        // 遍历数组
        for (int i = 0; i < N; i++) {
            // 算出界限，然后枚举元素的所有非1因数
            int limit = (int) Math.sqrt(nums[i]);
            for (int j = 1; j <= limit; j++) {
                if (nums[i] % j == 0) {
                    int other = nums[i] / j;
                    if (!factors.containsKey(other))
                        factors.put(other, i);
                    else
                        unionFind.union(factors.get(other), i);
                    if (j != 1) {
                        if (!factors.containsKey(j))
                            factors.put(j, i);
                        else
                            unionFind.union(factors.get(j), i);
                    }
                }
            }
        }
        return unionFind.maxSize();
    }

    public static class UnionFind {
        private int[] parents;
        private int[] sizes;
        private int[] help;

        public UnionFind(int N) {
            parents = new int[N];
            sizes = new int[N];
            help = new int[N];
            for (int i = 0; i < N; i++) {
                parents[i] = i;
                sizes[i] = 1;
            }
        }


        public int maxSize() {
            int res = 0;
            for (int elem : sizes)
                res = Math.max(res, elem);
            return res;
        }

        public void union(int i, int j) {
            int r1 = findRoot(i);
            int r2 = findRoot(j);
            if (r1 != r2) {
                int greater = sizes[r1] >= sizes[r2] ? r1 : r2;
                int less = greater == r1 ? r2 : r1;
                parents[less] = greater;
                sizes[greater] += sizes[less];
            }
        }

        private int findRoot(int pos) {
            int index = 0;
            while (parents[pos] != pos) {
                help[index++] = pos;
                pos = parents[pos];
            }
            while (index > 0) {
                parents[help[--index]] = pos;
            }
            return pos;
        }
    }
}
