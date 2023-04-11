package GreatOffer.class19;

import java.util.LinkedList;

// 一张扑克有3个属性，每种属性有3种值(A、 B、C)
// 比如"'AAA"，第一个属性值A，第二个属性值A， 第三个属性值A
// 比如"BCA"，第一个属性值B，第二个属性值C，第三个属性值A
// 给定一个字符串类型的数组cards[]，每一个字符串代表一张扑克，从中挑选三张扑克，一个属性达标的条件是：
// 这个属性在三张扑克中全一样，或全不一样
// 挑选的三张扑克达标的要求是:每种属性都满足上面的条件
// 比如: "ABC"、"CBC"、 "BBC"
// 第一张第一个属性为"A"、第二张第一个属性为"C"、第三张第- -个属性为"B"，全不一样
// 第一张第二个属性为"B"、第二张第二个属性为"B"、第三张第二个属性为"B"，全一样
// 第一张第三个属性为"C"、第二张第三个属性为"C"、第三张第三个属性为"C"，全一样
// 每种属性都满足在三张扑克中全一样，或全不一样，所以这三张扑克达标
// 返回在cards[]中任意挑选三张扑克，达标的方法数. cards的数量级可以达到百万级

public class PokerStatus {

    // 因为牌的数量很大，可以达到百万级，所以从每张牌入手不现实。发现属性种类只有3种，并且每张牌只有3个属性，
    // 所以挑选三张牌最多只有27种牌面，数量很少，我们应该从不同的牌面入手。我们可以用三进制来给AAA～CCC编号

    public static int getWays(String[] cards) {
        int[] counter = new int[27];  // 统计27种牌面牌的数量
        for (String s : cards) {
            char[] chs = s.toCharArray();
            counter[(chs[0] - 'A') * 9 + (chs[1] - 'A') * 3 + (chs[2] - 'A')]++;
        }
        int ways = 0;
        // 先算拿三张一样牌面的牌的结果
        for (int status = 0; status < 27; status++) {
            int n = counter[status];
            if (n > 2)  // C(n, 3) 排列组合
                ways += n == 3 ? 1 : (n * (n - 1) * (n - 2) / 6);
        }
        // 接下来计算选择不同牌面的结果。首先要明确如果选择了两张一样牌面的牌，比如两张ABC，为了符合题目要求
        // 那么第三张牌也只能是ABC；所以说，三张牌的选择要么是三张一摸一样的，要么是三张完全不同的牌面，不可能是
        // 两张一样，一张不同的。
        // 下面这个for循环就是算三张牌面不同的结果
        LinkedList<Integer> path = new LinkedList<>();
        // 还有一点值得注意，如果拿的是：ABC、CCC、BBA；那么这三张牌的拿牌顺序就不重要；为了不重复计算，我们规定
        // 按顺序从小到大开始拿，如果第一张牌确定了，那么剩下两张牌只能比之前的大
        for (int i = 0; i < 27; i++) {
            if (counter[i] != 0) {
                path.addLast(i);
                ways += f(counter, i, path);
                path.pollLast();
            }
        }
        return ways;
    }

    // pre表示上一次拿的牌面，所以当前如果还可以选择牌的话，只能从pre+1开始选择
    private static int f(int[] counter, int pre, LinkedList<Integer> path) {
        if (path.size() == 3)
            return resolve(counter, path);
        int ways = 0;
        for (int next = pre + 1; next < 27; next++) {
            if (counter[next] != 0) {
                path.addLast(next);
                ways += f(counter, next, path);
                path.pollLast();
            }
        }
        return ways;
    }


    // 现在三种牌面已经选择好了，都存放在path中；现在要看是否满足属性达标和牌面达标
    private static int resolve(int[] counter, LinkedList<Integer> path) {
        int p1 = path.get(0);
        int p2 = path.get(1);
        int p3 = path.get(2);
        for (int i = 9; i > 0; i /= 3) {
            int s1 = p1 / i;
            int s2 = p2 / i;
            int s3 = p3 / i;
            p1 %= i;
            p2 %= i;
            p3 %= i;
            // 要么都不相同，要么都相同
            if ((s1 == s2 && s2 == s3) || (s1 != s2 && s2 != s3 && s1 != s3))
                continue;
            return 0;
        }
        p1 = path.get(0);
        p2 = path.get(1);
        p3 = path.get(2);
        return counter[p1] * counter[p2] * counter[p3];
    }
}
