package Basic.Tree.OrderedTree;

// 给定数组hard和money，长度都为N，hard[i]表示i号工作的难度， money[i]表示i号工作的收入
// 给定数组ability，长度为M，ability[j]表示j号人的能力，每一号工作，都可以提供无数的岗位，难度和收入都一样
// 但是人的能力必须>=这份工作的难度，才能上班。返回一个长度为M的数组ans，ans[j]表示j号人能获得的最好收入

import utils.arrays;

import java.util.Arrays;
import java.util.TreeMap;

public class BestSalary {

    // 思路：将工作难度和报酬一一对应起来封装成一个类Job，先让Job按难度从小到大排序，再按报酬从大到小排序；
    //      这样数组整体上就是按照难度生序排序，然后相同难度的工作内部又是按照报酬从高到低排序。因为每个人只能选
    //      一份工作，所以只保留每种难度下报酬最高的那个工作，其余的都删除。因为难度相同的情况下不可能选择报酬低的。
    //      这样留下的就是每种难度下报酬最高的工作。将这些筛选剩下的工作放在有序表中，每个人来时，从有序表中查出
    //      <=ability[i]的结点即可。
    public static class Job {
        public int hard;
        public int money;

        public Job(int hard, int money) {
            this.hard = hard;
            this.money = money;
        }
    }


    // 主方法
    public static int[] bestSalary(int[] hard, int[] money, int[] ability){
        int N = hard.length;
        int M = ability.length;
        Job[] jobs = new Job[N];
        for (int i = 0; i < N; i++) {
            jobs[i] = new Job(hard[i], money[i]);
        }
        Arrays.sort(jobs, (a, b) -> (a.hard == b.hard ? b.money - a.money : a.hard - b.hard));
        TreeMap<Integer, Integer> map = new TreeMap<>();
        // 排完序之后的首个元素必然要入表，因为是难度最低，并且是同难度下报酬最高的
        // 那些不需要的Job是不会入表的
        // 如果在排完序后，后一个工作的难度比前一个工作大，但是报酬不增，那必然要丢弃，也不会进表
        Job pre = jobs[0];
        map.put(pre.hard, pre.money);
        for (int i = 1; i < N; i++) {
            if (jobs[i].hard > pre.hard && jobs[i].money > pre.money){
                pre = jobs[i];
                map.put(pre.hard,pre.money);
            }
        }
        int[] res = new int[M];
        for (int i = 0; i < M; i++) {
            res[i] = map.floorKey(ability[i]) == null ? 0 : map.get(map.floorKey(ability[i]));
        }
        return res;
    }
    // ==============================================================================================





    // 暴力方法做对数器
    public static int[] bestSalary2(int[] hard, int[] payoff, int[] ability){
        if (hard == null || payoff == null || ability == null || hard.length != payoff.length)
            return null;
        int N = hard.length;
        int M = ability.length;
        int[] res = new int[M];
        for (int i = 0; i < M; i++) {
            int best = 0;
            for (int j = 0; j < N; j++) {
                if (hard[j] <= ability[i] && payoff[j] > best)
                    best = payoff[j];
            }
            res[i] = best;
        }
        return res;
    }




    // for test
    public static void test(){
        int testTime = 100000;
        int fixedLen = 4;
        int maxPay = 10;
        int maxHard = 8;
        int maxLen = 5;
        for (int i = 0; i < testTime; i++) {
            int[] hard = arrays.fixedLenArray(fixedLen, maxHard);
            int[] payoff = arrays.fixedLenArray(fixedLen, maxPay);
            int[] ability = arrays.randomNoNegativeArr(maxLen, maxHard);
            int[] res1 = bestSalary(hard, payoff, ability);
            int[] res2 = bestSalary2(hard, payoff, ability);
            if (!arrays.isSameArr(res1, res2)){
                System.out.println("Failed");
                System.out.print("难度数组：");
                arrays.printArray(hard);
                System.out.print("报酬数组：");
                arrays.printArray(payoff);
                System.out.print("能力数组：");
                arrays.printArray(ability);
                System.out.print("优化方法：");
                arrays.printArray(res1);
                System.out.print("暴力方法：");
                arrays.printArray(res2);
                return;
            }
        }
        System.out.println("AC");
    }




    public static void main(String[] args) {
//        int[] hard = {6, 4, 5, 4};
//        int[] money = {2, 10, 8, 3};
//        int[] ability = {6, 6};
//        arrays.printArray(bestSalary(hard, money, ability));
        test();
    }
}
