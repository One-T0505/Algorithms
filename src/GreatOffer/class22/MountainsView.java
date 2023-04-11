package GreatOffer.class22;


// 一个不含有负数的数组可以代表一圈环形山， 每个位置的值代表山的高度
// 比如，{3,1,2,4,5}、 {4,5,3,1,2}或{1,2,4,5,3}都代表同样结构的环形山
// 山峰A和山峰B能够相互看见的条件为:
//  1.如果A和B是同一座山，认为不能相互看见
//  2.如果A和B是不同的山，并且在环中相邻，认为可以相互看见
//  3.如果A和B是不同的山，并且在环中不相邻，假设两座山高度的最小值为min。
//    1)如果A通过顺时针方向到B的途中没有高度比min大的山峰，认为A和B可以相互看见
//    2)如果A通过逆时针方向到B的途中没有高度比min大的山峰，认为A和B可以相互看见
//   两个方向只要有一个能看见，就算A和B可以相互看见
// 给定一个不含有负数且没有重复值的数组arr，请返回有多少对山峰能够相互看见。
//
// 进阶问题
// 给定一个不含有负数但可能含有重复值的数组arr,返回有多少对山峰能够相互看见。

import java.util.Stack;

public class MountainsView {

    // 假设这一个环中最大值为max，次大值为mmax，可以想像出如下的图：
    //
    //       ___________              现在有一个值x，顺时针找到第一个比自己大的数标记一下，标记处最远只能到
    //      /           \             max，或者在max之前；x逆时针找到第一个比自己大的数，最差情况也只能到mmax，
    //    max           mmax          或者在mmax之前；也就是说x一定能在[max,mmax]找到两个数和自己构成一对。
    //      \_____x_____/             同样的，上面那部分也都可以找到两个数和自己成一对，并且不会到下半部分。
    //
    // 用我们这样的方法来找是不会找出重复情况的。假设一共有N个数，那么除去max和mmax，还剩N-2个数，所以一共能找出
    // 2*(N-2)对，还遗漏了max和mmax这一对，所以最终结果就是：2*(N-2)+1 == 2*N-3
    public static int problem1(int[] m) {
        if (m == null || m.length < 2)
            return 0;
        // 当只有两个数的时候，只有1对；当长度为3时，两两成对
        if (m.length <= 3)
            return m.length == 2 ? 1 : 3;

        int N = m.length;
        return (N << 1) - 3;
    }
    // ===========================================================================================


    // 当有重复值的时候，我们需要用到单调栈了，单调栈存放的类型是Record，一个val记录是什么值，times，记录出现了多少次
    // 先要遍历数组，找到最大值，如果最大值有多个，随便哪一个都可以，然后以此为起点，循环遍历一遍数组。
    // 单调栈的规则：从栈顶到栈底严格按照递增顺序排列。首先让一开始找到的最大元素入栈，然后新元素进栈时，如果比栈顶元素小
    // 直接入栈；如果比栈顶元素大，那就一直弹栈，直到找顶元素<=当前要入栈的元素，如果此时相等，那么直接让栈顶元素的times++，
    // 如果不等，那就新生成一个Record加入；每次弹出栈的元素，都可以进行结算了，这个元素向后能找到当前元素，向前能找到栈里
    // 在他下面的元素，所以被弹出的元素每个都能找到两对，该被弹出的元素的数量times，乘2，就得到了这个被弹出元素对外的对数；
    // 对内的对数就是C(times,2)，但是times要>=2才可以，如果只有1个，那就没有对内的数量。
    //
    // 上面的结算方式都是在循环进行过程中结算的。当循环结束后，还需要对栈内剩余元素结算。如果栈内剩余记录数超过2个，那么
    // 除了倒数第一和倒数第二两个记录外，其他记录结算方式和循环进行中结算方式一模一样。因为除了最后两条记录外，其他记录
    // 一定能找到至少比自己大的两种不同数，如果该记录是(a,k)，表示数值为a，数量为k，那么就算方式：2*k + C(k,2)
    // 对于倒数第二条记录(b,m)，对外结算要看最后一条记录(c, t)的数量t，如果t==1，那么对外只能生成m对，就是每个b都和
    // 这个唯一的数形成一对；如果t>=2，那么对外就可生成m*t对，倒数第二条记录的每个b都可以从前找到一个c，然后从反方向
    // 找到一个不同的c；对内结算还是C(m,2)
    // 最后一条记录(a,k)的结算方式只有对内的结算

    public static int problem2(int[] m) {
        if (m == null || m.length < 2)
            return 0;
        int N = m.length;
        int maxIndex = 0;
        // 先在数组中找到一个最大值的位置，如果最大值有多个，随便一个的位置都可以
        for (int i = 1; i < N; i++)
            maxIndex = m[i] > m[maxIndex] ? i : maxIndex;

        Stack<Record> stack = new Stack<>();
        // 先把找到的最大值放进去
        stack.push(new Record(m[maxIndex], 1));
        // 从最大值位置的下一个位置开始沿next方向遍历
        int index = nextIndex(maxIndex, N);
        // 用“小找大”的方式统计所有可见山峰对
        int res = 0;
        // 当index到了maxIndex，说明转了一周，那么循环结束
        while (index != maxIndex) {
            // 当前数要进入栈，判断会不会破坏第一维的数字从顶到底依次变大
            // 如果破坏了，就依次弹出栈顶记录，并计算山峰对数量
            while (stack.peek().val < m[index]) {
                int times = stack.pop().times;
                // 弹出记录为(x,k)，如果k==1，产生2对; 如果k>1，产生2*k+C(2,k)对。
                res += internal(times) + (times << 1);
            }
            // 当前数字arr[index]要进入栈了，如果和当前栈顶数字一样就合并
            // 不一样就把记录(arr[index],1)放入栈中
            if (stack.peek().val == m[index])
                stack.peek().times++;
            else
                stack.push(new Record(m[index], 1));

            index = nextIndex(index, N);
        }
        // 清算阶段开始了
        // 清算阶段的第1小阶段
        while (stack.size() > 2) {
            int times = stack.pop().times;
            res += internal(times) + (times << 1);
        }
        // 清算阶段的第2小阶段
        if (stack.size() == 2) {
            int times = stack.pop().times;
            res += internal(times) + (stack.peek().times == 1 ? times : times << 1);
        }
        // 清算阶段的第3小阶段
        res += internal(stack.pop().times);

        return res;
    }
    // 在栈中相邻记录在实际数组中未必相邻，但是通过栈的结算后，这些相邻记录可以看成是在实际中相邻的，这样对于最后的
    // 清算阶段好想像。


    // 几个相等的数内部组合有几种，如果数量只有1个，那么成对的数量为0，
    // 如果相等的数量为2，那么成对的数量为1；超过两个的，那么组合数就是C(k,2)
    private static int internal(int k) {
        return k <= 2 ? k - 1 : (k * (k - 1)) >> 1;
    }


    // 该方法返回当前位置cur的下一个位置
    private static int nextIndex(int cur, int N) {
        return cur == N - 1 ? 0 : cur + 1;
    }


    public static class Record {
        public int val;
        public int times;

        public Record(int val, int times) {
            this.val = val;
            this.times = times;
        }
    }
}
