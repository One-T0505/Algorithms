package Basic.Tree.SegmentTree;

// 先去学习普通线段树的思想。再来看这个动态开点线段树
// 普通线段树的弊端
// 假如范围很大，给到了10^8，那么我们一开始初始化时就要开辟几个 4*10^8 这么大的数组，不管用不用，都得先把这么
// 大的空间先开辟了再说。其次，如果后续的操作仅仅在一个很小的范围内，那么剩下很大的空间的使用率并不高，所以还造成了
// 空间浪费。如果只是在[1,1000]这个范围上频繁操作，那一开始开辟那么大的空间很没必要。

// 动态开点线段树就是为了解决这个问题，它不会一开始就把所有空间都开辟好，一定是用到了才会动态去生成对应范围的区间，这样
// 就很省空间。
// 动态开点线段树支持  范围查询 范围更新 范围累加

public class DynamicSegmentTree {

    public Node root; // 线段树根结点
    public int N;

    public DynamicSegmentTree(int n) {
        root = new Node();
        N = n;
    }


    // 在[L,R]上加C
    public void add(int L, int R, int C){
        add(L, R, C, 1, N, root);
    }


    private void add(int L, int R, int C, int l, int r, Node cur) {
        if (L <= l && R >= r) {
            cur.sum += (r - l + 1) * C;
            cur.lazy += C;
            return;
        }
        int mid = l + ((r - l) >> 1);
        pushDown(cur, mid - l + 1, r - mid);
        if (L <= mid)
            add(L, R, C, l, mid, cur.left);
        if (R > mid)
            add(L, R, C, mid + 1, r, cur.right);
        pushUp(cur);
    }



    public void update(int L, int R, int C){
        update(L, R, C, 1, N, root);
    }

    private void update(int L, int R, int C, int l, int r, Node cur) {
        if (L <= l && R >= r){
            cur.lazy = 0;
            cur.change = C;
            cur.sum = (r - l + 1) * C;
            cur.effect = true;
            return;
        }
        int mid = l + ((r - l) >> 1);
        pushDown(cur, mid - l + 1, r - mid);
        if (L <= mid)
            update(L, R, C, l, mid, cur.left);
        if (R > mid)
            update(L, R, C, mid + 1, r, cur.right);
        pushUp(cur);
    }


    public int query(int L, int R){
        return query(L, R, 1, N, root);
    }

    private int query(int L, int R, int l, int r, Node cur) {
        if (cur == null)
            return 0;
        if (L <= l && R >= r)
            return cur.sum;
        int mid = l + ((r - l) >> 1);
        pushDown(cur, mid - l + 1, r - mid);
        int res = 0;
        if (L <= mid)
            res += query(L, R, l, mid, cur.left);
        if (R > mid)
            res += query(L, R, mid + 1, r, cur.right);
        return res;
    }


    private void pushUp(Node cur) {
        cur.sum = (cur.left == null ? 0 : cur.left.sum) + (cur.right == null ? 0 : cur.right.sum);
    }

    private void pushDown(Node cur, int ln, int rn) {
        if (cur.left == null)
            cur.left = new Node();
        if (cur.right == null)
            cur.right = new Node();
        if (cur.effect){
            cur.left.effect = true;
            cur.left.change = cur.change;
            cur.left.lazy = 0;
            cur.left.sum = ln * cur.change;
            cur.right.effect = true;
            cur.right.change = cur.change;
            cur.right.lazy = 0;
            cur.right.sum = rn * cur.change;
            cur.effect = false;
        }
        if (cur.lazy != 0){
            cur.left.lazy += cur.lazy;
            cur.left.sum += cur.lazy * ln;
            cur.right.lazy += cur.lazy;
            cur.right.sum += cur.lazy * rn;
            cur.lazy = 0;
        }
    }

}

// 这个就是动态开点线段树用到的结点，完全就是一个二叉树的结点。只不过里面新增了线段树结点特有的属性。
// 普通线段树都是用很大的数组去记录lazy、change、effect的信息。现在我们把这些信息都分摊给了结点。
// 一个结点就表示一个范围，至于表示什么范围，就不用记录在结点内部了，上游调用时会显示地告知。
class Node {
    public int sum;
    public int lazy;
    public int change;
    public boolean effect;
    public Node left;
    public Node right;
}
