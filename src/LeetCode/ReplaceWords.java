package LeetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/4/8 - 14 : 21
 **/


// leetCode648
// 在英语中，我们有一个叫做 词根(root) 的概念，可以词根后面添加其他一些词组成另一个较长的单词——我们
// 称这个词为 继承词(successor)。例如，词根an，跟随着单词 other(其他)，可以形成新的单词 another(另一个)。
// 现在，给定一个由许多词根组成的词典 dictionary 和一个用空格分隔单词形成的句子 sentence。你需要将句子中的
// 所有继承词用词根替换掉。如果继承词有许多可以形成它的词根，则用最短的词根替换它。
// 你需要输出替换之后的句子。

// 1 <= dictionary.length <= 1000
// 1 <= dictionary[i].length <= 100
// dictionary[i] 仅由小写字母组成。
// 1 <= sentence.length <= 10^6
// sentence 仅由小写字母和空格组成。
// sentence 中单词的总量在范围 [1, 1000] 内。
// sentence 中每个单词的长度在范围 [1, 1000] 内。
// sentence 中单词之间由一个空格隔开。
// sentence 没有前导或尾随空格。


public class ReplaceWords {

    // 使用前缀树
    public static PrefixNode root;

    public static String replaceWords(List<String> dictionary, String sentence) {
        if (dictionary == null || dictionary.size() == 0 || sentence == null || sentence.isEmpty())
            return "";
        root = new PrefixNode();
        for (String root : dictionary)
            insert(root);
        StringBuilder sb = new StringBuilder("");
        String[] words = sentence.split(" ");
        for (String word : words){
            boolean met = false;
            char[] chs = word.toCharArray();
            PrefixNode cur = root;
            for (int i = 0; i < chs.length; i++) {
                int path = chs[i] - 'a';
                // 根据官方给的例子，他们希望，如果一个单词没有dictionary中的前缀，那就保持原样添加
                if (cur.nexts[path] == null){
                    sb.append(String.valueOf(chs)).append(" ");
                    met = true;
                    break;
                }
                // 说明还有
                cur = cur.nexts[path];
                if (cur.end){
                    sb.append(String.valueOf(chs, 0, i + 1)).append(" ");
                    met = true;
                    break;
                }
            }
            if (!met) // 那就说明该单词没有在dic中找到任何一个前缀 所以得把自己完全加入答案
                sb.append(word).append(" ");

        }
        return sb.substring(0, sb.length() - 1);
    }



    public static class PrefixNode {
        public PrefixNode[] nexts;
        public boolean end;

        public PrefixNode() {
            nexts = new PrefixNode[26];
            end = false;
        }
    }


    public static void insert(String word){
        PrefixNode cur = root;
        char[] chs = word.toCharArray();
        for (char ch : chs) {
            int path = ch - 'a';
            if (cur.nexts[path] == null)
                cur.nexts[path] = new PrefixNode();
            cur = cur.nexts[path];
        }
        cur.end = true;
    }
    // --------------------------------------------------------------------------------------




    // 因为用最开始的方法提交时，官方报错了，但是错误例子是一个非常长的例子，根本不可能肉眼看出错误，
    // 所以，必须得实现一个对数器，然后手动控制长度，找出错误



    // 用于做对数器的暴力方法
    public static String replaceWords2(List<String> dic, String sen){
        StringBuilder sb = new StringBuilder("");
        String[] words = sen.split(" ");
        for (String word : words){
            int cur = word.length();
            boolean isMatched = false;
            for (String root : dic){
                isMatched = true;
                for (int i = 0; i < Math.min(root.length(), word.length()); i++) {
                    if (root.charAt(i) != word.charAt(i)){
                        isMatched = false;
                        break;
                    }
                }
                if (isMatched)
                    cur = Math.min(cur, root.length());
            }
            sb.append(word, 0, cur).append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }



    // 随机生成一个dictionary
    // maxNum表示最多生成几个词根   maxLen表示每个词根的最大长度
    private static List<String> randomDic(int maxNum, int maxLen){
        ArrayList<String> res = new ArrayList<>();
        int num = (int) (Math.random() * maxNum) + 1;
        for (int i = 0; i < num; i++) {
            int len = (int) (Math.random() * maxLen) + 1;
            StringBuilder sb = new StringBuilder("");
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + (int) (Math.random() * 26)));
            }
            res.add(sb.toString());
        }
        return res;
    }



    // 随机生成一个sentence
    // maxNum表示最多生成几个单词   maxLen表示每个单词的最大长度
    private static String randomSen(int maxNum, int maxLen){
        StringBuilder res = new StringBuilder("");
        int num = (int) (Math.random() * maxNum) + 1;
        for (int i = 0; i < num; i++) {
            int len = (int) (Math.random() * maxLen) + 1;
            StringBuilder sb = new StringBuilder("");
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + (int) (Math.random() * 26)));
            }
            res.append(sb).append(" ");
        }
        return res.substring(0, res.length() - 1);
    }


    // for test
    private static void test(int testTime, int dicNum, int dicLen, int senNum, int senLen){
        for (int i = 0; i < testTime; i++) {
            List<String> dic = randomDic(dicNum, dicLen);
            String sen = randomSen(senNum, senLen);
            String res1 = replaceWords(dic, sen);
            String res2 = replaceWords2(dic, sen);
            if (!isSame(res1, res2)){
                System.out.println("词根列表：" + dic);
                System.out.println("单词列表：" + sen);
                System.out.println("前缀树方法：" + res1);
                System.out.println("对数器方法：" + res2);
                return;
            }
        }
        System.out.println("AC");
    }

    private static boolean isSame(String a, String b) {
        if (a == null || b == null || a.length() != b.length())
            return false;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i))
                return false;
        }
        return true;
    }


    public static void main(String[] args) {
        test(10000, 10, 8, 10, 7);
//        List<String> dic = new ArrayList<>(List.of(new String[]{"jkid"}));
//        String sen = "j cad";
//        System.out.println(replaceWords(dic, sen));
    }

}
