package DynamicProgramming;

public class CoffeeCup {
    // 给定一-个数组，代表每个人喝完咖啡准备刷杯子的时间点，只有一台咖啡机，一次只能洗一个杯子， 时间耗费a，洗完才能洗下一杯
    // 每个咖啡杯也可以自己挥发干净，时间耗费b，咖啡杯可以并行挥发。返回让所有咖啡杯变干净的最早完成时间。每个员工
    // 只有一个选择，要么喝完就放在咖啡机里等待咖啡机来洗杯子，并且等待的时间不可以用于挥发；一开始选择挥发的杯子不可以
    // 挥发了一会之后再放到洗杯子的机器里排队
    // 三个参数: int[] arr、 int a、 int b

    // 方法1：暴力递归
    // wash 洗一个杯子需要的时间       固定变量
    // volatilize 杯子挥发干净的时间   固定变量
    // drinks 每--个员工喝完的时间     固定变量
    // drinks[0. .index-1] 都已经干净了，不用你操心了；
    // drinks[index...]都想变干净，这是我操心的，access 表示机器何时可用
    // 方法返回 drinks[index...]变干净，最少的时间点
    // 所以主函数应该这么调用：process(drinks, 3，10，0, 0)  假设洗杯子3 挥发10
    public static int process(int[] drinks, int volatilize, int wash, int index, int access){
        // 只剩最后一个杯子还没决定好
        if (index == drinks.length - 1)
            // Math.min(选择用机器洗的时间， 自然挥发的时间)
            // Math.max(drinks[index], access) 表示机器可用的时间点和喝完咖啡的时间谁后结束才有用
            return Math.min(Math.max(drinks[index], access) + wash,
                    drinks[index] + volatilize);

        // 还有多个杯子没决定好

        // 选择1：让当前杯子用机器洗
        int washTime = Math.max(access, drinks[index]) + wash; // washTime 表示仅洗完index这个杯子时的时间点
        // end1表示在决定index的杯子用机器洗后，弄干净剩余所有杯子的时间点
        int end1 = process(drinks, volatilize, wash, index + 1, washTime);
        int choice1 = Math.max(washTime, end1);  // 这两个都完成了才是选择1最终结束的时间

        // 选择2：让当前杯子自由挥发
        int volatilizeTime = drinks[index] + volatilize;
        // 此时access还是原来的值，因为挥发不会影响机器可用的时间点
        int end2 = process(drinks, volatilize, wash, index + 1, access);
        int choice2 = Math.max(volatilizeTime, end2);

        return Math.min(choice1, choice2);
    }

    // 可以发现上面的暴力递归过程可变参数只有两个，index，access。但是这题又不同之前的题可以很明确的
    // 知道两个可变参数的范围。所以此时要根据具体业务要求来确定可变参数的范围。
    // access最大为全部杯子都等着用机器洗的时间
    public static int dp(int[] drinks, int volatilize, int wash){
        int N = drinks.length;
        if (wash >= volatilize) // 那就全部挥发
            return drinks[N - 1] + volatilize;
        int limit = 0;
        for (int i = 0; i < N; i++)
            limit = Math.max(limit, drinks[i]) + wash;

        int[][] dp = new int[N][limit + 1];

        for (int access = 0; access <= limit; access++)
            dp[N - 1][access] = Math.min(Math.max(drinks[N - 1], access) + wash, drinks[N - 1] + volatilize);

        for (int index = N - 2; index >= 0; index--) {
            for (int access = 0; access <= limit; access++) {
                int choice1 = Integer.MAX_VALUE;
                int washTime = Math.max(access, drinks[index]) + wash; // washTime 表示仅洗完index这个杯子时的时间点
                int end1 = process(drinks, volatilize, wash, index + 1, washTime);
                if (end1 <= limit)
                    choice1 = Math.max(washTime, end1);  // 这两个都完成了才是选择1最终结束的时间

                // 选择2：让当前杯子自由挥发
                int choice2 = Math.max(drinks[index] + volatilize, dp[index + 1][access]);

                dp[index][access] = Math.min(choice1, choice2);
            }
        }
        return dp[0][0];
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 5, 5, 7, 10, 12, 12, 12, 12, 12, 12, 15};
        System.out.println(process(arr, 10, 3, 0, 0));
        System.out.println(dp(arr, 10, 3));
    }
}
