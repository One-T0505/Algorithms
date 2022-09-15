package Tree;

public class PrintFolds {
    // 折纸打印折痕问题
    public static void printFolds(int N) { // N 表示折纸几次，down==true时表示为凹折痕；down==false时表示为凸折痕
        printInOrder(1, N, true);
    }

    private static void printInOrder(int nodeHeight, int treeHeight, boolean down) {
        if (nodeHeight > treeHeight)
            return;
        printInOrder(nodeHeight + 1, treeHeight, true);
        System.out.println(down ? nodeHeight + "凹" : nodeHeight + "凸");
        printInOrder(nodeHeight + 1, treeHeight, false);
    }

    public static void main(String[] args) {
        PrintFolds.printFolds(3);
    }
}
