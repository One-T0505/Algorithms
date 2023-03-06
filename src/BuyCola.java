

// 这道题不涉及什么数据结构和算法相关的知识，纯考coding能力

// 贩卖机只支持硬币支付，且收退都只支持10 ，50，100三种面额
// 一次购买只能出一瓶可乐，且投钱和找零都遵循优先使用大钱的原则
// 需要购买的可乐数量是m，其中手头拥有的10、50、100的数量分别为a、b、c，可乐的价格是x(x是10的倍数)
// 请计算出需要投入硬币次数

public class BuyCola {


    // 正式方法：m是要买的可乐数量；a是面值100的货币数量；b是面值50的货币数量；c是面值10的货币数量；x是可乐的单价
    public static int buyCola(int m, int a, int b, int c, int x){
        int[] faces = {100, 50, 10};  // 面额数组
        int[] nums = {a, b, c};       // 不同面额对应的数量的数组
        int res = 0;        // 要投放的次数
        int preRestVal = 0;    // 之前剩下的钱没法单独购买可乐
        int preRestNum = 0;    // 之前剩下的钱的张数
        for (int i = 0; i < 3; i++) {
            // 有一个技巧：如果想求 a / b向上取整，就可以这样：(a + (b - 1)) / b. 该表达式符合任意情况
            // 这里为什么要算每种货币第一次购买时的情况？因为当之前那种货币无法单独购买可乐时，就得用下一种货币，
            // 而之前又可能剩下一些留下的货币，所以要凑在一起用。比如：可乐单价250，100面值的货币有5张；第一次购买时
            // 可以直接用3张100的购买，第二次购买时，剩下两张无法购买可乐了，所以这两张就构成了preRest。
            int curFaceFirstBuy = (x - preRestVal + faces[i] - 1) / faces[i];
            // 如果当前货币的数量无法支持第一次购买，那就一起加进preRest，让下一种货币来处理
            if (nums[i] < curFaceFirstBuy){
                preRestVal += nums[i] * faces[i];
                preRestNum += nums[i];
                continue;
            } else {
                exchange(faces, nums, preRestVal + faces[i] * curFaceFirstBuy - x,
                        i + 1, 1);
                res += preRestNum + curFaceFirstBuy;
                nums[i] -= curFaceFirstBuy;
                m--;
            }
            // 上面处理的都是第一次使用某张面值开始购买可乐时的情况，因为会存在之前剩余的情况；现在是如何全部使用
            // 当前面值购买可乐
            // curFaceRestBuy 表示：只用当前面值货币购买一瓶可乐需要几张
            int curFaceRestBuy = (x + faces[i] - 1) / faces[i];
            // 只用当前面额的货币能搞定几瓶可乐. 这里有一个边界：我能搞定的可乐数量  我需要购买的可乐 取最小值，
            // 如果我能搞定的数量超过我要购买的就无意义了
            int times = Math.min(nums[i] / curFaceRestBuy, m);
            int balance = curFaceRestBuy * faces[i] - x;  // 用这样的方式购买一瓶会找零多少
            exchange(faces, nums, balance, i + 1, times);
            m -= times;
            res += curFaceRestBuy * times;
            nums[i] -= curFaceRestBuy * times;
            preRestVal = nums[i] * faces[i];
            preRestNum = nums[i];
        }
        // 最后还要看m是否为0，有可能用光所有钱都达不到要求
        return m == 0 ? res : -1;
    }

    // exchange是如何交易的方法：balance表示机器要返还的钱；start表示从哪种货币开始选择，因为退钱也是尽可能
    // 选面值大的。比如可乐单价230，假设面值100的货币有20张，每次购买可以用3张，找零一张50，2张10；这样的购买方式
    // 可以持续6次，所以times传入6，因为这6次购买的找零方式都是一样的，可以一并计算。
    private static void exchange(int[] faces, int[] nums, int balance, int start, int times) {
        while (balance != 0){
            nums[start] += (balance / faces[start]) * times;
            balance %= faces[start];
            start++;
        }
    }
}
