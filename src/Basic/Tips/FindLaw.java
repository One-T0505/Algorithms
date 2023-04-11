package Basic.Tips;

// 这一类题目是一些找规律的题。很多人误以为需要先找到规律并用数学表达式写出来之后，才能写代码。
// 有时候是可以直接通过大量输出到控制台找输出的规律，再反向写代码。所以需要一个暴力解法来输出答案，这个方法不需要性能多好
// 只要能正确输出答案就行，根据大量的输出结果去反推数学规律。

public class FindLaw {
    // 1.小虎去买苹果，商店只提供两种类型的塑料袋，每种类型都有任意数量。
    //     1) 能装下6个苹果的袋子   2) 能装下8个苹果的袋子
    //   小虎可以自由使用两种袋子来装苹果，但是小虎有强迫症，他要求自己使用的袋子数量必须最少，且使用的每个袋子必须装满。
    //   给定一个正整数N，返回至少使用多少袋子。如果N无法让使用的每个袋子必须装满，返回-1

    // 像这样的题很大可能就是一上来先找数学规律，然后再递推写代码，这道题是因为数学规律好找，所以显得打表法没那么重要，如果
    // 换一个很难找数学规律的题，打表法的优势就凸显出来了。

    // 暴力解法  时间复杂度：O(N/8)->O(N)
    public static int shoppingV1(int n){
        if (n <= 0)
            return -1;
        int max = n >> 3;
        for (int i = max; i >= 0; i--) {  // 逐一尝试最大袋子数
            int rest = i == 0 ? n : n - (i << 3);
            if ((rest & 1) == 1)  // 如果余数是奇数
                return -1;
            if (rest == 0)
                return i;
            if (rest % 6 == 0)
                return i + rest / 6;
        }
        return -1;
    }

    // main方法中用暴力解法将1～100的情况的结果都输出了，发现了这样的规律：
    // 从18开始，每8个数为一组：18～25，26～33，34～41... 每8组数中，奇数就
    // 返回-1，偶数就返回 3 + (n - 18)/8; 1～17的数： 最硬核的方法直接写，不找规律
    // 下面的代码显示的规律，是根本不可能凭自己想出来的。而且电脑打印出的数据找出的硬核规律，
    // 不用管它具体什么含义。
    // 时间复杂度：O(1)  !!!!!!!!!!!
    public static int shoppingV2(int n){
        if (n <= 0)
            return -1;
        // 不管再1～17，还是之后的数，只要是奇数都是-1
        if ((n & 1) == 1)
            return -1;
        if (n < 18){ // 1～17
            if (n == 6 || n == 8)
                return 1;
            else if (n == 14 || n == 12 || n == 16)
                return 2;
            else
                return -1;
        }
        return 3 + ((n - 18) >> 3);
    }

    // 问题1的测试方法
    public static void test1(int testTime){
        for (int i = 1; i <= testTime; i++) {
            int res1 = shoppingV1(i);
            int res2 = shoppingV2(i);
            if (res1 != res2){
                System.out.println("Failed");
                System.out.println("暴力：" + res1 + " " + i);
                System.out.println("进阶：" + res2 + " " + i);
                return;
            }
        }
        System.out.println("AC");
    }
    // ==========================================================================================


    // 2.给定一个正整数N，表示有N份青草统一堆放在仓库里。有一只牛和一只羊，牛先吃，羊后吃，它俩轮流
    // 吃草，不管是牛还是羊，每一轮能吃的草量必须是：1， 4，16， 64*.(4的某次方)，不用是递增吃的，
    // 是每一次都可以吃1或4或16... 谁最先把草吃完，谁获胜。假设牛和羊都绝顶聪明，都想赢,都会做出理性
    // 的决定，根据唯一的参数N，返回谁会赢。

    // 时间复杂度：O(N)
    public static String eatGrassV1(int n){
        if (n < 5) // base case的范围可以自己决定
            // n=0或n=2时羊赢，因为先手先面对0的状况，所以输
            return n == 0 || n == 2 ? "后手" : "先手";
        // 这个函数中是牛先吃
        int want = 1;
        // 先手尝试所有可能的值，如果有一个是可以赢的，那就是先手赢，因为都绝顶聪明，
        // 如果有一种尝试可以赢，那先手必然会这么选择
        while (want <= n){
            // 当前方法中的先手，就是eatGrass(n - want)中的后手
            if (eatGrassV1(n - want).equals("后手"))
                return "先手";
            if (want <= (n >> 2)) // 防溢出
                want <<= 2;
            else
                break;
        }
        return "后手";
    }

    // 输出了上面暴力解法后，发现了非常明显的规律：从0～4：后先后先先  5～9：后先后先先
    // 10～14：后先后先先   15～19：后先后先先，于是有了下面的方法，是不是非常地简单。
    // 提示：用上面的递归方法测试输出0～100时结果输出很慢，因为递归很费时间。建议测试0～90即可
    // 时间复杂度：O(1)
    public static String eatGrassV2(int n){
        if (n % 5 == 0 || n % 5 == 2)
            return "后手";
        return "先手";
    }

    // 测试方法
    public static void test2(int testTime){
        for (int i = 1; i <= testTime; i++) {
            String res1 = eatGrassV1(i);
            String res2 = eatGrassV2(i);
            if (res1 != res2){
                System.out.println("Failed");
                System.out.println("暴力：" + res1 + " " + i);
                System.out.println("进阶：" + res2 + " " + i);
                return;
            }
        }
        System.out.println("AC");
    }
    // ==========================================================================================


    // 3.定义一种数：可以表示成若干(数量>1)连续正数和的数。比如: 5 = 2+3；12 = 3+4+5
    //   反例：1不是这样的数，因为要求数量大于1个、连续正数和；2= 1 + 1，2也不是，因为等号右边
    //   不是连续正数。给定一个参数N，返回是不是可以表示成若干连续正数和的数

    // 暴力尝试的思路：从1开始每次往下加一个数，1+2，1+2+3，.... 如果有一个i使得：1+2+..+i < N
    // 并且，1+2+..+i+(i+1) > N，就说明以1开头是找不到这样的累加和的；于是再以2开头尝试。
    public static boolean v1(int N){
        if (N <= 2)
            return false;
        for (int start = 1; start < N; start++) {
            int sum = start;
            for (int j = start + 1; j < N; j++) {
                sum += j;
                if (sum > N)
                    break;
                if (sum == N)
                    return true;
            }
        }
        return false;
    }

    // 通过上面的暴力尝试法打表发现，只要是2的整数次幂就返回false
    public static boolean v2(int N){
        if (N <= 2)
            return false;
        // 这个位运算用来求N是不是的2的指数。假如N是2的指数，那么写成2进制就是这样的形式：
        // 10，100，1000，1000，10000。。。 除了最高位其余全为0；N-1就成了01，011，0111，01111。。。
        // N-1就是除了最高位其余全为1，刚好和N完全相反。此时做与操作结果必然为0。
        // 所以， N & (N-1) == 0 则N为2的幂，否则不是。
        // 或者也可以提取出最右侧的1，看是不是和自己相等，如果相等就是，否则不是。
        // N & (-N) == N
        return (N & (N - 1)) != 0;
    }

    public static void main(String[] args) {
        test1(100);
        test2(70);
    }
}
