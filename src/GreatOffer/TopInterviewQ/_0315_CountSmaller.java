package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.List;

// 给你一个整数数组 nums，按要求返回一个新数组 counts 。数组 counts 有该性质：
// counts[i] 的值是  nums[i] 右侧小于 nums[i] 的元素的数量。

public class _0315_CountSmaller {

    // 本题用到的方法是之前讲过的：归并排序求逆序对问题

    // 主方法
    public List<Integer> countSmaller(int[] nums) {
        ArrayList<Integer> res = new ArrayList<>();
        if (nums == null)
            return res;
        int N = nums.length;
        for (int i = 0; i < N; i++)
            res.add(0);
        if (N < 2)
            return res;
        Node[] nodes = new Node[N];
        for (int i = 0; i < N; i++)
            nodes[i] = new Node(nums[i], i);

        f(nodes, 0, N - 1, res);
        return res;
    }

    private void f(Node[] arr, int L, int R, ArrayList<Integer> res) {
        if (L == R)
            return;
        int mid = L + ((R - L) >> 1);
        f(arr, L, mid, res);
        f(arr, mid + 1, R, res);
        merge(arr, L, mid, R, res);
    }

    private void merge(Node[] arr, int L, int mid, int R, ArrayList<Integer> res) {
        Node[] help = new Node[R - L + 1];
        int i = help.length - 1;
        int p1 = mid, p2 = R;
        while (p1 >= L && p2 > mid) {
            if (arr[p1].val > arr[p2].val)
                res.set(arr[p1].index, res.get(arr[p1].index) + p2 - mid);
            help[i--] = arr[p1].val > arr[p2].val ? arr[p1--] : arr[p2--];
        }
        while (p1 >= L)
            help[i--] = arr[p1--];
        while (p2 > mid)
            help[i--] = arr[p2--];

        System.arraycopy(help, 0, arr, L, help.length);
    }

    public static class Node {
        public int val;
        public int index;

        public Node(int val, int index) {
            this.val = val;
            this.index = index;
        }
    }
}
