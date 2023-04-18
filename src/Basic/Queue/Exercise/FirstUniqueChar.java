package Basic.Queue.Exercise;

// leetCode387
// 给定一个字符串 s ，找到它的第一个不重复的字符，并返回它的索引 。如果不存在，则返回 -1。


import java.util.Arrays;

public class FirstUniqueChar {

    public static int firstUniqChar(String s) {
        char[] chars = s.toCharArray();
        int N = chars.length;
        // count作为计数器，对应a～z 26个字母 记录每个字母第一次出现的索引，如果出现了>=2次，那就将对应值设为-1
        int[] count = new int[26];
        for (int i = 0; i < N; i++) {
            int c = chars[i] - 'a';
            if (count[c] == 0)
                count[c] = i;
            else if (count[c] > 0) { // 说明当前字符已经出现过了
                count[c] = -1;
            }
        }

        for (int i = 0; i < N; i++) {
            if (count[chars[i] - 'a'] > 0)
                return i;
        }
        return -1;
    }



    // 上面的整体思路是没错的，但是忘了一个边界问题：那就是字符串开头的字符，我们会在数组中对应位置的值设置为0，
    // 因为是开头字符位置为0；然后后面j位置假如又来了一个字符c和开头的一样，这时我们判断 count[c] == 0  那就把它
    // count[c] 设置成了j，实际上这时应该把count[c]设置为-1，因为已经出现过了。
    // 这就是数组的默认值0产生的歧义.
    public static int firstUniqChar2(String s) {
        char[] chars = s.toCharArray();
        int N = chars.length;
        // count作为计数器，对应a～z 26个字母 记录每个字母第一次出现的索引，如果出现了>=2次，那就将对应值设为-1
        int[] count = new int[26];
        // 消除0的歧义
        Arrays.fill(count, -2);
        for (int i = 0; i < N; i++) {
            int c = chars[i] - 'a';
            if (count[c] == -2)
                count[c] = i;
            else if (count[c] >= 0) { // 说明当前字符已经出现过了
                count[c] = -1;
            }
        }

        for (int i = 0; i < N; i++) {
            if (count[chars[i] - 'a'] >= 0)
                return i;
        }
        return -1;
    }
}
