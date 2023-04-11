package GreatOffer.class36;

import java.util.HashMap;

// 来自美团
// 有一棵树，给定头节点h, 和结构数组m， 下标0弃而不用
// 比如h=1,m= {[]，[2,3]，[4]，[5,6],[]，[]，[]}
// 表示1的孩子是2、3; 2的孩子是4; 3的孩子是5、6; 4、5和6是叶节点，都不再有孩子
// 每一个节点都有颜色，记录在c数组里，比如c[i] = 4,表示节点i的颜色为4
// 一开始只有叶节点是有权值的，记录在w数组里，
// 比如，如果一开始就有w[i] = 3，表示节点i是叶节点、且权值是3
// 现在规定非叶节点i的权值计算方式:
//  根据i的所有直接孩子来计算，假设i的所有直接孩子,颜色只有a,b,k
//  w[i]=Max{
//            (颜色为a的所有孩子个数+颜色为a的孩子权值之和)，
//            (颜色为b的所有孩子个数+颜色为b的孩子权值之和)，
//            (颜色为k的所有孩子个数+颜色k的孩子权值之和)
//          }
// 请计算所有孩子的权值并返回

public class _06_Code {

    // 可以发现，这道题目必须从下往上算权重，最后算根结点的权重
    // f函数的作用，传入一个结点h，f可以根据h的所有孩子，正确算出h的权重
    public static void f(int h, int[][] m, int[] w, int[] c) {
        if (m[h].length == 0) // 如果h没有孩子
            return;
        // 统计h所有孩子中每种颜色的孩子对应的数量
        HashMap<Integer, Integer> colors = new HashMap<>();
        // 统计h所有孩子中每种颜色的孩子对应的权重和
        HashMap<Integer, Integer> weights = new HashMap<>();
        for (int child : m[h]) {
            f(child, m, w, c); // 先把所有孩子的权重算出来
            colors.put(c[child], colors.getOrDefault(c[child], 0) + 1);
            weights.put(c[child], weights.getOrDefault(c[child], 0) + w[child]);
        }
        for (int color : colors.keySet())
            w[h] = Math.max(w[h], colors.get(color) + weights.get(color));
    }
}
