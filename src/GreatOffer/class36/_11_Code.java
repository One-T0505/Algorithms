package GreatOffer.class36;


// 来自哈啰单车
// 这道题是leetCode原题，1510题
// Alice 和 Bob 两个人轮流玩一个游戏，Alice 先手。
// 一开始，有 n个石子堆在一起。每个人轮流操作，正在操作的玩家可以从石子堆里拿走任意非零平方数个石子。
// 如果石子堆里没有石子了，则无法操作的玩家输掉游戏。
// 给你正整数n，且已知两个人都采取最优策略。如果 Alice 会赢得比赛，那么返回True，否则返回False。

public class _11_Code {

    // 这是之前讲的那个牛羊吃草问题原型
    // 该方法返回当前的先手会不会赢，默认先手是Alice
    public boolean winnerSquareGame(int n) {
        if (n == 0)
            return false;
        for (int i = 1; i * i <= n; i++) {
            if (!winnerSquareGame(n - i * i))
                return true;
        }
        return false;
    }


    // 改写成动态规划
    public boolean winnerSquareGame2(int n) {
        boolean[] dp = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                if (!dp[i - j * j]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}
