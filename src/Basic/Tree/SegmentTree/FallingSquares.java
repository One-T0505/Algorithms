package Basic.Tree.SegmentTree;

// leetCode699 俄罗斯方块问题
// 在二维平面上的 x 轴上，放置着一些方块。给你一个二维整数数组 positions ，
// 其中 positions[i] = [lefti, sideLengthi] 表示：第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上
// 坐标点 lefti 对齐。每个方块都从一个比目前所有的落地方块更高的高度掉落而下。方块沿 y 轴负方向下落，直到着陆
// 到另一个正方形的顶边或者是 x 轴上 。一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。一旦着陆，它就会固
// 定在原地，无法移动。在每个方块掉落后，你必须记录目前所有已经落稳的方块堆叠的最高高度。
// 返回一个整数数组 ans ，其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class FallingSquares {

    // 这是一道需要用到线段树优化的题。原始的线段树我们明确知道整个数组的范围，即数组的长度我们是知道的。但是这个题
    // 的困难点在于我们并不知道范围是在哪？即我们并不知道在那个范围上操作，没有边界可言，这就导致让我们在初始化时不知道
    // 要生成多大的数组空间。如果按照题目说的给出的方块的左边界范围是[1, 10^8] 边长是 [1,10^6]，那也就是说
    // 我们的范围上限是 10^8+10^6   生成一个这样长的数组是不可能的，所以我们需要使用离散化来处理。将较大的范围空间
    // 压缩至较小的范围空间。

    public static List<Integer> fallingSquares(int[][] positions) {
        HashMap<Integer, Integer> map = discrete(positions);
        int N = map.size();
        SegmentTree st = new SegmentTree(N);
        int max = 0;
        ArrayList<Integer> res = new ArrayList<>();
        for (int[] pos : positions){
            int L = map.get(pos[0]);
            int R = map.get(pos[0] + pos[1] - 1);
            int height = st.query(L, R) + pos[1];
            max = Math.max(max, height);
            res.add(max);
            st.update(L, R, height);
        }
        return res;
    }




    // 线段树结构   这道题目只能使用update的线段树，不能使用累加的线段树。比如：
    // 有一方块落在[2,8],边长为6，那么[2,8]这个区域的高度就是6。下一个木块是[6, 9],边长是3，
    // 如果用累加，那就会在[6,9]范围上累加，[6,8]就会变成9，而[8,9]只有3，实际上，应该让【6，9]都变成9的
    public static class SegmentTree {
        public int N;
        public int[] max;
        public int[] lazy;
        public boolean[] effect;

        public SegmentTree(int n) {
            N = n + 1;
            max = new int[N << 2];
            lazy = new int[N << 2];
            effect = new boolean[N << 2];
        }


        public void update(int L, int R, int C){
            update(L, R, C, 1, N, 1);
        }


        private void update(int L, int R, int C, int l, int r, int rt){
            if (L <= l && R >= r){
                effect[rt] = true;
                lazy[rt] = C;
                max[rt] = C;
                return;
            }
            distribute(rt);
            int mid = l + ((r - l) >> 1);
            if (L <= mid)
                update(L, R, C, l, mid, rt << 1);
            if (R > mid)
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        private void distribute(int rt) {
            if (effect[rt]){
                lazy[rt << 1] = lazy[rt];
                max[rt << 1] = max[rt];
                effect[rt << 1] = true;
                lazy[rt << 1 | 1] = lazy[rt];
                max[rt << 1 | 1] = max[rt];
                effect[rt << 1 | 1] = true;
                effect[rt] = false;
            }
        }



        public int query(int L, int R){
            return query(L, R, 1, N, 1);
        }


        private int query(int L, int R, int l, int r, int rt){
            if (L <= l && R >= r) {
                return max[rt];
            }
            int mid = l + ((r - l) >> 1);
            distribute(rt);
            int ans = Integer.MIN_VALUE;
            if (L <= mid) {
                ans = Math.max(ans, query(L, R, l, mid, rt << 1));
            }
            if (R > mid) {
                ans = Math.max(ans, query(L, R, mid + 1, r, rt << 1 | 1));
            }
            return ans;
        }
    }
    // ==============================================================================================




    // 离散化处理
    public static HashMap<Integer, Integer> discrete(int[][] pos){
        TreeSet<Integer> set = new TreeSet<>();
        for (int[] arr : pos){
            set.add(arr[0]);
            set.add(arr[0] + arr[1] - 1);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        int count = 0;
        for (int i : set)
            map.put(i, ++count);
        return map;
    }


    public static void main(String[] args) {
    }
}
