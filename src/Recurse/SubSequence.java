package Recurse;

import java.util.ArrayList;
import java.util.List;

public class SubSequence {
     // 1.打印一个字符串的所有子序列
    public static List<String> subSequence(String s){
        if (s == null || s.length() == 0)
            return null;
        String cur = "";
        char[] chars = s.toCharArray();
        List<String> res = new ArrayList<>();
        process1(chars, 0, res, cur);
        return res;
    }

    private static void process1(char[] chars, int index, List<String> res, String cur) {
        if (index == chars.length){
            res.add(cur);
        }else {
            String no = cur;
            process1(chars, index + 1, res, no); // 不选择chars[index]位置的字符
            String yes = cur + chars[index];
            process1(chars, index + 1, res, yes);
        }
    }

    // 2.打印一个字符串的所有全排列。eg：String s = abc 那么s的全排列就是：abc、acb、bac、bca、cab、cba
    public static List<String> permutation(String s){
        if (s == null || s.length() == 0)
            return null;
        char[] chars = s.toCharArray();
        List<String> res = new ArrayList<>();
        process2(chars, 0, res);
        return res;
    }

    // chars[0..index-1]已经做好决定了，不用管了。chars[index..]都有机会到index位置上
    // 如果index到了终止位置，chars当前的样子就是一种结果
    private static void process2(char[] chars, int index, List<String> res) {
        if (index == chars.length)
            res.add(String.valueOf(chars));
        for (int i = index; i < chars.length; i++) {
            // 调用两次swap才能使字符串变回原样
            swap(chars, i, index);
            process2(chars, i + 1, res);
            swap(chars, i, index);
        }
    }

    // 3.打印一个字符串的非重复的全排列。最简单的方法就是将上一个方法的ArrayList修改为HashSet，这样就会自动去重。
    //   但是这种方法在根本上是不能提高效率的，因为这样的方式同样是遍历了所有的可能性，然后再把所有结果去重。
    //   分支限界就是在递归时通过一些条件来直接不去遍历分支，这样才能从根本上提高效率。下面的方法就是利用分支限界的思想
    //   来实现找出一个字符串的所有非重复全排列。主函数和permutation一样，有区别的只是递归过程。
    public static void process3(char[] chars, int index, ArrayList<String> res){
        if (index == chars.length)
            res.add(String.valueOf(chars));
        boolean[] visit = new boolean[26]; // 默认字符串都是由小写英文字符组成
        for (int i = index; i < chars.length; i++) {
            if (!visit[chars[i] - 'a']){
                visit[chars[i] - 'a'] = true;
                swap(chars, i, index);
                process3(chars, i + 1, res);
                swap(chars, i, index);
            }
        }
    }

    private static void swap(char[] chars, int i, int j) {
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
    }
}
