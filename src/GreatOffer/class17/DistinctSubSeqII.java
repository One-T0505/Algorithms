package class17;

import java.util.HashMap;
import java.util.HashSet;

// leetCode940
// 给定一个字符串Str，返回Str的所有非空子序列中有多少不同的字面值。

public class DistinctSubSeqII {


    // 暴力递归方法
    public static int distinctSubsequence(String s) {
        if (s == null)
            return 0;
        if (s.length() < 2)
            return s.length();
        HashSet<String> res = new HashSet<>();
        char[] chs = s.toCharArray();
        int N = chs.length;
        f(0, "", chs, res);
        return res.size() - 1;  // 减去那个空串
    }

    private static void f(int i, String pre, char[] chars, HashSet<String> res) {
        if (i == chars.length) {
            res.add(pre);
        } else {
            f(i + 1, pre + chars[i], chars, res);
            f(i + 1, pre, chars, res);
        }
    }
    // --------------------------------------------------------------------------------------------


    // 这道题目的难点在于如何快速去重。如果是递归所有路径最后通过集合来去重，那就不可能通过。去重必须在运行
    // 过程中进行，这样才能剪枝。下面讲的字符串在运算过程中去重的方法是非常重要的一种方法！！！以实例方式讲解
    //
    // abab
    //
    // 当还没开始遍历时，我们默认有一个空集合 {}
    // 每当来到一个新字符时，我们需要生成当前所有可能性的集合，这些集合由两部分组成。当来到第一个a时，我们上一步原有
    // 的集合全部复制一份，那就是 {}，这就是第一部分，即上一步的复刻;  然后再将上一步的所有集合每一个都加入此时的a形
    // 成一批新的集合。因为上一步只有 {}, 所以加入a变成 {a}， 这就是第二部分; 于是 a 的集合就是：{} {a}
    // 这表示的是从头到当前位置所有可能的子序列。

    // 当来到b时也是一样：先将上一步的集合全部复制一份 {} {a}；再将b添加到上一步所有的集合：{b} {ab};
    // 这样就形成了b的全体集合：{} {a} {b} {ab}

    // 当来到第二个a的时候，也是一样：先复制 {} {a} {b} {ab}; 再添加 {a} {aa} {ba} {aba}
    // 生成a的集合：{} {a} {b} {ab} {a} {aa} {ba} {aba}    我们发现有重复的集合！！！ {a} 是重复的，所以移除。
    // 生成a的集合：{} {b} {ab} {a} {aa} {ba} {aba}

    // 当来到最后一个b的时候，生成b的全体集合：
    // {} {b} {ab} {a} {aa} {ba} {aba}     {b} {bb} {abb} {ab} {aab} {bab} {abab}
    // 也有重复！！！ 重复了 {b} {ab}

    // 我们可以总结一下一般规律：当遍历的字符是之前没出现过的，就不会产生重复集合，正常推算就可以了。当前字符如果是之前
    // 出现过的，那么必然会有重复集合。那么重合多少个呢？ 上一次出现该字符时，其全体集合中以该字符结尾的集合都是重复的。
    // 就比如第二个a出现的时候，我们需要找到上一个a出现时其全体集合：{} {a}   重合的是其中以a结尾的集合 {a}
    // 所以我们需要提前记录这些信息。

    // 简单证明一下。 ....x.........x   假设x出现了两次，其余位置都没出现过x。
    // 那么第一个x的上一步所有的全体集合简称为p1。那么第一个x的全体集合就是 p1 + (p1<x)
    // 这表示第一个x的全体集合是由 p1的全体集合 + p1的全体集合添加x 两部分组成的。假设第二个x的上一步的全体集合为p2，
    // 那么p2必然会一步一步继承第一个x的全体集合，外加一些生成的，但是不管怎样，之前的那些老集合一定依然存在。所以，
    // p2中也存在 p1 + (p1<x)  和其他的，我们先不管其他的，因为那些不可能产生重复。当来到第二个x的时候，我们也会继承
    // p1 + (p1<x)   当我们决定将p1这部分后面都添加x的时候，就产生重合了，因为(p1<x)在第一个x的时候就生成过了，
    // 所以重合的数量就是 (p1<x)   也就是上一次出现x的时候，集合中以x结尾的集合。

    public static int distinctSubSeqII(String s) {
        if (s == null)
            return 0;
        if (s.length() < 2)
            return s.length();
        char[] chs = s.toCharArray();
        int N = chs.length;
        int all = 1; // 每一步的全体集合数量，当还没遍历时就已经有了一个空集合，所以初始化为1。
        // key是某个字符，value是以key这个字符结尾的集合数量
        HashMap<Character, Integer> dp = new HashMap<>();
        for (char c : chs) {
            int newAdd = all;  // 将上一步all中的每个集合都添加当前元素，就是新生成的那批集合，数量也为all
            // 我们要来确定当前全体集合是多少了。首先是上一步的全体继承all，然后是新加的那部分newAdd，最后再去重
            // 如果当前字符时出现过的，那么dp中肯定有记录，那么减去那部分的数量即可；如果当前字符是首次出现，那就减0
            int curAll = all + newAdd - (dp.getOrDefault(c, 0));
            // 更新当前全体集合的数量
            all = curAll; // 更新当前全体集合的数量
            // 更新dp。以当前字符结尾的集合数量至少是newAdd，因为这里每一个都是在集合末尾添加当前字符。
            // 重合的那部分已经剪掉了
            dp.put(c, newAdd);
        }
        return all - 1;  // 因为题目说空集不算
    }

    public static void main(String[] args) {
        String s = "pcrd";
        System.out.println(distinctSubsequence(s));
    }
}
