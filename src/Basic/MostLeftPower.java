package Basic;


// 给定一个非负整数num，如何不用循环语句，返回>=num，并且离num最近的，2的某次方

public class MostLeftPower {

    public static int tableSizeFor(int n) {
        if ((n & (n - 1)) == 0)
            return n;
        // 下面的这几步，不管是哪个数，在执行完后都变成全1。比如9->1001，执行完后就是1111。因为int就是32位。
        // =================
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        // =================
        return (n < 0) ? 0 : n + 1;
    }


    public static void main(String[] args) {
        System.out.println(tableSizeFor(0));
    }
}
