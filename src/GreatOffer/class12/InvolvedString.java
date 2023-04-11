package class12;


// 给定长度为m的字符串aim，以及一个长度为n的字符串str.问能否在str中找到一个长度为m的子串,
// 使得这个子串刚好由aim的m个字符组成，顺序无所谓。返回任意满足条件的一个子串的起始位置，未找到返回-1
// 比如：aim="acbca"  str="dccaabe" 那么str[1..5]符合要求，因为这一段有2个a，一个b，2个c。所以返回1。

public class InvolvedString {

    // 思路
    // aim字符串的有效信息是长度，还有分别有几个不同字符组成的，所以用一张hashMap记录好，那么aim是什么就无所谓了。
    // 然后用滑动窗口去遍历str,当窗口小于m时只能右扩，当窗口等于m时，每次右扩一个左退一个。这个词频表就相当于欠债表，
    // 记录着某个字符还欠多少个。
    // 用一个all记录一共需要拼凑多少个字符。遍历主字符串，窗口右边界R每次新扩进来的字符，如果在词频表中的次数还>0，说明还需要还，
    // 那么就减1，并且让all也减1；如果新扩进来的字符在词频表中已经<=0了，说明之前已经还完了，那么此时词频可以继续减1，但all
    // 不能减少。每次左边退掉的字符，如果在词频表中>=0，那么词频加1，并且all加1；如果<0，那么词频加1，但是all不变。因为<0，
    // 说明之前的字符比需要的多，所以此时退还的字符不会影响实际的all。

    public static int consist(String str, String aim) {
        if (str == null || aim == null || str.length() < aim.length())
            return -1;
        char[] a = aim.toCharArray();
        int all = a.length;
        int M = a.length;  // 窗口大小
        int[] counter = new int[256];  // 统计aim的词频，256个足以表示各种字符，除了中文
        for (char c : a)
            counter[c]++;
        char[] s = str.toCharArray();
        int N = s.length;
        int R = 0;  // 滑动窗口的右边界
        // 形成初步的窗口
        for (; R < M; R++) {
            if (counter[s[R]]-- > 0)
                all--;
        }

        // 窗口初步形成了，并没有判断有效无效，决定下一个位置一上来判断
        // 接下来的过程，窗口右进一个，左吐一个
        for (; R < N; R++) {
            // 每次一进循环，先判断上一个窗口的有效性，
            if (all == 0)
                return R - M;
            if (counter[s[R]]-- > 0)  // 右边扩一个
                all--;
            if (counter[s[R - M]]++ >= 0)  // 左边退掉一个
                all++;
        }

        // 当退出循环后，R==N，此时N-M～N-1这个窗口还没判断
        return all == 0 ? R - M : -1;
    }
}
