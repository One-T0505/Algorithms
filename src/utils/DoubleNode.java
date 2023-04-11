package utils;

/**
 * ymy
 * 2023/3/15 - 11 : 12
 **/
public class DoubleNode {

    public int val;
    public DoubleNode pre;
    public DoubleNode next;

    public DoubleNode(){};

    public DoubleNode(int v){
        val = v;
    }

    public DoubleNode(int v, DoubleNode p, DoubleNode n){
        val = v;
        pre = p;
        next = n;
    }
}
