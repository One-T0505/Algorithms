package Hard;

import java.util.HashSet;

// leetCode291
// 给定两个字符串，一个是模版串p，一个是原始字符串s，如果s能和模版p匹配，返回true，否则返回false
// 给定一个模式和一个字符串 s，如果 s 与模式匹配则返回 true。
// 如果存在单个字符到字符串的某些双射映射，则字符串 s 与 p 匹配，如果 p 中的每个字符都被它映射到的字符串替换，则
// 生成的字符串为 s。
// 双射映射意味着没有两个字符映射到同一个字符串，也没有字符映射到两个不同的字符串。
// eg:
//    p = abab   s = redblueredblue   可以将red-->a  blue-->b  返回true
//    p = aaaa   s = asdasdasdasd     可以将asd-->a  返回true
//    p = abc    s = asdabfasd        返回false   不可以将asd-->a  abf-->b  asd-->c  asd这个字符串如果决定了映射成
//    某个字符后，之后都是一样的

public class WordPatternII {

    // 从题意可知：模版p最多只包含26种字符，题目说了所有字符都是小写英文字母。我们需要申请一个长度为26的字符串数组
    // dp   如果dp[0]=="fge"  说明模版中的a字符被已经被指派成了fge，那么后续模版中的所有a都得用fge来替换。
    // dp就是方便我们查询模版中的某个字符是否被映射了，如果被映射了，那么被映射成什么了。
    // 还需要一个集合set，里面存放已经被映射成模版中的字符的字符串，主要是方便知道哪些字符串已经被映射过了
    public static boolean wordPatternMatch(String p, String s) {
        String[] dp = new String[26];
        HashSet<String> set = new HashSet<>();
        return match(s, 0, p, 0, dp, set);
    }


    // si表示当前来到的原始字符串s的位置   pi表示当前来到的模版串p的位置
    // 返回值：从si开始及其最后能否成功匹配模版串中pi位置到最后的所有字符
    private static boolean match(String s, int si, String p, int pi, String[] dp, HashSet<String> set) {
        // 两个都空了
        if (si == s.length() && pi == p.length())
            return true;
        // 一个空，一个不空
        if (si < s.length() || pi < p.length())
            return false;
        // 执行到这里说明两个字符串都还没处理完
        char c = p.charAt(pi);
        String cur = dp[c - 'a']; // 将当前模版串字符对应的字符串取出
        // 说明当前模版中的字符已经被指派过了，有字符串映射到了当前字符
        if (cur != null) {
            return si + cur.length() <= s.length() && // 这个条件是说s中剩的字符数量是否还够
                    cur.equals(s.substring(si, si + cur.length())) && // 并且也要匹配得上
                    match(s, si + cur.length(), p, pi + 1, dp, set);   // 后续也要能完成匹配
        }
        // 执行到这里说明当前模版串中的字符是没被指派过的
        // 那么就可以从si到结尾枚举任意一个子串去映射c
        int end = s.length();

        // ===================================================
        // 下面的这一段代码是为了剪枝用的，直接跳过这一段不看也是完全可行的算法，把下面这段删除，算法依然可行
        // 下面的for循环就是枚举了，这单代码就是为了让枚举高效，不去枚举那些不可能的实例
        // 比如:        s == ac bd de ac __? ? ?__   ac-->a  bd-->b  de-->c  ?表示s剩余的字符串
        //       模版串 p ==  abca cbbtfaab   前面的abca刚好和s匹配好了，现在模版来到了c字符，一般意义的做法就是枚举
        //       ?的前缀串，从1个到..  假设s还剩23个字符，那就是从1～23枚举23遍。但实际上不用。因为模版串还剩 bbtfaab
        //       这些字符还没匹配，也就是说s尾部必须要留一些给bbtfaab去匹配，所以枚举时不可能使用所有剩下的字符。
        //       那么枚举的长度限制是多少呢？ b是已经指派过的 bd-->b，所以字符b对应的字符串长度为2，因为bbtfaab还有3个b
        //       没匹配。所以至少还要6个字符，a对应的字符串长度也是2，t、f是没被指派过的，但是他们都至少会对应一个长度为1的
        //       字符串，所以我们需要至少给 bbtfaab 留下：2+2+1+1+2+2+2 == 12个字符，那么当前字符c最多能枚举到的长度
        //       就是23-12==11，这样枚举的效率就能提高很多。
        for (int i = pi + 1; i < p.length(); i++) {
            end -= dp[p.charAt(i) - 'a'] == null ? 1 : dp[p.charAt(i) - 'a'].length();
        }
        // ===================================================


        for (int i = si; i < end; i++) {
            // 从si出发的所有前缀串全试一遍
            cur = s.substring(si, i + 1);
            // 只用尝试那些没被用过的字符串
            if (!set.contains(cur)) {
                set.add(cur);
                dp[c - 'a'] = cur;
                if (match(s, i + 1, p, pi + 1, dp, set))
                    return true;
                // 执行到这里，说明当前尝试的前缀串不可行，于是恢复现场
                dp[c - 'a'] = null;
                set.remove(cur);
            }
        }
        // 所有的尝试都不可行就返回false
        return false;
    }
}
