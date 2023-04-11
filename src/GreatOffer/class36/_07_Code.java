package GreatOffer.class36;

import java.util.HashMap;
import utils.SingleNode;

// 来自腾讯
// 给定一个单链表的头节点head,每个节点都有value(>0),给定一个正数m
// value % m 的值一样的节点算同类
// 请把所有的类根据单链表的方式重新连接好，返回每一类的头节点

public class _07_Code {

    // 主方法
    public static SingleNode[] classify(SingleNode h, int m) {
        HashMap<Integer, HT> dp = new HashMap<>();
        while (h != null) {
            if (!dp.containsKey(m % h.val))
                dp.put(m % h.val, new HT(h));
            else {
                HT ht = dp.get(m % h.val);
                ht.tail.next = h;
                ht.tail = h;
            }
            h = h.next;
        }
        SingleNode[] res = new SingleNode[dp.size()];
        int i = 0;
        for (HT ht : dp.values())
            res[i++] = ht.head;

        return res;
    }

    public static class HT {
        public SingleNode head;
        public SingleNode tail;

        public HT(SingleNode a) {
            head = a;
            tail = a;
        }
    }
}
