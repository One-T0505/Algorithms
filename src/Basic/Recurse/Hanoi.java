package Basic.Recurse;

// 递归问题之汉诺塔问题
public class Hanoi {
    // 1.将第1～N-1块圆盘从左边移到中间
    // 2.将第N块圆盘从左边移到右边
    // 3.将第1～N-1块圆盘从中间移到右边
    // 所以汉诺塔问题归根到底就是上面的三个动作，其中第2个动作已经是最基本的动作无法拆分，而第1、3步还可以继续递归

    // 先实现一个简单的版本
    public static void hanoi_v1(int n){ // 一共有n个盘子在最左边
        LeftToRight(n); // 将N块圆盘从左边移到右边
    }

    private static void LeftToRight(int n) {
        if (n == 1){
            System.out.println("move 1 from left to right");
            return;
        }
        LeftToMid(n - 1); // 将第1～N-1块圆盘从左边移到中间
        System.out.println("move 1 from left to right");
        MidToRight(n - 1);  // 将第1～N-1块圆盘从中间移到右边
    }

    private static void MidToRight(int n) {
        if (n == 1){
            System.out.println("move 1 from mid to right");
            return;
        }
        MidToLeft(n - 1); // 将第1～N-1块圆盘从中间移到左边
        System.out.println("move 1 from mid to right");
        LeftToRight(n - 1); // 将第1～N-1块圆盘从左边移到右边
    }

    private static void MidToLeft(int n) {
        if (n == 1){
            System.out.println("move 1 from mid to left");
            return;
        }
        MidToRight(n - 1);  // 将第1～N-1块圆盘从中间移到右边
        System.out.println("move 1 from mid to left");
        RightToLeft(n - 1);    // 将第1～N-1块圆盘从右边移到左边
    }

    private static void RightToLeft(int n) {
        if (n == 1){
            System.out.println("move 1 from right to left");
            return;
        }
        RightToMid(n - 1);  // 将第1～N-1块圆盘从右边移到中间
        System.out.println("move 1 from right to left");
        MidToLeft(n - 1);    // 将第1～N-1块圆盘从中间移到左边
    }

    private static void RightToMid(int n) {
        if (n == 1){
            System.out.println("move 1 from right to mid");
            return;
        }
        RightToLeft(n - 1);  // 将第1～N-1块圆盘从右边移到左边
        System.out.println("move 1 from right to mid");
        LeftToMid(n - 1);    // 将第1～N-1块圆盘从左边移到中间
    }

    private static void LeftToMid(int n) {
        if (n == 1){
            System.out.println("move 1 from left to mid");
            return;
        }
        LeftToRight(n - 1);  // 将第1～N-1块圆盘从左边移到右边
        System.out.println("move 1 from left to mid");
        RightToMid(n - 1);    // 将第1～N-1块圆盘从右边移到中间
    }


    // 升级版的汉诺塔解法：要忘记左中右这三个概念，用from、to、other来替代。比如将n块圆盘从左移动到右，就可以替换为：
    // 1> 将第1～n-1块圆盘从from移动到other
    // 2> 将第n块圆盘从from移动到to
    // 3> 将第1～n-1块圆盘从other移动到to
    public static void hanoi_v2(int n){
        if (n > 0)
            func(n, "left", "right", "mid");
    }

    private static void func(int n, String from, String to, String other) {
        if (n == 1){
            System.out.println("move 1 from " + from + " to " + to);
        }else {
            func(n - 1, from, other, to);
            System.out.println("move 1 from " + from + " to " + to);
            func(n - 1, other, to, from);
        }
    }


    public static void main(String[] args) {
        hanoi_v2(4);
    }
}
