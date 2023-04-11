package class17;


// 给定一个每一行有序、每一列也有序，整体可能无序的二维数组
// 再给定一个数num，返回二维数组中有没有num这个数。

public class ContainsNumInOrderedMatrix {

    // 这个思路很简单，起点从右上角的点开始，每到一个格子都需要问三件事：
    // 1.当前位置是否等于num，如果相等直接返回，若不相等，则：
    // 2.当前位置 < num，那么当前位置所在行的左边都不可能找到num。因为其左侧的元素必然小于等于当前位置的值
    // 3.当前位置 > num，那么当前位置所在列的下边都不可能找到num。因为列也有序，下侧的元素必然大于等于当前位置的值

    public static boolean isContain(int[][] m, int num) {
        if (m == null || m.length == 0 || m[0].length == 0)
            return false;
        int N = m.length;
        int M = m[0].length;
        int i = 0, j = M - 1; // 起点位置的行号和列号
        while (i < N && j >= 0) {
            if (m[i][j] == num)
                return true;
            if (m[i][j] < num)
                i++;
            else
                j--;
        }
        return false;
    }
    // 时间复杂度：O(N + M)   起点从左下角也可以
}
