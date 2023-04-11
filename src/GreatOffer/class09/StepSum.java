package class09;

// 定义何为step sum ?
// 比如680：680+68+6=754,  680的step sum叫754。逐一抹去最低位然后累加。
// 给定一个正数num， 判断它是不是某个数的step sum，如果是则返回该数。

public class StepSum {

    // 思路：任意一个数，其步骤和必然是大于等于自己本身的。所以当给一个数num时，如果num是某个数的步骤和，
    // 他只可能是1～num之间的某个数，此时可以用二分查找。

    public static int isStepSum(int num) {
        if (num < 1)
            return -1;
        if (num < 10)  // 0的步骤和是0  1的步骤和是1
            return num;
        int L = 1, R = num;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            int cur = stepNum(mid);
            if (cur == num)
                return mid;
            else if (cur < num)
                L = mid + 1;
            else
                R = mid - 1;
        }
        return -1;
    }


    // 给一个正数n，求他的步骤和
    private static int stepNum(int n) {
        int res = n;
        while (n != 0) {
            res += n / 10;
            n /= 10;
        }
        return res;
    }

}
