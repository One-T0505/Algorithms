package Basic.DynamicProgramming.StatusCompressed;

// 在 "100 game" 这个游戏中，两名玩家轮流选择从 1 到 10 的任意整数，累计整数和，先使得累计整数和达到或超过 100
// 的玩家，即为胜者。如果我们将游戏规则改为 “玩家不能重复使用整数” 呢？
// 例如，两个玩家可以轮流从公共整数池中抽取从 1 到 15 的整数（不放回），直到累计整数和 >= 100。
// 给定两个整数 maxChoosableInteger （整数池中可选择的最大数）和 desiredTotal（累计和），若先出手的玩家
// 能稳赢则返回 true ，否则返回 false 。假设两位玩家游戏时都表现最佳。

// 1 <= maxChoosableInteger <= 20
// 0 <= desiredTotal <= 300

public class _0464_Code {

    // 最暴力的尝试
    public boolean canIWin(int choose, int target) {
        // 这两个基本的 base case 都是题目设定，没什么意义
        if (target == 0)
            return true;
        if (choose * (choose + 1) < (target << 1))
            return false;
        // 因为题目数据规模说了choose <=20，所以可以用int的二进制形式标记1～choose的数字哪些被选过了
        // 如果status从低到高的第i位是1，表示数字 i+1 已经被选过了。只有是0才表示可以选择
        int status = 0;
        return f(choose, status, target);
    }

    private boolean f(int choose, int status, int rest) {
        if (rest <= 0) // 谁先碰到这种局面，就说明另一个人刚好凑到了>=target的累加和，所以谁先遇到这种局面谁就输
            return false;
        // 尝试status中每一位上为0的选择。但是只有从第0～choose-1位才是有效范围
        for (int i = 0; i < choose; i++) {
            if (((1 << i) & status) == 0){ // 第i位可以选择
                // 然后就把第i位设置为1，表示不可选，接着就近下一步递归了。
                // 1.这里为什么是 ! ?  因为进入递归时，此时的先手就变成了递归中的后手了。而我们f函数的返回结果
                //   表示在(status, rest)这种局面下，先手的结果。而
                //   2⃣️ f(choose, status | (1 << i), rest - i - 1) 的结果是这个递归中的先手，也就是当前递归
                //   的后手。所以只有在2⃣️中返回false，表示2⃣️中的先手是输了，也就是当前递归的先手是赢的

                // 2.这里是不是还有一个疑惑，就是为什么为什么没有还原现场呢，这不是深度遍历吗？
                //   这里有个很巧妙的细节，就是我们并没有实际地去修改status的值，而是直接将新的status传入递归，
                //   也就是说从递归回到当前函数里时，status的值和进入下面的递归之前都是一样的，没有变过。
                if (!f(choose, status | (1 << i), rest - i - 1))
                    return true;
            }
        }
        // 尝试了所有选择，都没办法赢，那就是输
        return false;
    }




    // 上面的方法思路已经对了，但是在leetCode中超时了，所以还得继续优化。看是否能用记忆化搜索来优化。
    // 记忆化搜索的前提是有重复计算，优化才有意义。假如choose==10，target==14  如果我先拿了2，再拿走5，
    // 那么局面就变成了rest==7 status上2和5的位置变成1；如果我先拿走5再拿走2，status也是一样，rest还是7，也就是说
    // 的确是存在重复计算的，所以有必要优化。来分析下有几个可变参数：貌似只有status和rest两个可变参数。
    // 真的是这样吗？实际上只有一个可变参数status，因为status完全可以决定rest。如果target==13
    // status==00..0010110   说明1、2、4拿走了，那么rest自然就是6。但是反过来rest不能决定status的值，如果rest==6
    // 那么你怎么知道拿的是2，5  还是3，4 还是1，2，4

    public boolean canIWin2(int choose, int target) {
        // 这两个基本的 base case 都是题目设定，没什么意义
        if (target == 0)
            return true;
        if (choose * (choose + 1) < (target << 1))
            return false;
        // dp[status]==1，表示先手赢， -1表示后手赢，0表示没算过
        int[] dp = new int[1 << choose];
        return g(choose, 0, target, dp);
    }


    private boolean g(int choose, int status, int rest, int[] dp) {
        if (dp[status] != 0)
            return dp[status] == 1;
        // 谁先碰到这种局面，就说明另一个人刚好凑到了>=target的累加和，所以谁先遇到这种局面谁就输
        boolean res = false;
        if (rest > 0){
            // 尝试status中每一位上为0的选择。但是只有从第0～choose-1位才是有效范围
            for (int i = 0; i < choose; i++) {
                if (((1 << i) & status) == 0){ // 第i位可以选择
                    if (!g(choose, status | (1 << i), rest - i - 1, dp)){
                        res = true;
                        break;
                    }
                }
            }
        }
        // 尝试了所有选择，都没办法赢，那就是输
        dp[status] = res ? 1 : -1;
        return res;
    }
}
