package Basic.SystemDesign;

// 为搜索引擎设计一个搜索自动补全系统。用户会输入一条语句（最少包含一个字母，以特殊字符 '#' 结尾）。
// 给定一个字符串数组 sentences 和一个整数数组 times ，长度都为 n，其中 sentences[i] 是之前输入的句子，
// times[i] 是该句子输入的相应次数。对于除 ‘#’ 以外的每个输入字符，返回前 3 个历史热门句子，这些句子的前缀
// 与已经输入的句子的部分相同。
// 下面是详细规则：
//  1.一条句子的热度定义为历史上用户输入这个句子的总次数。
//  2.返回前 3 的句子需要按照热度从高到低排序（第一个是最热门的). 如果有多条热度相同的句子，请按照 ASCII 码的
//    顺序输出（ASCII 码越小排名越前）。
//  3.如果满足条件的句子个数少于 3，将它们全部输出。
//  4.如果输入了特殊字符，意味着句子结束了，请返回一个空集合。
// 实现 AutocompleteSystem 类：
//  1.AutocompleteSystem(String[] sentences, int[] times) 使用数组 sentences 和 times 对对象进行初始化。
//  2.List<String> input(char c) 表示用户输入了字符 c。
//    1.如果 c == '#'，则返回空数组[] ，并将输入的语句存储在系统中。
//    2.否则，返回前 3 个历史热门句子，这些句子的前缀与已经输入的句子的部分相同。如果少于 3 个匹配项，则全部返回。

// 注意
// n == sentences.length   n == times.length
// 1 <= n <= 100
// 1 <= sentences[i].length <= 100
// 1 <= times[i] <= 50
// c是小写英文字母，'#', 或空格' '
// 每个被测试的句子将是一个以字符 '#'结尾的字符 c 序列。
// 每个被测试的句子的长度范围为 [1,200]
// 每个输入句子中的单词用单个空格隔开。
// input最多被调用 5000 次

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class AutoComplete {

    // AutoComplete的成员
    // ------------------------------------------------------------------------------------------
    public final int TOP = 3;  // 用于限制收集答案的数量
    public final TrieNode root = new TrieNode(null, "");
    // 我们的初衷是想让每个前缀树结点内部都携带一张有序表，里面记录着通过自己结点的所有句子对应的频次。
    // 但这里我们选择外挂的方式，做成全局变量.
    // 比如前缀树有一个字符f，那么它对应的表可能是这样的：
    // (cfd, 7)  (aeef, 5)  (f, 3)
    // 我的意思是想说：任何一个前缀树结点对应的这个有序表存储的是只要经过了f这个结点的句子，就都会存储，
    // 而不是存储根到f这一段的前缀，或者f开始到结尾这一段的后缀。
    // 只要经过了f，就都会记下来，并且记的是完整的句子
    public HashMap<TrieNode, TreeSet<Record>> rank = new HashMap<>();
    public HashMap<String, Record> count = new HashMap<>();
    // path表示从根到当前结点已经走过的路径，对应的是已经键入的字符  cur表示来到的当前前缀树结点
    // 这两个变量是配套使用的。举个例子：
    // 当我键入第一个字符a时，我的备用推荐列表里根据前缀树可能会保持这样一个列表：(acf, 8) (abd, 6) (afg, 5)
    // 当键入第二个字符b的时候，推荐列表并不是直接凭空生成的，而是要依据之前的路径a的列表生成。
    // 于是ab的列表：剔除了acf和afg两条记录，有可能新添上(abd, 6) (abr, 4) (abz, 4)
    // 我想表达的意思是，我们从开头到目前键入的字符所生成的列表，要保留下来，供后面新的字符进来时参考。
    public String path;
    public TrieNode cur;

    public AutoComplete(String[] sentences, int[] times) {
        int N = sentences.length;
        path = "";
        cur = root;
        for (int i = 0; i < N; i++) {
            String sent = sentences[i];
            int time = times[i];
            // 我们一开始默认某个句子sentences[i]的频次是times[i] - 1，为什么呢？
            // 因为我们还要留出来一次真真切切地把它输入一遍，就是下面的循环input，input中句子结束时会让
            // 词频+1，使得最终次数等于times[i]
            // 为什么要单独留一次给input呢？因为我们想通过input，这样才能在前缀树上建立数据结构
            Record r = new Record(sent, time - 1);
            count.put(sent, r);
            char[] chs = sent.toCharArray();
            for (char c : chs)
                input(c);
            input('#');
        }
    }



    // 当前要键入的字符是c，我们需要根据之前键入的字符串留下的历史作为参考，
    // 所以 path和cur都做成了类变量  每次调用input方法不仅会参考path和cur，并且执行完后还会对
    // path和cur及时更新。
    // 上游调用input时，是已经将该句子已经出现的次数加在count表中了，所以进入input时，count是有东西的
    public List<String> input(char c) {
        ArrayList<String> res = new ArrayList<>();
        if (c != '#') {
            path += c;
            int route = findRoute(c);
            if (cur.nexts[route] == null)
                cur.nexts[route] = new TrieNode(cur, path);
            cur = cur.nexts[route];
            if (!rank.containsKey(cur))
                rank.put(cur, new TreeSet<>());
            int k = 0;
            // 如果cur本身就有了有序表，说明是初始化是给的一批数据初始化的。
            // 所以，既然cur本就有的话，那就直接把答案收集起来
            for (Record re : rank.get(cur)) {
                if (k == TOP)
                    break;
                res.add(re.word);
                k++;
            }
        }

        // 如果c=='#' 说明键入了终止符  并且path != ""  说明不是只有终止符  前面是有有效字符串的
        // 这样的话 xxxx#  我们就需要把这个字符串xxxx在前缀树中的词频+1   因为我搜索了一次这个
        // 如果 path == ""  说明前面任何字符都没有键入  直接输入的是终止符，这样是没有意义的，我们
        // 任何操作都不做
        if (c == '#' && !path.equals("")) {
            if (!count.containsKey(path))
                count.put(path, new Record(path, 0)); // 先把词频设置为0，后续再修改
            // 先把旧的取出来
            Record oldOne = count.get(path);
            // 再造一个新的，词频+1
            Record newOne = new Record(path, oldOne.times + 1);
            // 这里就体现了为什么在前缀树结点里设置parent的用处了
            // 我们让cur结点逐步往上移，这些路径必然是path的，所以就是说每个回溯的结点都需要修改
            // 结点自带的有序表，因为沿途每个结点都是path这个句子中的字符
            // 如何修改呢？因为某个记录的词频变化了，而系统的有序表没办法动态更新，所以我们采取删除旧的
            // 在加入新的  这样的操作来完成动态更新。当然也可以用有序表的改写来实现，但是有点大材小用了。
            while (cur != root) {
                rank.get(cur).remove(oldOne);
                rank.get(cur).add(newOne);
                cur = cur.parent;
            }
            // 此时cur又来到了root结点，因为只有键入'#'终止符，才会有这样的操作，于是path也需要重置了
            // 因为下一个句子的第一个字符键入时是没有历史包袱的。
            count.put(path, newOne);
            path = "";
        }
        return res;
    }



    // 找到c应该去nexts的哪里  调用的该方法的语境是：我当前在cur这个结点，我的后继是c字符，我需要计算
    // c应该在我的nexts数组的哪个索引处
    private int findRoute(char c) {
        // ' '放0位置  a～z对应1～26    '`'字符是ASCII码中'a'的前一个字符
        return c == ' ' ? 0 : (c - '`');
    }

    // ------------------------------------------------------------------------------------------


    // 下面两个方法是要完成的

    // 读完题目后应该会想到是要用前缀树做的吧。但是这里我们还得给前缀树增加些属性帮助我们解题。
    // 前缀树结点类
    public static class TrieNode {
        public TrieNode parent; // 每一个结点都能向上找到自己的父结点
        public String path;  // 从根结点到自己，走过的路径所代表的字符串
        public TrieNode[] nexts; // 后继

        public TrieNode(TrieNode parent, String path) {
            this.parent = parent;
            this.path = path;
            // 因为除了26个字母还有一个空格' '
            // 默认让 ' ' 放在0位置。a～z放在1～26
            nexts = new TrieNode[27];
        }
    }

    // 这个Record就是外包了一层的类。其比较规则是：出现次数相等的情况下按照ASCII码从小到大排序，
    // 不等的情况下，按词频从大到小排序
    public static class Record implements Comparable<Record> {
        public String word;
        public int times;

        public Record(String word, int times) {
            this.word = word;
            this.times = times;
        }

        @Override
        public int compareTo(Record o) {
            return times != o.times ? o.times - times : word.compareTo(o.word);
        }
    }


    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        System.out.println(set.remove(3));
    }
}
