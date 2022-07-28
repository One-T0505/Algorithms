package Tree;

import java.util.HashMap;

public class PrefixTree {
    public PrefixTreeNode root;

    public PrefixTree() {
        this.root = new PrefixTreeNode();
    }

    // 对一个字符串进行处理，将一个字符串加入前缀树
    public void insert(String word){
        if (word == null)
            return;
        char[] chars = word.toCharArray();
        PrefixTreeNode cur = this.root;
        cur.pass++;
        for (char aChar : chars) {
            int path = aChar - 'a';
            if (cur.nexts.get(path) == null) {
                cur.nexts.put(path, new PrefixTreeNode());
            }
            cur = cur.nexts.get(path);
            cur.pass++;
        }
        cur.end++;
    }

    // 查找word这个字符串在前缀树中出现了几次
    public int search(String word){
        if (word == null)
            return 0;
        char[] chars = word.toCharArray();
        PrefixTreeNode cur = this.root;
        for (char aChar : chars) {
            int path = aChar - 'a';
            if (cur.nexts.get(path) == null)
                return 0;
            cur = cur.nexts.get(path);
        }
        return cur.end;
    }

    // 在前缀树中查找以prefix为前缀的字符串个数
    public int matchPrefix(String prefix){
        if (prefix == null)
            return 0;
        char[] chars = prefix.toCharArray();
        PrefixTreeNode cur = this.root;
        for (char aChar : chars) {
            int path = aChar - 'a';
            if (cur.nexts.get(path) == null)
                return 0;
            cur = cur.nexts.get(path);
        }
        return cur.pass;
    }

    // 在前缀树中删除word
    public void delete(String word){
        if (search(word) != 0){
            char[] chars = word.toCharArray();
            PrefixTreeNode cur = this.root;
            cur.pass--;
            for(char item : chars){
                int path = item - 'a';
                // 如果某个结点的pass为0，说明该结点后继全部为空，直接让其指向null使其后面的所有都断链，JVM就会自动删除
                if (--cur.nexts.get(path).pass == 0){
                    cur.nexts.put(path, null);
                    return;
                }
                cur = cur.nexts.get(path);
            }
            cur.end--;
        }
    }


}

class PrefixTreeNode{
    public int pass; // 经过该结点的次数
    public int end;  // 以该结点为终点的字符串数量
    public HashMap<Integer, PrefixTreeNode> nexts;

    public PrefixTreeNode() {
        this.pass = 0;
        this.end = 0;
        this.nexts = new HashMap<>(); // 现在处理的是只包含小写26个字母的情况，0->a，以此类推.
    }
}
