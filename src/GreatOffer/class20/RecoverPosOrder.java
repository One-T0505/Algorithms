package GreatOffer.class20;

import java.util.HashMap;

// 如果只给定一个二叉树前序遍历数组pre和中序遍历数组in,能否不重建树，而直接生成这个二叉树的后序数组并返回
// 已知二叉树中没有重复值

public class RecoverPosOrder {

    // 已知先序和中序，完成后序。其实这个方法和我们手动完成该算法时一样。pre的第一个数必然是根，要填到pos的最后一个
    // 然后将根元素在in中找到，将其分成两侧，左边的就是左子树，右边的就是右子树

    // 该方法的作用：已知pre和in
    // 现在只关注pre[L1..R1]和in[L2..R2]，并填写到pos[L3..R3]范围上
    public static void f(int[] pre, int L1, int R1,
                         int[] in, int L2, int R2,
                         int[] pos, int L3, int R3) {
        // 这个case很关键，这个case是为了避免：在pre中的一个元素在in中匹配时，发现其是in中左右两端的情况，
        // 如果是左右两端的端点，那就说明只有左子树或右子树，根据下面调用递归时的索引转换会出现L1>R1的情况
        if (L1 > R1)
            return;
        if (L1 == R1) {
            pos[L3] = pre[L1];
        } else {
            pos[R3] = pre[L1];
            int loc = L2;
            // 在中序数组in中定位
            for (; loc < R2; loc++) {
                if (in[loc] == pre[L1])
                    break;
            }
            int left = loc - L2;
            // 填左子树
            f(pre, L1 + 1, L1 + left, in, L2, loc - 1, pos, L3, L3 + left - 1);
            // 填右子树
            f(pre, L1 + left + 1, R1, in, loc + 1, R2, pos, L3 + left, R3 - 1);
        }
    }
    // 该方法中有个枚举行为，就是在in数组中定位pre中的元素。如果事先将in数组每个元素的位置存放在哈希表中，就可以省去
    // 枚举行为
    // ===============================================================================================


    public static int[] solution(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length)
            return null;
        int N = pre.length;
        int[] pos = new int[N];
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < N; i++)
            map.put(in[i], i);
        g(pre, 0, N - 1, in, 0, N - 1, pos, 0, N - 1, map);
        return pos;
    }


    public static void g(int[] pre, int L1, int R1,
                         int[] in, int L2, int R2,
                         int[] pos, int L3, int R3, HashMap<Integer, Integer> map) {
        // 这个case很关键，这个case是为了避免：在pre中的一个元素在in中匹配时，发现其是in中左右两端的情况，
        // 如果是左右两端的端点，那就说明只有左子树或右子树，根据下面调用递归时的索引转换会出现L1>R1的情况
        if (L1 > R1)
            return;
        if (L1 == R1) {
            pos[L3] = pre[L1];
        } else {
            pos[R3] = pre[L1];
            // 在中序数组in中定位
            int loc = map.get(pre[L1]);
            int left = loc - L2;
            // 填左子树
            g(pre, L1 + 1, L1 + left, in, L2, loc - 1, pos, L3, L3 + left - 1, map);
            // 填右子树
            g(pre, L1 + left + 1, R1, in, loc + 1, R2, pos, L3 + left, R3 - 1, map);
        }
    }


    public static void main(String[] args) {
        int[] pre = {5, 4};
        int[] in = {4, 5};
        int[] pos = solution(pre, in);
        for (int e : pos)
            System.out.print(e + " ");
        System.out.println();
    }
}
