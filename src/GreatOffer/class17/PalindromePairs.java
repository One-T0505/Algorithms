package class17;

// leetCode336
// 给定一组互不相同的单词， 找出所有不同的索引对 (i, j)，使得列表中的两个单词， words[i] + words[j]，
// 可拼接成回文串。

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PalindromePairs {

    // 先思考一下暴力解法：
    // 我们对于每个字符串，都让他去和剩下的N-1个字符串去拼接，因为 A="abc" B="ba" 只有A+B可以形成回文，而
    // B+A不能形成回文，所以我们不能漏掉组合。然后拼完之后再去验证是否为回文，假设字符串平均长度为k，那么
    // 时间复杂度为 O(N^2 * k) 验证回文用 Manacher 才能达到O(k)

    // 另一种解法：
    // 先把所有字符串都添加到集合中。对于每个字符串 假如A=="cbaa"
    //  1.第一步先遍历前缀 对于每个前缀 c cb cba cbaa 我们都需要判断前缀是否为回文，如果是，那么就将剩余部分逆序
    //    后，在集合中查找是否存在该字符串，如果存在那么将其拼在A的前面就可以形成回文。比如前缀 c 就是回文，剩余部
    //    分的逆序 aab 如果存在，那么将 aab 拼在 A 的前面就可以形成回文 aabcbaa
    //    第一步其实就是在做，当前字符串作为 (i,j) 序列对的后一个，能找到哪些元素组合
    //  2.第二步就是遍历字符串的每个后缀，然后判断后缀是否为回文，如果是回文，那么就将剩余部分逆序后，在集合中查找
    //    是否存在该字符串，如果存在那么将其拼在A的后面就可以形成回文。比如后缀 aa 是回文, 剩余部分逆序 bc 如果
    //    存在，那么将其拼在 A 的后面  cbaabc 就是回文。
    //    第二步其实就是在做，当前字符串作为 (i,j) 序列对的前一个，能找到哪些元素组合

    // 分析下时间复杂度，对于每个字符串，我们都需要遍历前缀后缀，检查是否为回文，假设字符串平均长度为k，
    // 那就是 O(N * k^3)  前缀就有k个，先判断是否为回文，这部分为O(k)  去查找剩余部分也是O(k) 所以查
    // 找代价就变成了O(k) 然后逆序和拼接的代价也是O(k)  整体就是 O(N * k^3)
    // 但是可以利用 Manacher 算法将其检查前缀代价变成 O(1)  所以 整体就是 O(N * k^2)
    // 根据题目数据规模，选择上述的两种方法中的一个即可。

    // 我们整体的大流程就是这样的，只不过在大流程中还需要注意下边界问题。

    public static List<List<Integer>> palindromePairs(String[] words) {
        // 每个字符串出现的位置
        HashMap<String, Integer> dp = new HashMap<>();
        int N = words.length;
        for (int i = 0; i < N; i++) {
            dp.put(words[i], i);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            res.addAll(findAll(words[i], i, dp));
        }
        return res;
    }


    // 找出所有能和 word 构成回文串的序列对
    private static List<List<Integer>> findAll(String word, int i, HashMap<String, Integer> dp) {
        List<List<Integer>> res = new ArrayList<>();
        String reverse = reverse(word);
        Integer rest = dp.get("");
        // 边界情况：字符串数组本身存在空串，并且自己不是空串，并且自己本身就是回文串，这种情况下，只需要将
        // 空串拼在前面或后面，都可以形成回文。
        if (rest != null && rest != i && word.equals(reverse)) {
            add(res, rest, i);
            add(res, i, rest);
        }
        // 利用manacher算法的回文半径数组，就可以用O(1)的时间判断某个前缀或后缀是否为回文
        int[] ra = manacher(word);
        int mid = ra.length >> 1;
        // 从1开始，因为回文半径第一个位置记录的是'#'的回文半径，无意义
        // p 其实就是以哪个位置为中心找回文半径。p < mid 因为让 p==mid，其实就是判断整个字符串是否为回文，这个我们
        // 上面已经判断过了边界，所以此时不考虑。
        // 那么如何判断前缀是否为回文呢？ 那就选定一个位置p，看他的回文半径长度是否等于从0到p
        for (int p = 1; p < mid; p++) {
            // 说明从0～2 * ra[p] - 1 这个前缀是回文的，并且该前缀不是全体字符串，也就是说后面还有剩
            // 这就是让 p < mid 的作用
            if (p + 1 == ra[p]) {
                // reverse是整体的逆序。以后每次需要部分的逆序直接截取就行，不用每次专门去逆序。
                // 经过 manacher 处理过的串中是有 '#' 的。ra 也是对预处理串处理的。
                //
                // # a # b # c # c # a #    a的回文半径为2，所以从0～2这个前缀是我们找到的回文
                // 3～10是剩余部分的，其实我们就是要将这剩余部分逆序，但是 ra 的数据都是添加了 '#' 的，
                // 所以需要变换处理。不管预处理串怎么样，只要你找到了一个前缀是回文，那么剩余部分的长度必然是
                // 偶数，并且剩余部分都是 先字母 后'#'
                // 剩余部分坐标是 3~10  直接除2  1～5 但是最后的5要-1，因为最后的字符是'#',
                // 所以1～4 实际上就是我们要截取的剩余部分，但是这是正序的坐标，
                // 还要转化成逆序的坐标，就变成了0~3  但是字符串subString方法是前闭后开，所以还要+1，
                // 变成了 0~4
                rest = dp.get(reverse.substring(0, mid - p));
                if (rest != null && rest != i)
                    add(res, rest, i);
            }
        }

        // 再来做后缀  看到没，唯独跳过了p==mid，因为如果p==mid，如果全局是回文，那么又会和上面的情况重复
        for (int p = mid + 1; p < ra.length; p++) {
            if (p + ra[p] == ra.length) {
                rest = dp.get(reverse.substring((mid << 1) - p));
                if (rest != null && rest != i)
                    add(res, i, rest);
            }
        }
        return res;
    }


    // Manacher 算法
    private static int[] manacher(String word) {
        char[] ex = expand(word);
        int N = ex.length;
        int[] r = new int[N];
        int R = -1, C = -1;
        for (int i = 0; i < N; i++) {
            r[i] = i <= R ? Math.min(R - i + 1, r[2 * C - i]) : 1;
            while (i - r[i] >= 0 && i + r[i] < N) {
                if (ex[i - r[i]] != ex[i + r[i]])
                    break;
                r[i]++;
            }
            if (i + r[i] - 1 > R) {
                R = i + r[i] - 1;
                C = i;
            }
        }
        return r;
    }


    private static char[] expand(String word) {
        char[] ex = new char[word.length() << 1 | 1];
        for (int i = 0; i < ex.length; i++) {
            ex[i] = (i & 1) == 0 ? '#' : word.charAt((i - 1) >> 1);
        }
        return ex;
    }

    private static void add(List<List<Integer>> res, int former, Integer latter) {
        List<Integer> path = new ArrayList<>();
        path.add(former);
        path.add(latter);
        res.add(path);
    }

    private static String reverse(String word) {
        char[] chs = word.toCharArray();
        int N = chs.length;
        int l = 0, r = N - 1;
        while (l < r) {
            char tmp = chs[l];
            chs[l++] = chs[r];
            chs[r--] = tmp;
        }
        return String.valueOf(chs);
    }


    public static void main(String[] args) {
        String ccbad = reverse("ccbad");
        System.out.println(ccbad);
    }
}
