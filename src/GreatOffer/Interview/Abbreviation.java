package GreatOffer.Interview;

// 给你一个字符串数组 words ，该数组由互不相同的若干字符串组成，请你找出并返回每个单词的最小缩写。
// 生成缩写的规则如下：
// 初始缩写由起始字母+省略字母的数量+结尾字母组成。
// 若存在冲突，亦即多于一个单词有同样的缩写，则使用更长的前缀代替首字母，直到从单词到缩写的映射唯一。换而言之，
// 最终的缩写必须只能映射到一个单词。若缩写并不比原单词更短，则保留原样。

// 示例
// 输入: words = ["like", "god", "internal", "me", "internet", "interval", "intension", "face", "intrusion"]
// 输出: ["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]
// 解释：internal是被interval干扰的，因为其开头和结尾都一样 公共前缀是 inter 如果要区分的话，只能缩写成 intern1l
//      和interv1l  这和原串长度一样，所以保留原样。

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Abbreviation {

    public static List<String> wordsAbbreviation(List<String> words) {
        int N = words.size();
        List<String> res = new ArrayList<>();
        // 先将每个单词化成最简形式
        for (String word : words) {
            res.add(abbreviate(word, 0));
        }
        // 上面只是做好了预处理数据结构
        // dp的每个key要不就是字符串原样，要不就是最简形式，每个最简形式可能对应了多个字符串，所以我们现在
        // 就是要将最简形式逐一增加长度，让本来有歧义的字符串消除歧义。
        int[] prefix = new int[N];
        for (int i = 0; i < N; i++) {
            while (true) { // 只有下面的break执行了才能跳出循环
                HashSet<Integer> dupes = new HashSet<>(); // 简写形式一样的全部放在一起
                for (int j = i + 1; j < N; j++) { // 从i+1开始往后找，看哪些单词简写和i位置的一样
                    if (res.get(j).equals(res.get(i)))
                        dupes.add(j); // 将重复的单词下标加入集合
                }
                // 如果集合为空，就说明没有和单词i产生歧义的单词，那么可以直接处理下一个了
                if (dupes.isEmpty())
                    break;
                // 再把自己也加进去，这样集合里就都是同一种缩写的单词了
                dupes.add(i);
                for (int pos : dupes)
                    res.set(pos, abbreviate(words.get(pos), ++prefix[pos]));
            }
        }
        return res;
    }


    // k表示用s的前0～k位置的字符当作前缀。如果是最简形式，k==0  k是前缀的终止位置
    private static String abbreviate(String s, int k) {
        // 长度<=3的字符串不用缩写，直接保持原样
        int N = s.length();
        // 0~k是前缀，数量是k+1个
        if (N - (k + 1) <= 2)
            return s;
        return s.substring(0, k + 1) + (N - k - 2) + s.charAt(N - 1);
    }
}
