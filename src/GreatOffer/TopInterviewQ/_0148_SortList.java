package GreatOffer.TopInterviewQ;


// 给你链表的头结点 head ，请将其按 升序 排列并返回排序后的链表。
// 要求时间复杂度为O(NlogN)  额外空间复杂度O(1)

import utils.SingleNode;

public class _0148_SortList {

    // 该算法的时间复杂度限制了只能从堆排序、快排、归并里面选择，堆排序和快排都做不到，只能用归并排序，但是只能
    // 使用非递归版的归并，如果使用递归的归并，那么额外空间复杂度为O(logN)

    public SingleNode sortList(SingleNode head) {
        int N = 0;
        SingleNode cur = head;
        // 计算长度
        while (cur != null) {
            N++;
            cur = cur.next;
        }
        SingleNode h = head;
        // 每一次合并组的开头
        SingleNode teamFirst = head;
        // 上一个已经合并好的组的结尾
        SingleNode pre = null;

        // 每次合并的组的长度加倍
        for (int len = 1; len < N; len <<= 1) {
            while (teamFirst != null) {
                // 其实 hthtn 方法返回三个参数即可。但是我们返回了5个，只是想说可以利用这种方式抓取我们想要的信息
                SingleNode[] group = hthtn(teamFirst, len);
                SingleNode[] mhmt = merge(group[0], group[1], group[2], group[3]);

                if (h == teamFirst) {
                    h = mhmt[0];
                    pre = mhmt[1];
                } else {
                    pre.next = mhmt[0];
                    pre = mhmt[1];
                }
                teamFirst = group[4];
            }
            teamFirst = h;
            pre = null;
        }
        return h;
    }

    // 该方法从teamFirst处向后找出符合len要求的一组可合并的元素
    // 返回：左部分的头 ls   左部分的尾  le
    //      右部分的头 rs   右部分的尾  re
    //      下一组的应该从哪找
    // 比如： 4->2->6->1->3->8->0->7->9    teamFirst现在指向了4，len==4，那么4～1是左部分，3～7是右部分，
    // 这两部分刚好凑成一个可合并的组，那么此时hthtn方法返回：(4, 1, 3, 7, 9)  9是作为寻找下一组的起始位置
    private SingleNode[] hthtn(SingleNode teamFirst, int len) {
        SingleNode ls = teamFirst;
        SingleNode le = teamFirst;
        SingleNode rs = null;
        SingleNode re = null;
        SingleNode next = null;
        int pass = 0;

        while (teamFirst != null) {
            pass++;
            if (pass <= len)
                le = teamFirst;
            if (pass == len + 1)
                rs = teamFirst;
            if (pass > len)
                re = teamFirst;
            if (pass == (len << 1))
                break;
            teamFirst = teamFirst.next;
        }

        le.next = null;
        if (re != null) {
            next = re.next;
            re.next = null;
        }

        return new SingleNode[]{ls, le, rs, re, next};
    }

    // 传入了两个部分的开始和结尾，按照归并排序的合并操作照做，返回整个组排好序的新头和新尾
    private SingleNode[] merge(SingleNode ls, SingleNode le, SingleNode rs, SingleNode re) {
        if (rs == null)  // 只有左部分
            return new SingleNode[]{ls, le};
        SingleNode head = null;
        SingleNode pre = null;
        SingleNode cur = null;
        SingleNode tail = null;

        while (ls != le.next && rs != re.next) {
            if (ls.val <= rs.val) {
                cur = ls;
                ls = ls.next;
            } else {
                cur = rs;
                rs = rs.next;
            }

            if (pre == null) {
                head = cur;
                pre = cur;
            } else {
                pre.next = cur;
                pre = cur;
            }
        }

        if (ls != le.next) {
            while (ls != le.next) {
                pre.next = ls;
                pre = ls;
                tail = ls;
                ls = ls.next;
            }
        } else {
            while (rs != re.next) {
                pre.next = rs;
                pre = rs;
                tail = rs;
                rs = rs.next;
            }
        }
        return new SingleNode[]{head, tail};
    }

}
