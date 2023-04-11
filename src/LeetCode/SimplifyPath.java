package LeetCode;

import java.util.Stack;

/**
 * ymy
 * 2023/3/25 - 16 : 23
 **/


// leetCode71
// 给你一个字符串 path ，表示指向某一文件或目录的 Unix 风格的绝对路径 （以 '/' 开头），请你将其转化为更
// 加简洁的规范路径。在 Unix 风格的文件系统中，一个点（.）表示当前目录本身；此外，两个点 （..） 表示将目
// 录切换到上一级（指向父目录）；两者都可以是复杂相对路径的组成部分。任意多个连续的斜杠（即，'//'）都被视
// 为单个斜杠 '/' 。 对于此问题，任何其他格式的点（例如，'...'）均被视为 文件/目录名称。
// 请注意，返回的 规范路径 必须遵循下述格式：
//  1.始终以斜杠 '/' 开头。
//  2.两个目录名之间必须只有一个斜杠 '/' 。
//  3.最后一个目录名（如果存在）不能 以 '/' 结尾。
//  4.此外，路径仅包含从根目录到目标文件或目录的路径上的目录（即，不含 '.' 或 '..'）。
// 返回简化后得到的规范路径 。

// 1 <= path.length <= 3000
// path 由英文字母，数字，'.'，'/' 或 '_' 组成。
// path 是一个有效的 Unix 风格绝对路径。

public class SimplifyPath {

    // 建议去官方看一下给的几个例子 便于更好地理解题意
    // 根据题目给的提示，已经明确告知给的路径是有效的，所以我们不用对有效性检查，我们只需要简化即可。
    // 所以无形中告知了很多信息，比如开头必是 '/'
    // 其实也就是包含三种可以简化的情况：
    //  1. 多个 /    2. . 当前目录   3.  .. 上一级目录
    public static String simplifyPath(String path) {
        if (path == null || path.length() < 2)
            return path;
        char[] chs = path.toCharArray();
        int N = chs.length;
        // 用数组来模拟栈
        Stack<String> stack = new Stack<>();
        int p = 0;
        StringBuilder name = new StringBuilder();
        for (int i = 1; i < N; i++) { // 0位置必然是 /
            if (chs[i] == '/'){
                if (chs[i - 1] == '/')
                    continue;
                 // 如果你只是单一的斜杠，那么就将上一次得到的目录名加入到栈里
                if (name.toString().equals("..")){
                    if (!stack.isEmpty())
                        stack.pop();
                } else if (!name.toString().equals(".")) {
                    stack.push(name.toString());
                }
                name.delete(0, name.length()); // 再把内容清空
            } else {
                name.append(chs[i]);
            }
        }
        // 经过测试才知道，原来题目给的例子中不一定都是以 "/" 结尾的
        if (name.length() > 0){
            if (name.toString().equals("..")){
                if (!stack.isEmpty())
                    stack.pop();
            } else if (!name.toString().equals(".")){ // 还有内容
                stack.push(name.toString());
            }

        }

        return "/" + String.join("/", stack);
    }


    public static void main(String[] args) {
        String path = "/a//b////c/d//././/..";
        System.out.println(simplifyPath(path));
    }
}
