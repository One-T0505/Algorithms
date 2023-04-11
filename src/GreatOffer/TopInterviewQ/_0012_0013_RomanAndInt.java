package GreatOffer.TopInterviewQ;

import java.util.HashMap;

public class _0012_0013_RomanAndInt {


    // 将int转换成罗马数字
    public static String intToRoman(int num) {
        // 该题目给的int数字不超过3999，所以最大的罗马数字准备到三千就足够了
        String[][] Roman = {{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},
                {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},
                {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},
                {"", "M", "MM", "MMM"}};

        StringBuilder roman = new StringBuilder();
        roman.append(Roman[3][num / 1000 % 10])
                .append(Roman[2][num / 100 % 10])
                .append(Roman[1][num / 10 % 10])
                .append(Roman[0][num % 10]);
        return roman.toString();
    }

    public static void main(String[] args) {
        _0012_0013_RomanAndInt v = new _0012_0013_RomanAndInt();
        System.out.println(v.romanToInt("MCMX"));
    }

    // 将罗马数字转换成int
    public int romanToInt(String s) {
        HashMap<Character, Integer> hashMap = new HashMap<>();
        hashMap.put('I', 1);
        hashMap.put('V', 5);
        hashMap.put('X', 10);
        hashMap.put('L', 50);
        hashMap.put('C', 100);
        hashMap.put('D', 500);
        hashMap.put('M', 1000);
        char[] chs = s.toCharArray();
        int res = 0;
        for (int i = 0; i < chs.length; i++) {
            int cur = hashMap.get(chs[i]);
            if (i < chs.length - 1 && cur < hashMap.get(chs[i + 1])) {
                res += hashMap.get(chs[i + 1]) - cur;
                i++;
            } else
                res += cur;
        }
        return res;
    }
}
