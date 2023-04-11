package Basic.Recurse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class  SubSequence {
     // 1.打印一个字符串的所有子序列
    public static List<String> subSequence(String s){
        if (s == null || s.length() == 0)
            return null;
        String pre = "";
        char[] chars = s.toCharArray();
        List<String> res = new ArrayList<>();
        f(chars, 0, res, pre);
        return res;
    }

    // chars是固定参数，一直不变； index表示当前要决定chars[index]的字符是否要，而0..index-1已经做好决定了，
    // 并且其结果保存在了pre中
    private static void f(char[] chars, int index, List<String> res, String pre) {
        if (index == chars.length){
            res.add(pre);
        }else {
            // 这个递归子过程index变成index + 1了，说明index处已经做好决定了，我们发现pre这个参数没有变化，
            // 说明这个递归子过程数不要chars[index]处字符的递归
            f(chars, index + 1, res, pre); // 不选择chars[index]位置的字符
            String attend = pre + chars[index];
            f(chars, index + 1, res, attend);
        }
    }
    // ============================================================================================

    // 2.打印一个字符串的全部子序列，要求不要出现重复字面值的子序列。这个题目和上面的几乎一样，只需要把返回结果的类型
    //   变成HashSet即可
    public static HashSet<String> subSequenceNorepeat(String s){
        if (s == null || s.length() == 0)
            return null;
        String pre = "";
        char[] chars = s.toCharArray();
        HashSet<String> res = new HashSet<>();
        g(chars, 0, res, pre);
        return res;
    }

    private static void g(char[] chars, int index, HashSet<String> res, String pre) {
        if (index == chars.length){
            res.add(pre);
        }else {
            // 这个递归子过程index变成index + 1了，说明index处已经做好决定了，我们发现pre这个参数没有变化，
            // 说明这个递归子过程数不要chars[index]处字符的递归
            g(chars, index + 1, res, pre); // 不选择chars[index]位置的字符
            String attend = pre + chars[index];
            g(chars, index + 1, res, attend);
        }
    }
    // ============================================================================================


    // 3.打印一个字符串的所有全排列。eg：String s = abc 那么s的全排列就是：abc、acb、bac、bca、cab、cba
    public static List<String> permutationV1(String s){
        if (s == null || s.length() == 0)
            return null;
        List<String> res = new ArrayList<>();
        char[] chars = s.toCharArray();
        ArrayList<Character> rest = new ArrayList<>();
        for (char c : chars)
            rest.add(c);
        String pre = "";
        h(rest, pre, res);
        return res;
    }

    // chars[0..index-1]已经做好决定了，不用管了。chars[index..]都有机会到index位置上
    // 如果index到了终止位置，chars当前的样子就是一种结果
    private static void h(List<Character> rest, String pre, List<String> res) {
        if (rest.isEmpty())
            res.add(pre);
        else {
            for (int i = 0; i < rest.size(); i++){
                char cur = rest.get(i);
                rest.remove(i);
                h(rest, pre + cur, res);
                rest.add(i, cur);  // 恢复
            }
        }
    }

    // 这个问题还有另一种编写递归的方式。假如给定一个字符串abc，首先确定0位置可以放什么字符，他可以依次和后面的每个字符
    // 交换，尝试所有放在0位置的元素；然后再确定1位置放什么字符，此时1位置只能和自己后面的交换；前面的是已经决策好了的
    public static List<String> permutationV2(String src){
        if (src == null || src.length() == 0)
            return null;
        List<String> res = new ArrayList<>();
        char[] chars = src.toCharArray();
        y(chars, 0, res);
        return res;
    }

    // 从pos位置开始做决策，0..pos-1位置已经决策好了，并且结果就在chars中
    private static void y(char[] chars, int pos, List<String> res) {
        if (pos == chars.length)
            res.add(String.valueOf(chars));
        else {
            for (int i = pos; i < chars.length; i++) {
                swap(chars, pos, i);
                y(chars, pos + 1, res);
                swap(chars, pos, i);
            }
        }
    }
    // ============================================================================================


    // 4.打印一个字符串的非重复的全排列。最简单的方法就是将上一个方法的ArrayList修改为HashSet，这样就会自动去重。
    //   但是这种方法在根本上是不能提高效率的，因为这样的方式同样是遍历了所有的可能性，然后再把所有结果去重。
    //   分支限界就是在递归时通过一些条件来直接不去遍历分支，这样才能从根本上提高效率。下面的方法就是利用分支限界的思想
    //   来实现找出一个字符串的所有非重复全排列。主函数和permutation一样，有区别的只是递归过程。
    private static void t(char[] chars, int index, ArrayList<String> res){
        if (index == chars.length)
            res.add(String.valueOf(chars));
        boolean[] visit = new boolean[26]; // 默认字符串都是由小写英文字符组成
        for (int i = index; i < chars.length; i++) {
            if (!visit[chars[i] - 'a']){
                visit[chars[i] - 'a'] = true;
                swap(chars, i, index);
                t(chars, i + 1, res);
                swap(chars, i, index);
            }
        }
    }

    private static void swap(char[] chars, int i, int j) {
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
    }


    public static void main(String[] args) {
        System.out.println(subSequence("abcb"));
        System.out.println(subSequenceNorepeat("abcb"));
        System.out.println(permutationV1("abce"));
        System.out.println(permutationV2("abce"));
    }
}
