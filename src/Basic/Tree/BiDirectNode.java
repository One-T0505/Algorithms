package Basic.Tree;

public class BiDirectNode {
    public int val;
    public BiDirectNode left;
    public BiDirectNode right;
    public BiDirectNode parent;

    public BiDirectNode(int val) {
        this.val = val;
    }

    public BiDirectNode(int val, BiDirectNode left, BiDirectNode right, BiDirectNode parent) {
        this.val = val;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
