package Basic.SystemDesign;

// leetCode588
// 设计一个内存文件系统，模拟以下功能：
// 实现文件系统类:
//  1. FileSystem() 初始化系统对象
//  2. List<String> ls(String path)
//     如果 path 是一个文件路径，则返回一个仅包含该文件名称的列表。
//     如果 path 是一个目录路径，则返回该目录中文件和 目录名 的列表。
//     答案应该 按字典顺序 排列。
//  3. void mkdir(String path)
//     根据给定的路径创建一个新目录。给定的目录路径不存在。如果路径中的中间目录不存在，您也应该创建它们。
//  4. void addContentToFile(String filePath, String content)
//     如果 filePath 不存在，则创建包含给定内容content的文件。
//     如果 filePath 已经存在，将给定的内容content附加到原始内容。
//  5. String readContentFromFile(String filePath) 返回 filePath下的文件内容。

// 注意
// 1 <= path.length, filePath.length <= 100
// path 和 filePath 都是绝对路径，除非是根目录 ‘/’ 自身，其他路径都是以 ‘/’ 开头且不以 ‘/’ 结束。
// 你可以假定所有操作的参数都是有效的，即用户不会获取不存在文件的内容，或者获取不存在文件夹和文件的列表。
// 你可以假定所有文件夹名字和文件名字都只包含小写字母，且同一文件夹下不会有相同名字的文件夹或文件。
// 1 <= content.length <= 50
// ls, mkdir, addContentToFile, and readContentFromFile 最多被调用 300 次

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class FileSystem {

    // 只需要用到前缀树即可。
    public Node root;

    public FileSystem() {
        root = new Node("");
    }


    // 下面是题目给的要实现的方法

    public List<String> ls(String path) {
        List<String> res = new ArrayList<>();
        Node cur = root;
        String[] parts = path.split("/");
        int N = parts.length;
        for (int i = 1; i < N; i++) {
            if (!cur.nexts.containsKey(parts[i]))
                return res;
            cur = cur.nexts.get(parts[i]);
        }
        // cur结束了！来到path最后的节点，该返回了
        // ls a/b/c  cur 来到c目录
        // 如果c是目录，那么就要返回c下面所有的东西！
        // 如果c是文件，那么就只返回c
        if (cur.content == null)
            res.addAll(cur.nexts.keySet());
        else
            res.add(cur.name);

        return res;
    }

    // 这里统一讲一下下面所有方法共同存在的问题：当给定一个路径，我们通过split方法将其分开后，
    // 遍历的时候i都是从1位置开始的，为什么不是从0位置开始呢？
    // 因为系统的split方法导致的。如果给定的路径是： /a/b/c  那么系统会将其分为  "" a  b  c
    // 也就是说，碰到第一个 "/" 时，会默认其前面为第一个部分，所以才多了一个空串出来，
    // 所以遍历的时候都是从1位置开始的。

    // path是绝对路径
    public void mkdir(String path) {
        String[] parts = path.split("/");
        int N = parts.length;
        Node cur = root;
        for (int i = 1; i < N; i++) {
            if (!cur.nexts.containsKey(parts[i]))
                cur.nexts.put(parts[i], new Node(parts[i]));
            cur = cur.nexts.get(parts[i]);
        }
    }


    // 给的文件名肯定是合法的，所以最后一级必然是文件名
    public void addContentToFile(String filePath, String content) {
        Node cur = root;
        String[] parts = filePath.split("/");
        int N = parts.length;
        for (int i = 1; i < N - 1; i++) { // parts[N-1]必然是文件名，所以要到N-2位置的目录
            if (!cur.nexts.containsKey(parts[i]))
                cur.nexts.put(parts[i], new Node(parts[i]));
            cur = cur.nexts.get(parts[i]);
        }
        // cur来到了N-2的位置，而N-1就是文件了
        // 如果没有该文件。就先把文件建起来
        if (!cur.nexts.containsKey(parts[N - 1]))
            cur.nexts.put(parts[N - 1], new Node(parts[N - 1], ""));
        // 追加内容
        cur.nexts.get(parts[N - 1]).content.append(content);
    }


    public String readContentFromFile(String filePath) {
        Node cur = root;
        String[] parts = filePath.split("/");
        int N = parts.length;
        for (int i = 1; i < N; i++) {
            if (!cur.nexts.containsKey(parts[i]))
                cur.nexts.put(parts[i], new Node(parts[i]));
            cur = cur.nexts.get(parts[i]);
        }
        return cur.content.toString();
    }


    // 前缀树结点
    public static class Node {
        public String name; // 当前文件名或目录名

        // content == null 说明这个结点是目录结点，不能有内容
        // content != null 说明这个结点是文件结点，可以直接往里面添加内容
        public StringBuilder content;

        // nexts表示当前结点的下级目录；因为ls操作要求按字典序返回下级目录所有东西，使用有序表
        // key为String类型，我这里没有传入比较规则，所以默认按照String的字典序排列
        public TreeMap<String, Node> nexts;

        // 构造方法
        public Node(String name) { // 构造目录
            this.name = name;
            content = null;
            nexts = new TreeMap<>();
        }


        public Node(String name, String content) { // 构造文件
            this.name = name;
            this.content = new StringBuilder(content);
            nexts = new TreeMap<>();
        }
    }


    public static void main(String[] args) {
        String a = "/a/b/c/";
        String[] split = a.split("/");
        for (String p : split)
            System.out.println(p);
        System.out.println(split[0]);
        System.out.println("开头");
        System.out.println(split.length);
    }
}
