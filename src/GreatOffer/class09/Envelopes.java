package class09;

// leetCode354
// 这个就是俄罗斯套娃的模型，其原理是来自于 最长递增子序列 这个基本模型，所以先去看 IncrementalSubSeq

import java.util.Arrays;

public class Envelopes {

    // 首先先将每个小数组排序：宽度从小到大排序，宽度相等的按照高度从大到小排序。拍完序后，将每个数组的高度那维
    // 数据单独拿出来成立一个数组，在该数组上完成 最长递增子序列 的寻找就是该题目的答案。假如单独将高度数据拿出来
    // 组成新的数组为：[5, 7, 3, 2, 6, 1, 4, 7]
    // 可以确切地说，7的宽度一定比5大，因为7排在5的后面，但是高度却比5大，如果宽度和5一样的话，那必然是7在5的前面，
    // 后面只要不是递减了，就说明是碰到了一个新的更大的宽度。那也就是说，当前数cur的后面比cur大的，不仅仅是高度比cur大，
    // 也说明宽度比cur大，所以该数必然能包住cur。

    public int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a, b) -> (a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]));
        int N = envelopes.length;
        int[] end = new int[N];
        int R = 0;
        int res = 1;
        end[0] = envelopes[0][1];
        for (int i = 1; i < N; i++) {
            int cur = envelopes[i][1];
            int pos = mostLeftGAE(end, 0, R, cur);
            if (pos == -1) {
                end[++R] = cur;
                res = Math.max(res, R + 1);
            } else
                end[pos] = cur;
        }
        return res;
    }

    private int mostLeftGAE(int[] end, int L, int R, int t) {
        int pos = -1;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (end[mid] >= t) {
                pos = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return pos;
    }
}
