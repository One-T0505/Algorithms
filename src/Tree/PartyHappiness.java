package Tree;

import java.util.ArrayList;
import java.util.List;

public class PartyHappiness {
    // 派对的最大快乐值
    // 公司的每个员工都符合 Employee 类的描述。整个公司的人员结构可以看作是一棵标准的、没有环的多叉树。
    // 树的头结点是公司唯一的老板，除老板之外的每个员工都有唯一的直接上级。叶结点是没有任何下属的基层员工
    // （sobordinates列表为空），除基层员工外，每个员工都有一个或多个直接下级。
    // 现在该公司办 party，你可以决定哪些员工不来，哪些员工来，规则：
    //      1.如果某个员工来了，那么这个员工的所有直接下级都不能来
    //      2.派对的整体快乐值是所有到场员工快乐值的累加
    //      3.你的目标是让派对的整体快乐值尽量大
    // 给定一棵多叉树的头结点boss，返回派对的最大快乐值

    public class Info {
        public int yes; // 头结点在来的情况下，最大快乐值
        public int no; // 头结点在不来的情况下，最大快乐值

        public Info(int yes, int no) {
            this.yes = yes;
            this.no = no;
        }
    }

    public Info maxHappiness(Employee x) {
        if (x.subordinates.isEmpty())
            return new Info(x.happy, 0);
        int yes = x.happy;
        int no = 0;
        for (Employee s: x.subordinates) {
            Info info = maxHappiness(s);
            yes += info.no;
            no += Math.max(info.yes, info.no);
        }
        return new Info(yes, no);
    }
}

class Employee {
    public  int happy; // 该名员工能带来的快乐值
    public List<Employee> subordinates; // 直接下级

    public Employee(int happy) {
        this.happy = happy;
        this.subordinates = new ArrayList<>();
    }
}

