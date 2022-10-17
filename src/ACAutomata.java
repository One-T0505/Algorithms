
// AC自动机
// 该模型应用场景是什么样的？假如有一篇很长的文章，然后有一个敏感词表单，请从这篇文章里找出包含了哪些敏感词。
// 即便是用KMP进行快速匹配，那也只能每次遍历整篇文章才能找到一种敏感词，KMP只适用于单一子串匹配，并且原串不能太大。
// AC自动机可以做到只需要遍历一遍就可以找到文章里包含的所有表单中的词。AC自动机需要有前缀树的基础。
//
// 我们现在的首要目的是熟悉AC自动机的机制，而对于原理先别管，先把流程搞熟再说。
//
// 一、首先要对所有敏感词建立前缀树
//   1.这里的结点新增加了String类型的end域，如果end为空，说明该结点不是任意一个字符串的结尾。如果end不为空，表示这个结点
//     是某个字符串的结尾，end的值就是这个字符串。比如加入了aer这个字符串，那么r的end域就等于aer。沿途结点的end域都为null。
//   2.其次，结点还新增了布尔型的logged域，这个域只对那些end域不为空的结点才有意义，表示以该字符串作为敏感词是否加入了答案中，
//     如果加入了那下次碰到了就别重复加了。
//   3.最重要的就是新加的这个fail域，每个结点都有。以何种次序设置fail很有讲究，我们需要按层逐一设置fail，所以需要用到队列。
//     并且每次弹出一个结点我们不是去设置该结点的fail，而是去设置该结点所有孩子的fail。
//     先别管fail的指向有何含义，只需要记住如何设置fail域即可。对于前缀树的根结点root，fail指向null；而对于root紧挨着的
//     下一层所有结点，fail指向root；对于剩下的结点，按照如下流程设置：
//         1>从队列中弹出一个结点cur，该结点的fail肯定是被其父亲从队列中弹出时就设置好了的，所以我们要关心的是当前结点的所有孩子。
//         2>对于每一个有的孩子编号index，cur的位置保持不变，让另一个变量tmp=cur.fail，接下来让tmp去游走
//         3> while(tmp != null){
//              如果tmp.next[index]不为空 就让cur.nexts[index].fail = tmp.nexts[index] 并break跳出while
//              否则 tmp = tmp.fail
//         }
//   4.建立好这棵前缀树后，所有结点的fail只可能有三种指向：只有根结点指向null; 根的所有孩子都指向根；剩下的所有结点要么
//     指向别的结点要么指向根，不可能为null。
//
// 二、现在大文章来了，建好的前缀树开始运转。举个实际的例子会比较具体。敏感词有：abcde bcde cde de，那么建出来的前缀树如下：
//
//                           ○---null
//                a|          b|        c|        d|
//                 ○   |--->   ○   |---> ○   |---> ○
//                b|   |      c|   |    d|   |    e|
//                 ○ --| |-->  ○ --| |-->○ --|     ●
//                c|     |    d|     |  e|         ↑
//                 ○ ----| |-->○ ----|   ● --------|
//                d|       |  e|         ↑
//                 ○ ------|   ● --------|
//                e|           ↑
//                 ● ----------|
// ●说明该结点是某个字符串的结尾;虚线表示的fail域，那些没画fail域的结点都是指向根的. 如果大文章是 gabcdefk.
// 那么从根开始，发现根没有到g的路，说明以g开头不可能有敏感词在树中；直接下一个来到a，根上有到a的路，那就顺着走，来到a时，
// cur==a，cur保持不变，让另一个变量follow==cur，然后让follow顺着fail域循环一周，follow现在等于cur发现cur.end为空，
// 说明当前结点不是某个词的结尾，那么follow来到其fail指向的根，那么说明循环一周了，但是并没有匹配到，于是继续匹配；来到大文章
// 中的b，而cur此时还在a，a有到b的路，此时cur来到b，然后再顺着b的fail域循环一周，也没找到一个可以记录的词。。。中间略过
// 此时cur来到了e，发现此时e.end不为空说明此时是某个词的结尾，并且发现end.logged==false，说明没有添加过该找到的词，于是
// 在答案中添加abcde,并把e的logged设为true，然后follow顺着fail来到第二条的e，发现e为结尾并且没被记录过，所以在答案中
// 添加bcde，并把logged设为true；再来到第三条的e，同样发现e为结尾并且没被记录过，所以在答案中添加cde，并把logged设为true；
// 又顺着fail来到第四条的e，同样发现e为结尾并且没被记录过，所以在答案中添加de，并把logged设为true；顺着fail来到根，循环了一周
// 就把能找到的敏感词全部收集到了。然后大文章中再来到下一个。因为此时cur还在第一条的e并且没有孩子为f，所以此时让cur直接回到根。
//
// 当遍历到大文章的e时，就把文章开头到当前的e可能包含的所有敏感词全部找出来了，所以效率很高。

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ACAutomata {

    // 前缀树结点
    public static class Node{
        // 如果一个node，end为空，说明该结点不是任意一个字符串的结尾
        // 如果end不为空，表示这个点是某个字符串的结尾，end的值就是这个字符串
        public String end;
        // 只有在上面的end变量不为空的时候，logged才有意义。表示，这个字符串之前有没有加入过答案
        public boolean logged;
        public Node fail;
        public Node[] nexts;   // 这里默认所有都是由小写英文字母组成的字符串，a-->0  b-->1  z-->25

        public Node() {
            logged = false;
            end = null;
            fail = null;
            nexts = new Node[26];
        }
    }

    public Node root;

    public ACAutomata() {
        root = new Node(); // 初始化node时fail默认指向null，作为根结点此时fail已经设置好了
    }

    // 向前缀树中插入一个字符串
    public void insert(String word){
        if (word == null)
            return;
        char[] chars = word.toCharArray();
        Node cur = root;
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            index = chars[i] - 'a';
            if (cur.nexts[index] == null)
                cur.nexts[index] = new Node();
            cur = cur.nexts[index];
        }
        cur.end = word;
    }

    // build表示统一设置前缀树中所有结点的fail域；说明所有该加入的词已经全部加入前缀树了
    public void build(){
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        Node cur = null;
        Node cFail = null;
        while (!queue.isEmpty()){
            cur = queue.poll(); // 弹出某个结点，我们要处理的是它所有的孩子
            for (int i = 0; i < 26; i++) { // 排查所有孩子
                if (cur.nexts[i] != null){ // 如果有i号儿子
                    // 我们采用的逻辑是：先全部指向root，再单独处理那些有其他指向的
                    cur.nexts[i].fail = root;
                    cFail = cur.fail;
                    while (cFail != null){ // 顺着fail循环一周去寻找 ==null 说明已经来到根结点的fail
                        if (cFail.nexts[i] != null){
                            cur.nexts[i].fail = cFail.nexts[i];
                            break;
                        }
                        cFail = cFail.fail;
                    }
                    queue.add(cur.nexts[i]);
                }
            }
        }
    }


    // 最关键的方法：拿着大文章和建好的前缀树来做多匹配
    public List<String> involvedWords(String content){
        char[] chars = content.toCharArray();
        Node cur = root;
        Node follow = null;
        int index = 0;
        List<String> res = new ArrayList<>();
        for (char c : chars) {
            index = c - 'a';
            // 如果当前字符在这条路上没配出来，就随着fail方向走向下条路径
            // 为啥要加一个cur != root 因为cur==root时再往fail走就到null了，我们的意思是最后最差也是回到根重新配
            while (cur.nexts[index] == null && cur != root)
                cur = cur.fail;
            // 跳出while时有两种可能
            cur = cur.nexts[index] == null ? root : cur.nexts[index];
            // 到这里时，cur就有两种情形：
            // 1) 现在来到的路径，是可以继续匹配的
            // 2) 现在来到的节点，就是前缀树的根节点
            follow = cur;
            while (follow != root) { // 如果是第2种情形，那么该while不会执行，不影响后续
                if (follow.logged)  // 如果已经加过，直接跳出，因为后续的循环肯定也走过
                    break;
                if (follow.end != null) {
                    res.add(follow.end);
                    follow.logged = true;
                }
                follow = follow.fail;
            }
        }
        return res;
    }


    public static void main(String[] args) {
        ACAutomata ac = new ACAutomata();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("abcdheks");
        // 设置fail指针
        ac.build();
        List<String> contains = ac.involvedWords("abcdhekskdjfafhasldkflskdjhwqaeruv");
        for (String word : contains) {
            System.out.println(word);
        }
    }
}
