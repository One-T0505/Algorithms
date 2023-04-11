package class03;


// 只由小写字母(a~z) 组成的一批字符串都放在字符类型的数组String[] arr 中，如果其中某两个字符串所含有的
// 字符种类完全一样就将两个字符串算作一类。比如: baacbba和bac就算作一类。返回arr中有多少类?

import java.util.HashSet;

public class _04_Classification {

    // 最优解：一个int有32位，从最低位往左数26个位分别对应a～z，最低位对应a；对每一个字符串中出现的字符，让对应位设置为1，
    //        然后把这个int数存在HashSet中，最后看集合中有几个不同的int整数即可.

    public static int classification(String[] arr) {
        if (arr == null || arr.length < 1)
            return 0;
        HashSet<Integer> set = new HashSet<>();
        int bitMap;
        for (String s : arr) {
            bitMap = 0;
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++)
                // 如果字符串有两个b，那么000就会和010做两次或操作，就会变成010,所以一个相同字符出现多次都不影响，
                // 只要出现一次对应位就成1了，再出现多的次数也不会影响结果
                bitMap |= 1 << (chars[i] - 'a');
            set.add(bitMap);
        }
        return set.size();
    }
}
