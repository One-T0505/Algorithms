package Hard;

// leetCode411
// 给定一个字符串s，和一个字符串数组dictionary，试寻找s的简写形式，使其长度最短，并且该简写形式不能和dictionary
// 中的字符串产生歧义，返回该最短长度的简写字符串。简写规则如下：
// jfleds 其简写形式可以是：j5  jf4  jfl3  jfle2  jfled1  也可以是  2l3  3e2  也可以是  3eds  4ds  5s
// 数字就表示了几个字符。如果dictionary中有一个字符串是：jabcde  那么j5这个简写形式就会和jabcde产生歧义，因为
// jabcde也可以简写成j5。如果是这样的情况，那返回 5s  因为其长度只有2
// 此外，jfleds的长度为6，那么简写形式 jfled1 也是不必要的，因为简写后其长度也是6，没有缩短原始字符串
// dictionary 中的所有字符串都是原始串，没有经过简写处理

// 数据规模
// s.length == m
// dictionary.length == n
//  1 <= m <= 21
//  0 <= n <= 1000
//  log2(n) + m <= 21  if n > 0

public class UniqueAbbreviation {

    // 从题意可以提炼出一个结论：假设给定的字符串s的长度为n，不管s的简写形式是什么，其代表的长度都是n，就像上面的
    // 2l3  其代表的长度也是6。所以说，s的所有简写形式只可能和dictionary中字符串长度为n的字符串产生歧义。长度不是n的
    // 字符串必不可能和s的简写产生歧义
    // 所以整体思路就有了：s的每个字符都可以选择保留原始还是缩略，这就是背包模型，尝试出s的所有简写形式
    // 然后让每个简写形式都去dictionary中检查是否有歧义.
    // 其时间复杂度为: 2^m * n ==> O(2^21 * 1000) > 10^8  所以这种暴力尝试过不了
    // 但是这个题目给的数据规模很有意思：log2(n) + m <= 21  if n > 0   当 m == 21时，n才为1  m==20 n==2
    // 所以不可能出现m==21时，n==1000这种最差情况
    // 所以暴力解就可以过

    public static int min = Integer.MAX_VALUE;  // 取得最短缩写的长度

    public static int best = 0;  // 取得最短缩写长度时的决定，就是哪些位保留，哪些位缩略了


    // 主方法
    public static String minAbbreviation(String s, String[] dictionary) {
        min = Integer.MAX_VALUE;
        best = 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int count = 0;  // 记录dictionary中有多少个字符串的长度和s一样
        for (String w : dictionary) {
            if (w.length() == N)
                count++;
        }
        // 只用让s和words中的字符串进行判断，看是否会有歧义  为什么是int？因为方便处理。
        // words中的字符串长度都和s相等，假设t和s对应位字符一样就用0表示，不一样则用1表示 这样一个字符串t就变成了
        // 一串数字，我们只用int数据的状态位，并不使用int具体的数值.
        // 而对s简写形式的标记我们用同样的方法：如果s某位被决定保留，则用1  如果决定当前位缩略  则用0表示
        // 所以s也被转化成了一个int型数据。
        // s & t == 0  就表示 当前s的这种简写形式是会和字典中的 t 产生歧义的，所以上面转化成int只是为了判断时方便
        // 1 0 0 0 0  a b c d e
        // 0 1 1 1 1  a c b a d
        int[] words = new int[count];
        int index = 0;
        for (String w : dictionary) {
            if (w.length() == N) {
                char[] c = w.toCharArray();
                int status = 0;
                // 这里的转换是反向的  这里字符串的第0位对应的是int的最低位  但并不影响结果
                for (int i = 0; i < N; i++) {
                    if (c[i] != chs[i])
                        status |= 1 << i;
                }
                words[index++] = status;
            }
        }
        // 到这里，dictionary中和s长度相等的所有字符串全部被转化成了二进制
        // 下面的dfs就是穷举s的简写形式
        dfs(words, N, 0, 0);  // 执行完这个方法后 全局变量 min 和 best 都记录成了最好的情况
        StringBuilder sb = new StringBuilder();
        // 我们要拿着这个简写形式的二进制去实际地写出简写字符串。 1 表示对应字符保留   0 表示对应字符缩略
        // cnt表示两个保留字符之间有多少个字符被缩略了
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            if ((best & (1 << i)) != 0) { // 当前字符是1
                if (cnt != 0)
                    sb.append(cnt);
                sb.append(chs[i]);
                cnt = 0;
            } else // 如果当前字符是0 那就直接+1
                cnt++;
        }
        if (cnt > 0)
            sb.append(cnt);
        return sb.toString();
    }


    // words 和 N 是固定参数  N是s的长度  i表示要处理s的第i位了  pre表示0～i-1位做的决定对应的二进制状态位
    private static void dfs(int[] words, int N, int i, int pre) {
        if (!isSolved(words, pre)) { // 如果之前的决定还不能找到合法的简写形式
            if (i < N) {
                dfs(words, N, i + 1, pre); // 当前字符不保留
                dfs(words, N, i + 1, pre | (1 << i));  // 当前字符保留  所以要在对应位上设置一个1
            }
        } else { // 如果之前的决定已经能找到合法的简写形式
            int len = abbrLen(pre, N);
            if (len < min) {
                min = len;
                best = pre;
            }
        }
    }


    // 当前这种简写形式form是否可行  如果和某一个字符串产生歧义了直接返回false
    private static boolean isSolved(int[] words, int form) {
        for (int w : words) {
            if ((w & form) == 0)
                return false;
        }
        return true;
    }


    // 需要明白，我们只是用了一个等长的二进制位来记录对s的简写形式，但s实际上还是其本身，并没有改变。
    // 我们现在是要拿着二进制位，也就是说做的所有决定，一个int就能表示一种简写形式，我们需要用该二进制计算
    // 最终缩写的长度是多少
    // 简写的二进制其实就是0、1串   0 0 1 1 0 0 1 0 1
    // 我们要做的就是找出所有连续的0，每一段连续的0都会被转化成一个字符长度，而1表示保留原始的字符，所以每个1都是一个字符

    private static int abbrLen(int form, int N) {
        int len = 0;
        int cnt = 0;  // 记录当前区间连续的1有多少
        for (int i = 0; i < N; i++) {
            if (((1 << i) & form) == 0) {
                cnt++;
            } else { // 碰到1了
                len++;
                if (cnt != 0) // 说明碰到1之前有碰到0
                    len++;
                cnt = 0; // 每次碰到1了就把0的数量清空
            }
        }
        // 最后一段可是也是连续的0
        if (cnt != 0)
            len++;
        return len;
    }


    public static void main(String[] args) {
        String[] dic = {"blade", "plain", "amber"};
        String t = "apple";
        System.out.println(minAbbreviation(t, dic));
    }
}
