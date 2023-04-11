package GreatOffer.TopInterviewQ;

import java.util.HashSet;

// 编写一个算法来判断一个数 n 是不是快乐数。
// 「快乐数」 定义为：
//   对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
//   然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
//   如果这个过程 结果为 1，那么这个数就是快乐数。
// 如果 n 是 快乐数 就返回 true ；不是，则返回 false 。

public class _0202_HappyNumber {

    public boolean isHappy(int n) {
        HashSet<Integer> set = new HashSet<>();
        while (n != 1) {
            int sum = 0;
            while (n != 0) {
                sum += (n % 10) * (n % 10);
                n /= 10;
            }
            n = sum;
            if (set.contains(sum))
                break;
            set.add(sum);
        }
        return n == 1;
    }
    // 上面的方法是暴力解法，但是这道题目涉及到的数学理论是很重要的，现在就给出其最优解。
    // 数学上证明了：如果该数在上述过程中碰到了4，那么该数就一定不是快乐数.


    public boolean isHappy2(int n) {
        while (n != 1 && n != 4) {
            int sum = 0;
            while (n != 0) {
                sum += (n % 10) * (n % 10);
                n /= 10;
            }
            n = sum;
        }
        return n == 1;
    }
}
