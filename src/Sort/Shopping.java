package Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// 增强堆的相关问题

// 给定一个整型数组，int[] arr和一个布尔类型数组，boolean[] op
// 两个数组一定等长，假设长度为N，arr[]表示客户编号，op[]表示客户操作
// arr = [3, 3，1，2, 1, 2, 5..
//  op = [T, T, T, T, F, T, F...
// 依次表示: 3用户购买了一件商品，3用户购买了一件商品，1用户购买了一件商品，2用户购买了一件商品，1用户退货了一件商品，
// 2用户购买了- -件商品, 5用户退货了一件商品..
//
// 得奖系统的规则:
//   1，如果某个用户购买商品数为0，但是又发生了退货事件，则认为该事件无效，得奖名单和之前事件时一致，比如例子中的5用户
//   2，某用户发生购买商品事件，购买商品数+1，发生退货事件，购买商品数-1
//   3，每次都是最多K个用户得奖，K也为传入的参数 如果根据全部规则，得奖人数确实不够K个，那就以不够的情况输出结果
//   4，得奖系统分为得奖区和候选区，任何用户只要购买数>0, 一定在这两个区域中的一个
//   5，购买数最大的前K名用户进入得奖区,在最初时如果得奖区没有到达K个用户，那么新来的用户直接进入得奖区
//   6，如果购买数不足以进入得奖区的用户，进入候选区
//   7, 如果候选区购买数最多的用户，已经足以进入得奖区，该用户就会替换得奖区中购买数最少的用户(大于才能替换),
//      如果得奖区中购买数最少的用户有多个，就替换最早进入得奖区的用户 如果候选区中购买数最多的用户有多个，
//      机会会给最早进入候选区的用户
//   8，候选区和得奖区是两套时间，因用户只会在其中一个区域，所以只会有一个区域的时间，另一个没有.从得奖区出来进入候选区的用户，
//      得奖区时间删除; 进入候选区的时间就是当前事件的时间(可以理解为arr[i]和op[i]中的i). 从候选区出来进入得奖区的用户，
//      候选区时间删除, 进入得奖区的时间就是当前事件的时间(可 以理解为arr[i]和op[i]中的i)
//   9，如果某用户购买数==0，不管在哪个区域都离开，区域时间删除，离开是指彻底离开，哪个区域也不会找到该用户,
//      如果下次该用户又发生购买行为，产生>0的购买数,会再次根据之前规则回到某个区域中，进入区域的时间重记

public class Shopping {

    // 最暴力的方法，完全不用优化. 返回结果是数组套数组：每一个都是一个时刻下的前k名获奖人
    public static List<List<Integer>> solutionV1(int[] arr, boolean[] op, int k){
        HashMap<Integer, Customer> map = new HashMap<>(); // 用于记录客户和id的对应关系
        ArrayList<Customer> candidates = new ArrayList<>();  // 候选区
        ArrayList<Customer> winners = new ArrayList<>();  // 得奖区
        List<List<Integer>> res = new ArrayList<>();  // 结果

        for (int i = 0; i < arr.length; i++) {
            int curId = arr[i];
            boolean buyOrRefund = op[i]; // 是购买还是退货
            // 如果是退货，并且map中还没有这个人的记录，说明这个人第一次来就是退货，不可能实现
            if (!buyOrRefund && !map.containsKey(curId)){
                res.add(getCurWinners(winners));
                continue;
            }
            // 能执行到这里说明不会发生用户购买数为0且要退货的情况
            // 但是还有三种情况要逐一处理：
            //  1. 用户之前购买数=0，此时买货
            //  2. 用户之前购买数>0，此时买货
            //  3. 用户之前购买数>0，此时退货

            if (!map.containsKey(curId)){  // 说明是第一次来购买或者之前购买数为0
                // shopping和enterTime暂且设为0，后面会调整的
                map.put(curId, new Customer(curId, 0, 0));
            }
            Customer c = map.get(curId);
            if (buyOrRefund)
                c.shopping++;
            else
                c.shopping--;
            if (c.shopping == 0)
                map.remove(curId);
            // 候选区也没有，获奖区也没有，说明之前购买数为0
            if (!candidates.contains(c) && !winners.contains(c)){
                c.enterTime = i;
                if (winners.size() < k){ // 如果获奖区有空位，直接加入获奖区
                    winners.add(c);
                } else {
                    candidates.add(c);  // 否则，加入候选区
                }
            }
            // 这种方法是遍历的，并不高效
            cleanZero(candidates);  // 将候选区中购买数为0的删除
            cleanZero(winners);     // 将获奖区中购买数为0的删除
            // 传入一个候选区的比较规则并排序
            candidates.sort(new Comparator<Customer>() {
                @Override
                public int compare(Customer o1, Customer o2) {
                    // 购买数量不同，优先放置购买数量多的；购买数量相同时，优先放置进入时间早的
                    return o1.shopping == o2.shopping ? o1.enterTime - o2.enterTime : o2.shopping - o1.shopping;
                }
            });

            // 传入一个获奖区的比较规则并排序
            winners.sort(new Comparator<Customer>() {
                @Override
                public int compare(Customer o1, Customer o2) {
                    // 购买数量相同，进入时间早的放前面，优先被换出
                    return o1.shopping == o2.shopping ? o1.enterTime - o2.enterTime : o1.shopping - o2.shopping;
                }
            });
            transfer(candidates, winners, k, i); // 看下候选区最前面的那个能否置换得奖区最前面的那个
            res.add(getCurWinners(winners));
        }
        return res;
    }

    private static void transfer(ArrayList<Customer> candidates, ArrayList<Customer> winners, int k, int time) {
        // 会存在这种情况的，用户刚购买一个商品进入候选区，然后立即退货了
        // 或者，k=3，但是现在只处理了两个记录
        if (candidates.isEmpty())
            return;
        // 候选区和获奖区的删除就是clearnZero，发生在transfer之前，而且每次至多获奖区会多出一个空位
        // 如果有空位，直接让候选区最前面的那个直接加入获奖区
        if (winners.size() < k) {
            Customer c = candidates.get(0);
            c.enterTime = time;
            winners.add(c);
            candidates.remove(0);
        } else { // 获奖区已满
            if (candidates.get(0).shopping > winners.get(0).shopping){
                Customer oldW = winners.get(0);
                Customer newW = candidates.get(0);
                winners.remove(0);
                candidates.remove(0);
                oldW.enterTime = time;
                newW.enterTime = time;
                // 虽然刚加进来，顺序还不对，但是每轮循环的时候，都会在transfer前sort的
                winners.add(newW);
                candidates.add(oldW);
            }
        }
    }

    private static void cleanZero(ArrayList<Customer> list) {
        list.removeIf(c -> c.shopping == 0);
    }

    private static List<Integer> getCurWinners(ArrayList<Customer> winners) {
        if (winners == null || winners.isEmpty())
            return null;
        ArrayList<Integer> res = new ArrayList<>();
        for (Customer c : winners)
            res.add(c.id);
        return res;
    }




    // 接下来，用增强堆来进行优化
    static class WhoIsWinner {
        private HashMap<Integer, Customer> map;  // 用户和id的关系表
        private EnhancedHeap<Customer> candidates;  // 候选区堆
        private EnhancedHeap<Customer> winners;  // 获奖区堆
        private final int topLimit;  // 获奖区的人数限制

        public WhoIsWinner(int k) {
            map = new HashMap<>();
            // 大根堆，谁买得多谁在最上面；买得同样多，那就谁先来谁在上面
            candidates = new EnhancedHeap<Customer>((o1, o2) ->
                    o1.shopping == o2.shopping ? o1.enterTime - o2.enterTime : o2.shopping - o1.shopping);
            // 小根堆，谁买得最少谁在最上面，买得同样少的，买得较早的在前面
            winners = new EnhancedHeap<Customer>((o1, o2) ->
                    o1.shopping == o2.shopping ? o1.enterTime - o2.enterTime : o1.shopping - o2.shopping);
            topLimit = k;
        }

        public static List<List<Integer>> solutionV2(int[] arr, boolean[] op, int k){
            List<List<Integer>> res = new ArrayList<>();
            WhoIsWinner heap = new WhoIsWinner(k);
            for (int i = 0; i < arr.length; i++) {
                heap.operate(i, arr[i], op[i]);
                res.add(heap.getWinners());
            }
            return res;
        }

        // 当前处理arr[i]事件
        public void operate(int time, int id, boolean buyOrRefund) {
            // 既是退货 并且 这个人还是第一次出现
            if (!buyOrRefund && !map.containsKey(id))
                return;
            // 执行到这里说明上面的条件不成立，所以现在的情况是：
            // 1. 退货 并且不是新用户     或者
            // 2. 购物 并且是新用户     或者
            // 3. 购物 并且不是新用户

            // 经过这个if之后，三种情况下，不管是什么操作都对应了用户
            if (!map.containsKey(id))
                map.put(id, new Customer(id, 0, 0));
            // 现在来统一处理是购物还是退货
            Customer c = map.get(id);
            if (buyOrRefund)
                c.shopping++;
            else
                c.shopping--;
            if (c.shopping == 0)
                map.remove(id);
            // 上面只是处理好了map，还没有处理候选区和获奖区
            // 下面开始就能体现出优化了
            // 当前用户不存在于两个区  那只有可能是情况2，购物，所以下面的if里并没有对c.shopping==0做检查，
            // 因为是购物，所以不可能是0
            if (!candidates.contains(c) && !winners.contains(c)){
                c.enterTime = time;
                if (winners.size() < topLimit){
                    winners.push(c);       // O(logN)
                } else {
                    candidates.push(c);    // O(logN)
                }
            } else if (candidates.contains(c)) {  // 存在于候选区  那就是说这个用户是之前就有的，对应了情况1和情况3
                // 因为有可能是退货，所以要对c.shopping检查
                if (c.shopping == 0)
                    candidates.remove(c);  // O(logN)
                else
                    candidates.resign(c);  // O(logN)
            } else {  // 存在于获奖区
                if (c.shopping == 0)
                    winners.remove(c);    // O(logN)
                else
                    winners.resign(c);    // O(logN)
            }
            transfer2(time);
        }  // 根据这个定制的增强堆

        private void transfer2(int time) {
            if (candidates.isEmpty())
                return;
            if (winners.size() < topLimit) {
                Customer c = candidates.pop();
                c.enterTime = time;
                winners.push(c);
            }else {
                if (candidates.peek().shopping > winners.peek().shopping){
                    Customer oldW = winners.pop();
                    Customer newW = candidates.pop();
                    oldW.enterTime = time;
                    newW.enterTime = time;
                    winners.push(newW);
                    candidates.push(oldW);
                }
            }
        }

        public List<Integer> getWinners() {
            List<Customer> customers = winners.getAllElements();
            List<Integer> res = new ArrayList<>();
            for (Customer c : customers)
                res.add(c.id);
            return res;
        }

    }

    static class Customer {
        public int id;
        public int shopping; // 购买的数量
        public int enterTime;  // 进入某个区域的时间

        public Customer(int id, int shopping, int enterTime) {
            this.id = id;
            this.shopping = shopping;
            this.enterTime = enterTime;
        }
    }
}
