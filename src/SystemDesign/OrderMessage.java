package SystemDesign;

// 已知一个消息流会不断地吐出整数1~N，但不一定按照顺序依次吐出。如果上次打印的序号为i，那么当i+1出现时，
// 请打印i+1及其之后接收过的并且连续的所有数，直到1~N全部接收并打印完。请设计这种接收并打印的结构。并且时
// 间复杂度要求：O(N)

// 举例：N=13，目标是将1～13顺序打印。此时收到3，2，不能打印，第三个数收到了1，可以打印出：1,2,3；接下来收到4，
//      直接打印4；现在收到7(不能打印，因为没等到5)，又收到8，9，6，5，此时可以一次性打印5,6,7,8,9; 此时收到
//      13，不能打印，因为在等待10，此时收到12，10，可以直接打印10；最后收到11，然后打印11，12，13。

// 这样一种结构适用于网络接收消息，一定要按序输出消息，即便后面的消息先到了，也不能输出，也得等顺序的信息到了才能一起输出。

import java.util.HashMap;

public class OrderMessage {

    // 思路：搭建这样一种数据结构MessageBox，里面有两张hashMap(headMap, tailMap)和一个变量waiting，
    //      waiting表示期待的序号；每次来一个消息时，比如(3, F)，把这个结点加入头表和尾表，然后判断尾表中是否存在
    //      key为2的结点，如果有，则把该结点的next指向3，此时3不可能再当头了，所以把头表中3的结点删除，并且2也不可能
    //      当尾了，所以把尾表中的2删除；再判断头表中是否存在key为4的结点，如果存在，则将自己3的next指向4，此时3也不
    //      可能当尾了，所以在尾表中删除3，同样地在头表中删除4；如果此时来的结点的key正好是waiting，那么经过上述
    //      操作后就可以直接打印。其实应该先判断来的结点的key是不是waiting，如果是，只需要向后连，不需要向前连。

    public static class Node {
        public String message;
        public Node next;

        public Node(String message) {
            this.message = message;
        }
    }

    public static class MessageBox {
        private int waiting;
        private HashMap<Integer, Node> headMap;
        private HashMap<Integer, Node> tailMap;

        public MessageBox() {
            waiting = 1;
            headMap = new HashMap<>();
            tailMap = new HashMap<>();

        }

        public void receive(int index, String message){
            if (index < 1)
                return;
            Node cur = new Node(message);
            headMap.put(index, cur);
            tailMap.put(index, cur);
            if (headMap.containsKey(index + 1)){
                cur.next = headMap.get(index + 1);
                tailMap.remove(index);
                headMap.remove(index + 1);
            }
            if (tailMap.containsKey(index - 1)){
                tailMap.get(index - 1).next = cur;
                tailMap.remove(index - 1);
                headMap.remove(index);
            }
            if (index == waiting)
                printMessage(headMap.get(waiting));
        }

        // 可以输出时一定是waiting等到了；那么将这组信息输出后，两张表的记录也要删除，并且修改waiting。
        private void printMessage(Node node) {
            headMap.remove(waiting);
            StringBuilder builder = new StringBuilder("");
            while (node != null){
                builder.append(node.message).append(" ");
                node = node.next;
                waiting++;
            }
            tailMap.remove(waiting - 1);
            System.out.println(builder);
            System.out.println();
        }
    }
}
