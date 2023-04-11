package class09;

// 字节原题(问题二)
// 给定一个数组arr,长度为N, arr中的值不是0就是1。arr[i]表示第i盏灯的状态，0代表灭灯，1代表亮灯。
// 每一盏灯都有开关，但是按下i号灯的开关，会同时改变i-1、 i、i+1 号灯的状态。
//
// 问题一: 如果N盏灯排成一条直线，请问最少按下多少次开关能让所有灯都亮着?
//        i为中间位置时，i号灯的开关能影响i-1、i和i+1；0号灯的开关只能影响0和1位置的灯；
//        N-1号灯的开关只能影响N-2和N-1位置的灯
//
// 问题二：如果N盏灯排成一个圈,请问最少按下多少次开关，能让灯都亮起来？
//        i为中间位置时，i号灯的开关能影响i-1、i和i+1；0号灯的开关能影响N-1、0和1位置的灯；
//        N-1号灯的开关能影响N-2、N- 1和0位置的灯

public class LightenLamp {

    // 先解决问题1
    public static int lampNoLoop(int[] arr) {
        if (arr == null || arr.length == 0)  // 数组无效，所以一次也不用按
            return 0;
        if (arr.length == 1)  // arr[0]==0则按1次   arr[0]==1则按0次
            return arr[0] ^ 1;
        // 只有两盏灯时，如果两个灯的状态不一样则必定搞不定，返回-1；如果状态相同，则同为0需按1次，同为1需按0次
        if (arr.length == 2)
            return arr[0] == arr[1] ? (arr[0] ^ 1) : -1;

        // 主函数中为何调了两次递归分支？ 我们决定0位置的按钮是否按的时候，前面没有位置了，没有办法帮我们辅助决策，
        // 而在递归中，来到一个真是的位置是否要按下按钮，都有它前面一个的状态辅助我们做决策。所以开头位置，我们不确定
        // 是否要按。即便开头如果本来就是开着的，也不能说明，我们就可以不按，万一不按，后面不可行呢？所以开头第一个位置
        // 很玄幻，我们得调用两个分支，一个是按的，一个是不按的，这样绝对能统计处所有情况，不遗漏。
        int p1 = f(arr, 2, arr[0], arr[1]);
        int p2 = f(arr, 2, arr[0] ^ 1, arr[1] ^ 1);
        if (p2 != -1)
            p2++;  // 加上改变0号灯状态的这一次
        return Math.min(p1, p2);
    }


    // 该函数f的定义：
    // 当前来到了i-1位置，但是我们传入的是当前位置的下一个位置
    // pre的值是：arr[i-2] 就是当前位置i-1的前一个位置状态
    // cur的值是：arr[i-1]，就是当前位置的状态
    private static int f(int[] arr, int i, int pre, int cur) {
        if (i == arr.length) // 说明当前来到了最后一个灯的位置，而非已经越界了
            // 如果最后一个灯的状态和前一个灯的状态不一样，那必然搞不定，否则就可以搞定
            return (pre ^ cur) == 0 ? (pre ^ 1) : -1;

        // 此时还不是最后一个灯的位置
        // 如果前一个灯的位置是关的，那我必须要在我这里按开关，因为此时不按，之后的位置不可能再能覆盖到pre了
        if (pre == 0) {
            int p1 = f(arr, i + 1, cur ^ 1, arr[i] ^ 1);
            return p1 == -1 ? -1 : p1 + 1;  // p1 + 1 表示加上当前按的那次
        } else  // pre == 1   说明在当前位置绝对不能按
            return f(arr, i + 1, cur, arr[i]);
    }
    // 可以发现，该递归函数中只会走一个递归分支，所以可以用循环的方法去改写递归
    // -------------------------------------------------------------------------------------------------


    // 迭代版，在主函数中直接f函数即可。
    private static int trace(int[] arr, int pre, int cur) {
        int i = 2;
        int res = 0;
        while (i != arr.length) {
            if (pre == 0) {
                res++;   // 必须在当前位置按开关
                pre = cur ^ 1;
                cur = arr[i++] ^ 1;
            } else {
                pre = cur;
                cur = arr[i++];
            }
        }
        return pre == cur ? (res + (pre ^ 1)) : -1;
    }
    // =====================================================================================================


    // 问题2
    // 此时的递归函数需要的参数和f的三个参数及含义保持一样：i，pre，cur；此外再另加两个参数：
    // head 表示0位置的状态；tail 一直表示N-1位置的状态；切记我们用的这些变量都是独立于原始数组arr之外的，
    // 我们不会真的去改arr的元素的，因为如果arr也变成可变的了，拿之后改成动态规划会非常复杂。我们的基本思想就是使可变参数
    // 不能超过int、boolean这种基本类型。因为递归中我们通常都是选择一些普适规则去做，所以要将比较特殊的位置单独拿出来处理。
    // 我们现在就以当前真正来到的位置来分析，而不是当前位置的下一个位置。
    // 0 1 2.........N-2 N-1      这些位置中，具有普适规则的位置是：2～N-3，因为在这些位置上，如果前一盏灯是关着的
    // 状态，那么在当前位置就必须按下开关，因为后续就没有机会再去拯救了。并且2～N-3这些位置，是无法影响 head 和 tail 的。
    // N-2 位置是在递归中要单独考虑的，因为会影响到 tail 的；0、1这两个位置是可以在递归之外的主函数
    // 中处理的，所以递归一开始传入的i就是3，因为是实际来到的位置的下一个位置。我们进入递归的时候不必像之前一样，一定要
    // 让0号位置是开着的，因为后面决策 N-1 的时候还可以影响 0 号位置。既然0号位置无所谓，那么1号位置也可以随便，因为2号
    // 位置还可以补救1。所以一开始进入递归时，前面0、1的操作可以有四种情况。

    public static int lampLoop(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return arr[0] ^ 1;
        if (arr.length == 2)
            return arr[0] == arr[1] ? arr[0] ^ 1 : -1;
        if (arr.length == 3)
            return arr[0] == arr[1] && arr[1] == arr[2] ? arr[0] ^ 1 : -1;
        int N = arr.length;
        // 在0位置按，1位置不按
        int p1 = g(arr, arr[1] ^ 1, arr[2], 3, arr[0] ^ 1, arr[N - 1] ^ 1);
        // 在0位置不按，1位置不按
        int p2 = g(arr, arr[1], arr[2], 3, arr[0], arr[N - 1]);
        // 在0位置按，1位置按
        int p3 = g(arr, arr[1], arr[2] ^ 1, 3, arr[0], arr[N - 1] ^ 1);
        // 在0位置不按，1位置按
        int p4 = g(arr, arr[1] ^ 1, arr[2] ^ 1, 3, arr[0] ^ 1, arr[N - 1]);
        p1 = p1 == -1 ? -1 : p1 + 1;
        p3 = p3 == -1 ? -1 : p3 + 2;
        p4 = p4 == -1 ? -1 : p4 + 1;
        return Math.min(Math.min(p1, p2), Math.min(p3, p4));
    }

    private static int g(int[] arr, int pre, int cur, int i, int head, int tail) {
        if (i == arr.length) // N-2 N-1 0 三处必须一致，否则就失败
            return pre == cur && cur == head ? pre ^ 1 : -1;

        int N = arr.length;

        // 当前位置，i - 1     当前的状态，叫cur
        // 如果不按下按钮，下一步的pre 就是 现在的cur
        // 如果按下按钮，下一步的pre, 就是当前的cur ^ 1
        // 如果不按下按钮，下一步的cur，就是arr[i]
        // 如果按下按钮，下一步的cur，就是arr[i] ^ 1

        // 一开始都默认给个值
        int noNextPre = 0;     // 不按按钮，下一个位置的pre
        int yesNextPre = 0;    // 按按钮，下一个位置的pre
        int noNextCur = 0;      // 不按按钮，下一个位置的cur
        int yesNextCur = 0;    // 按按钮，下一个位置的cur
        int noTail = 0;        // 不按按钮，下一个位置的N-1尾部状态
        int yesTail = 0;       // 按按钮，下一个位置的N-1尾部状态

        if (i < N - 1) {  // 说明还没来到N-2位置
            noNextPre = cur;
            yesNextPre = cur ^ 1;
            noNextCur = arr[i];
            yesNextCur = arr[i] ^ 1;
        } else if (i == N - 1) {
            noNextPre = cur;
            yesNextPre = cur ^ 1;
            noNextCur = tail;    // 为什么不能写noNextCur = arr[N-1]，我们绝对不会再原数组上改，尾部最新的状态在tail中
            yesNextCur = tail ^ 1;
            noTail = tail;
            yesTail = tail ^ 1;
        }

        if (pre == 0) {
            // 这里就要看当前的位置是不是N-2，如果不是那么尾部状态不会受到影响；如果是N-2，那么尾部状态会受到影响
            int next = g(arr, yesNextPre, yesNextCur, i + 1, i == N - 1 ? yesTail : tail, head);
            return next == -1 ? -1 : next + 1;
        } else
            return g(arr, i + 1, noNextPre, noNextCur,
                    i == N - 1 ? noTail : tail, head);
    }


    // 上面的g函数写法是完全按着思维轨迹一步一步写的，没有一丝简化和省略就是为了方便回忆。现在对g做一定的代码精简
    private static int g2(int[] arr, int pre, int cur, int i, int head, int tail) {
        if (i == arr.length) // N-2 N-1 0 三处必须一致，否则就失败
            return pre == cur && cur == head ? pre ^ 1 : -1;
        int N = arr.length;
        // 不按按钮，下一个位置的cur
        int noNextCur = i < N - 1 ? arr[i] : tail;
        // 按按钮，下一个位置的cur
        int yesNextCur = i < N - 1 ? arr[i] ^ 1 : tail ^ 1;
        int tailChange = i == N - 1 ? tail ^ 1 : tail;

        if (pre == 0) {
            // 这里就要看当前的位置是不是N-2，如果不是那么尾部状态不会受到影响；如果是N-2，那么尾部状态会受到影响
            int next = g(arr, i + 1, cur ^ 1, yesNextCur, tailChange, head);
            return next == -1 ? -1 : next + 1;
        } else
            return g(arr, i + 1, cur, noNextCur, tail, head);
    }


    // 有环问题的迭代版本：因为上方的递归，只可能中一个分支，不可能两个递归分支都中，所以可以改成迭代版。主函数是一模一样的
    // 只需要将递归函数替换成迭代函数即可。
    private static int traceLoop(int[] arr, int pre, int cur, int tail, int head) {
        int nextIndex = 3;
        int res = 0;
        while (nextIndex < arr.length - 1) {
            if (pre == 0) {
                res++;
                pre = cur ^ 1;
                cur = arr[nextIndex] ^ 1;
            } else {
                pre = cur;
                cur = arr[nextIndex++];
            }
        }
        // 到这里时，nextIndex == N - 1
        if (pre == 0) {
            res++;
            pre = cur ^ 1;
            tail ^= 1;
            cur = tail;
        } else {
            pre = cur;
            cur = tail;
        }
        return tail == pre && tail == head ? res + (tail ^ 1) : -1;
    }

}
