package LeetCode;

/**
 * ymy
 * 2023/3/26 - 16 : 32
 **/


// leetCode165
// 给你两个版本号 version1 和 version2 ，请你比较它们。
// 版本号由一个或多个修订号组成，各修订号由一个 '.' 连接。每个修订号由多位数字组成，可能包含前导零。每个版本号
// 至少包含一个字符。修订号从左到右编号，下标从 0 开始，最左边的修订号下标为 0 ，下一个修订号下标为 1 ，以此类推。
// 例如，2.5.33 和 0.1 都是有效的版本号。
// 比较版本号时，请按从左到右的顺序依次比较它们的修订号。比较修订号时，只需比较忽略任何前导零后的整数值 。也就是说，
// 修订号 1 和修订号 001 相等 。如果版本号没有指定某个下标处的修订号，则该修订号视为 0 。例如，版本 1.0 小于版本 1.1，
// 因为它们下标为 0 的修订号相同，而下标为 1 的修订号分别为 0 和 1 ，0 < 1 。
// 返回规则如下：
//   如果 version1 > version2 返回 1，
//   如果 version1 < version2 返回 -1，
//   除此之外返回 0。


// 1 <= version1.length, version2.length <= 500
// version1 和 version2 仅包含数字和 '.'
// version1 和 version2 都是 有效版本号
// version1 和 version2 的所有修订号都可以存储在 32 位整数中


public class CompareVersion {

    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null || version1.length() == 0 || version2.length() == 0)
            return 0;
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int N = v1.length;
        int M = v2.length;
        String[] more = N > M ? v1 : v2;
        String[] less = more == v1 ? v2 : v1;
        for (int i = 0; i < less.length; i++) {
            int res = compare(v1[i].toCharArray(), v2[i].toCharArray());
            if (res != 0)
                return res;
        }
        if (N == M)
            return 0;
        // 因为题目会给这样的版本号：1.008.000   1.8
        // 虽然前两部分相等，但是版本1有三个部分，不能仅根据长度多的就直接判定大小，因为很有可能给的修订好是无用的
        for (int i = less.length; i < more.length; i++) {
            char[] cur = more[i].toCharArray();
            for (char c : cur) {
                if (c != '0')
                    return more == v1 ? 1 : -1;
            }
        }
        return 0;
    }


    private static int compare(char[] p1, char[] p2) {
        int N = p1.length;
        int M = p2.length;
        int i = 0, j = 0;
        // 都先找到不为0的第一个数字 先跳过前导0
        while (i != N && p1[i] == '0'){
            i++;
        }
        while (j != M && p2[j] == '0'){
            j++;
        }
        if (i == N && j == M)
            return 0;
        if (i == N ^ j == M)
            return i == N ? -1 : 1;
        // 两个都不为空 并且都跳过了前导0  此时又要从后往前比了。
        // 比如：1.002  1.0010   当第二部分都跳过了前导0后，如果还按照从左到右的顺序比，那么2 > 1
        // 但实际上是 10 > 2   所以方向应该变一下  其实可以根据有效位数的数量直接判断
        if (N - i != M - j)
            return N - i > M - j ? 1 : -1;
        // 说明剩余有效位数相等  那么就可以依然从左到右遍历了，从高位到低位，但凡有一个区分出大小了，后面的低位就可以不看了
        while (i != N && j != M){
            if (p1[i] == p2[j]){
                i++;
                j++;
            } else
                return p1[i] < p2[j] ? -1 : 1;
        }
        return 0;
    }


    public static void main(String[] args) {
        String v1 = "1.2";
        String v2 = "1.10";
        System.out.println(compareVersion(v1, v2));
    }
}
