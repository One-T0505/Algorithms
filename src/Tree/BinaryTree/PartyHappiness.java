package Tree.BinaryTree;

import java.util.ArrayList;
import java.util.List;

public class PartyHappiness {
    // 派对的最大快乐值
    // 公司的每个员工都符合 Employee 类的描述。整个公司的人员结构可以看作是一棵标准的、没有环的多叉树。
    // 树的头结点是公司唯一的老板，除老板之外的每个员工都有唯一的直接上级。叶结点是没有任何下属的基层员工
    // （subordinates列表为空），除基层员工外，每个员工都有一个或多个直接下级。
    // 现在该公司办 party，你可以决定哪些员工不来，哪些员工来，规则：
    //      1.如果某个员工来了，那么这个员工的所有直接下级都不能来
    //      2.派对的整体快乐值是所有到场员工快乐值的累加
    //      3.你的目标是让派对的整体快乐值尽量大
    // 给定一棵多叉树的头结点boss，返回派对的最大快乐值

    static class Employee {
        public  int happy; // 该名员工能带来的快乐值
        public List<Employee> subordinates; // 直接下级

        public Employee(int happy) {
            this.happy = happy;
            this.subordinates = new ArrayList<>();
        }
    }

    static class Info {
        public int attend; // 头结点在来的情况下，最大快乐值
        public int absent; // 头结点在不来的情况下，最大快乐值

        public Info(int attend, int absent) {
            this.attend = attend;
            this.absent = absent;
        }
    }

    public Info maxHappiness(Employee e) {
        if (e.subordinates.isEmpty())
            return new Info(e.happy, 0);
        int attend = e.happy;
        int absent = 0;
        for (Employee s: e.subordinates) {
            Info info = maxHappiness(s);
            attend += info.absent;
            // 为什么这里是max，因为在当前结点不来的情况下，其直接下属是可来可不来的，不是必须来的，
            // 所以需要在其直接下属来或不来中找较大值。当前结点如果出席，那直接下属必不能来，所以上面一条代码没有max
            absent += Math.max(info.attend, info.absent);
        }
        return new Info(attend, absent);
    }
}


