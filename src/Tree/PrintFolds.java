package Tree;

public class PrintFolds {
    // 折纸打印折痕问题
    public static void printFolds(int N) { // N 表示折纸几次，down==true时表示为凹折痕；down==false时表示为凸折痕
        printInOrder(1, N, true);
    }

    // N 表示了整棵树的高度
    private static void printInOrder(int nodeHeight, int N, boolean down) {
        if (nodeHeight > N)
            return;
        printInOrder(nodeHeight + 1, N, true);
        System.out.print(down ? nodeHeight + "凹\t" : nodeHeight + "凸\t");
        printInOrder(nodeHeight + 1, N, false);
    }

    public static void main(String[] args) {
        printFolds(4);
    }
}
