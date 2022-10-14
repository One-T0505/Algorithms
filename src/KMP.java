
// 若des是ori的子串，返回匹配成功的索引；若不存在返回-1
public class KMP {

    // 在ori这个字符串中匹配des字符串，若存在，则返回第一次出现的索引
    public static int kmp(String ori, String des){
        if (ori == null || des == null || ori.length() < 1 || ori.length() < des.length())
            return -1;
        char[] source = ori.toCharArray();
        char[] template = des.toCharArray();
        // 获取匹配字符串的最大前缀和数组
        int[] next = getNextArr(template); // O(M)
        // i表示在原始串中已经匹配到的位置，j表示在目标串中已经匹配到的位置
        int i = 0, j = 0;
        while (i < source.length && j < template.length){ // O(N)
            if (source[i] == template[j]){
                i++;
                j++;
            } else if (next[j] != -1) {
                j = next[j];
            } else { // 等价于 j == 0
                i++;
            }
        }
        // i越界了或j越界了。上面while中，只有两个元素相等j才会往后移动，所以如果是j越界了就说明匹配成了
        return j == template.length ? i - template.length : -1;
    }


    // 获取前缀和数组
    private static int[] getNextArr(char[] template) {
        if (template.length == 1)
            return new int[] {-1};
        int[] next = new int[template.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2;
        int pointer = next[i - 1];
        while (i < next.length){
            if (template[pointer] == template[i - 1])
                next[i++] = ++pointer;
            else if (pointer > 0) {
                pointer = next[pointer];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }


    // KMP应用：假如给定字符串s1，s2，判断s2是不是s1的旋转子串。
    // 旋转子串的定义：假如s1=abcdef  那么s1的旋转子串有：bcdefa  cdefab  defabc  efabcd  fabcde  abcdef

    // 思路：可以将原始串复制一份拼成两倍长度：s1=abcdefabcdef  然后让s2用KMP算法去匹配s1。当长度变为两倍时，所有旋转子串
    //      都是其子串。

    public static boolean isRotatedSubString(String s1, String s2){
        if (s1 == null || s2 == null || s1.length() != s2.length())
            return false;
        // 扩充原始串
        int N = s1.length();
        String processed = s1.repeat(2);
        return kmp(processed, s2) != -1;
    }


    public static void main(String[] args) {
        String s1 = "abcdef";
        String s2 = "abcdef";
        System.out.println(isRotatedSubString(s1, s2));
    }

}
