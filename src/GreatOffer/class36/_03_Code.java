package GreatOffer.class36;

// 来自美团
// 给定两个字符串s1和52  返回在s1中有多少个子串等于s2

public class _03_Code {


    // KMP 模型应用改写   将问题翻译一下就是：s2 能匹配上 s1 中多少个子串  所以 s1 是原串   s2 是匹配串
    public static int same(String s1, String s2) {
        if (s1 == null || s2 == null)
            return 0;
        if (s1.equals("") || s2.equals(""))
            return 1;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        return count(c1, c2);
    }


    //  返回 c1中有几个子串等于c2
    private static int count(char[] c1, char[] c2) {
        int[] next = getNexts(c2);
        int x = 0;
        int y = 0;
        int count = 0;
        while (x < c1.length){
            if (c1[x] == c2[y]){
                x++;
                y++;
                // 这个是新增的代码，每次让y到了终止位置，就说明匹配到了一个子串了，所以count+1，然后继续
                // 让y回滚。
                if (y == c2.length){
                    count++;
                    y = next[y];
                }
            } else if (next[y] == -1)
                x++;
            else
                y = next[y];
        }
        return count;
    }




    // 生成 chs 的 next 数组，只不过，这道题里，我们需要生成一个比chs长度大1的next数组
    private static int[] getNexts(char[] chs) {
        if (chs.length == 1)
            return new int[] {-1, 0};
        int N = chs.length;
        int[] next = new int[N + 1];
        next[0] = -1;
        // next[1] = 0;
        int i = 2;
        int cn = next[i - 1];
        while (i < next.length){
            if (chs[i - 1] == chs[cn])
                next[i++] = ++cn;
            else if (cn > 0) {
                cn = next[cn];
            } else
                next[i++] = 0;
        }
        return next;
    }
}
