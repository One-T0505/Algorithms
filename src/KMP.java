public class KMP {
    // 若des是ori的子串，返回匹配成功的索引；若不存在返回-1
    public static int getIndexOf(String ori, String des){
        if (ori == null || des == null || ori.length() < 1 || ori.length() < des.length())
            return -1;
        char[] source = ori.toCharArray();
        char[] template = des.toCharArray();
        // 获取匹配字符串的最大前缀和数组
        int[] next = getNextArr(template); // O(M)

        int i = 0, j = 0;
        while (i < source.length && j < template.length){ // O(N)
            if (source[i] == template[j]){
                i++;
                j++;
            } else if (next[j] == -1) { // 等价于 j == 0
                i++;
            } else {
                j = next[j];
            }
        }
        // i越界了或j越界了。上面while中，只有两个元素相等j才会往后移动，所以如果是j越界了就说明匹配成了
        return j == template.length ? i - template.length : -1;
    }

    private static int[] getNextArr(char[] template) {
        if (template.length == 1)
            return new int[] {-1};
        int[] next = new int[template.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2, cn = next[i - 1];
        while (i < next.length){
            if (template[cn] == template[i - 1])
                next[i++] = ++cn;
            else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }
}
