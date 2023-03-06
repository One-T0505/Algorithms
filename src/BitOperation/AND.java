package BitOperation;

public class AND {

    // 一个二进制数，如何提取出它最右侧的1？
    // 自己本身 & 相反数   这里的相反数是指二进制中按补码规则去相反数：符号位不变，数值位取反最后加1。
    public static int rightestOne(int num) {
        return num & (-num);
        // 这是十进制整数的写法，因为底层默认会按补码规则来获取相反数。
        // 也可以按照二进制的做法获得补码规则下的相反数：~num+1
    }

    public static void main(String[] args) {
        System.out.println(4 & (~4 + 1));
    }
}
