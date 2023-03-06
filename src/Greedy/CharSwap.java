package Greedy;

// 一个数组中只有两种字符'G'和'B'，想让所有的G都放在一侧，所有的B都放在另一侧，但是只能在相邻字符之间进行交换操作。
// 返回至少需要交换几次。

public class CharSwap {
    // 这个题目就有两种情况了：1>G在左侧  2>G在右侧
    // 这个题目返回的是交换次数，所以未必需要真的实现位置交换。还有，我们只需要把所有G都逐一放在开头，那么剩下的B自然就是都在
    // 右侧了。同理，将B逐一放在开头，那么G就自然地在另一侧了。时间复杂度：O(N)

    // 这里又用到了一个贪心：字符串中第一个出现的G放在0位，第二个出现的G放在1位，这样放是最省的。不是说第二个G放在0位

    public static int swapTime(char[] chars){
        if (chars == null || chars.length <= 1)
            return 0;
        int L = 0;  // G应该填放在数组的位置
        int res1 = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 'G'){
                res1 += i - L;  // 从i到L需要冒泡i-L次
                L++;
            }
        }
        L = 0;
        int res2 = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 'B'){
                res2 += i - L;  // 从i到L需要冒泡i-L次
                L++;
            }
        }
        return Math.min(res1, res2);
    }
}
