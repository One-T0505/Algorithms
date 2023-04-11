package Basic.LinkedList;

public class RandNode {
    public int val;
    public RandNode next;
    public RandNode rand;  // 这个指针随意指向一个结点，可以为null，可以是自己，也可以是任意一个结点，但是一旦确定下来就不可修改

    public RandNode(int val) {
        this.val = val;
    }

    public RandNode(int val, RandNode next, RandNode rand) {
        this.val = val;
        this.next = next;
        this.rand = rand;
    }
}
