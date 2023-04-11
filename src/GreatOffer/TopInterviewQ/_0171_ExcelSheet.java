package GreatOffer.TopInterviewQ;


// 给你一个字符串 columnTitle ，表示 Excel 表格中的列名称。返回 该列名称对应的列序号 。
// A->1  B->2 ... Z->26  AA->27  AB->28

public class _0171_ExcelSheet {

    public static int titleToNumber(String columnTitle) {
        if (columnTitle == null || columnTitle.length() == 0)
            return Integer.MIN_VALUE;
        char[] chs = columnTitle.toCharArray();
        int N = chs.length;
        int res = 0;
        int power = 1;
        for (int i = N - 1; i >= 0; i--) {
            res += ((chs[i] - 'A') + 1) * power;
            power *= 26;
        }
        return res;
    }


    public static void main(String[] args) {
        System.out.println(titleToNumber("ZY"));
    }
}
