package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.arrays;

// 给定一个非负有序数组arr，再给定一个上下界限[lower, upper]，返回缺失的区间。比如：
// arr = [0, 1, 5, 49, 76]  [lower, upper] == [-3, 99]
// 那么返回的结果是：["-3~(-1)", "2~4", "6~48", "50~75", "77~99"]
// 以字符串数组的形式返回，返回数组arr在[lower, upper]范围内缺失的部分

public class _0163_MissingRanges {

    public static List<String> missingRanges(int[] arr, int lower, int upper) {
        if (arr == null)
            return null;
        ArrayList<String> res = new ArrayList<>();
        for (int elem : arr) {
            if (elem < lower)
                continue;
            if (elem > upper){
                res.add(range(lower, upper));
                return res;
            }
            if (elem > lower)
                res.add(range(lower, elem - 1));
            lower = elem + 1;
        }
        if (lower <= upper)
            res.add(range(lower, upper));
        return res;
    }

    private static String range(int L, int R) {
        StringBuilder sb = new StringBuilder();
        sb.append(L);
        if (L == R)
            return sb.toString();
        sb.append("->").append(R);
        return sb.toString();
    }



    // for test
    public static void test(int testTime, int maxSize, int maxVal, int Limit){
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.randomNoNegativeArr(maxSize, maxVal);
            Arrays.sort(arr);
            int a = (int) (Math.random() * (Limit + 1)) - (int) (Math.random() * (Limit + 1));
            int b = (int) (Math.random() * (Limit + 1)) - (int) (Math.random() * (Limit + 1));
            int lower = Math.min(a, b);
            int upper = Math.max(a, b);
            List<String> res1 = missingRanges(arr, lower, upper);
            List<String> res2 = missingRanges(arr, lower, upper);
            if (res1.size() != res2.size() || !res1.equals(res2)){
                System.out.println("Failed");
                arrays.printArray(arr);
                System.out.println("区间：" + "[" + lower + ", " + upper + "]");
                System.out.println(res1);
                System.out.println(res2);
                return;
            }
        }
        System.out.println("AC");
    }

}
