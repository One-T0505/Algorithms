package utils;

/**
 * ymy
 * 2023/3/15 - 11 : 11
 **/
public class SingleNode {

    public int val;

    public SingleNode next;

    public SingleNode(){};

    public SingleNode(int v){
        val = v;
    }

    public SingleNode(int v, SingleNode n){
        val = v;
        next = n;
    }

}
