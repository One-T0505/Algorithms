package Hard;

// leetCode483
// 以字符串的形式给出 n , 以字符串的形式返回 n 的最小好进制。
// 如果 n 的  k(k>=2) 进制数的所有数位全为1，则称 k(k>=2) 是 n 的一个好进制。
// n 的取值范围是 [3, 10^18]

public class SmallestGoodBase {

    // 流程：
    // 首先要明白，进制数越大那么位数越少。所以将n转换成二进制后能使得位数最多。
    // 我们先确定出一个位数的上下界限，不管n用几进制表示，位数都不会超过这个范围。所以上界就是用二进制表示时的位数M，
    // 下界就是2，因为任意一个数n，其n-1进制的表示都是全1，并且是11，所以每个数都必有一个好进制就是n-1，但并不一定
    // 是最优解。位数不可能再比2少了，所以位数的上下界就确定了：2~M  2是不用尝试的，只需要遍历尝试3～M即可。
    // 当遍历尝试到N位时表示：现在我们就是要用某种进制来表示n使其数位长度为N，所以是数位是固定的，几进制表示能让数位
    // 为N是我们要考虑的。当数位确定后，我们可以确定出一个进制的上下界限，即超出这个范围的进制来表示n，最后的数位必然>N
    // 或者<N.
    // 这个进制的上下界限的确定需要一点技巧，先来举个例子：如果我们希望数位是5，此时用k进制来表示n，
    // 那就是从0～4位。如果 n >= k^5  那么必须会用到第5位的数，因为如果用到第5位的数，那最小也是k^5，所以如果
    // n >= k^5  那么就会使用到0～5一共6个数位  所以说n要满足 n < k^5   所以 k > n的开5次方  这就是k的下界
    //
    //  5  4  3  2  1  0           5位数最小就是 10000 即 k^4  如果 n < k^4  那数位不可能用到5个
    //  ?  ?  ?  ?  ?  ?           所以n要满足 n >= k^4  所以 k <= n的开4次方   这是k的上界
    //
    // 所以     n的开5次方 < k <= n的开4次
    // 确定了进制的上下界之后就可以二分这个界限了。 二分找到一个进制，将N位数全部填1的答案x算出来和n比较
    // 如果 x < n  说明进制小了，让L==mid+1
    // 如果 x > n  说明进制打了  让R==mid-1
    // 如果 x== n  返回答案

    public static String smallestGoodBase(String n) {
        Long num = Long.valueOf(n);
        // 先判断num是否为2^n-1，如果是直接返回2，因为2进制必然是最优解
        if ((num & num + 1) == 0)
            return String.valueOf(2);
        // 不是2^n-1，那就按照上面的思路先确定位数的上下界
        // 位数的上界:  (int) (Math.ceil(Math.log(num + 1) / Math.log(2)))
        // 首先我们要算出 log2(num)  但是系统自带的Math.log的底数是e，所以需要用: log(num) / log(2) 来换底
        // 这是数学公式。 外面套的+1还有ceiling向上取整是为了正确得出结果: 如果num==62，我们需要返回6；
        // num==64，我们需要返回7  返回值是数位
        // 位数的下界是3，2不用尝试，因为位数固定为2时，num-1进制必然是好进制，可以表示num为11
        for (int m = (int) (Math.ceil(Math.log(num + 1) / Math.log(2))); m > 2; m--) {
            // 确定数位了之后，再确定进制的上下界限     n的开5次方 < k <= n的开4次
            // 所以下界要向上取整   上界要向下取整
            // 下界: num的开m次方也可以表示成num的1/m次方
            long L = (long) Math.ceil(Math.pow(num, 1.0 / m));
            long R = (long) Math.pow(num, 1.0 / (m - 1));
            while (L <= R) {
                // mid进制
                long mid = L + ((R - L) >> 1);
                long sum = 0;
                long base = 1L;
                // 将mid进制下的m位数全部为1的答案算出来
                for (int i = 0; i < m && sum <= num; i++) {
                    sum += base;
                    base *= mid;
                }
                if (sum == num)
                    return String.valueOf(mid);
                else if (sum < num)
                    L = mid + 1;
                else
                    R = mid - 1;
            }
        }
        // 这就是最差结果，用num-1进制来表示num
        return String.valueOf(num - 1);
    }


    public static void main(String[] args) {
        System.out.println((int) (Math.log(128) / Math.log(2)));
        System.out.println((Math.log(128 + 1) / Math.log(2)));
    }
}
