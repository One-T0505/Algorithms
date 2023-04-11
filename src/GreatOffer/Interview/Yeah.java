package GreatOffer.Interview;

// 来自360笔试
// 给定一个正数数组arr，长度为n，下标0~n-1
// arr中的0、n-1位置不需要达标，它们分别是最左、最右的位置
// 中间位置i需要达标，达标的条件是: arr[i-1] > arr[i]或者arr[i+1] > arr[i]哪个都可以
// 你每一步可以进行如下操作:对任何位置的数让其-1
// 你的目的是让arr[1~n-2]都达标，这时arr称之为yeah! 数组
// 返回至少要多少步可以让arr变成yeah!数组
// 数据规模: 数组长度 <= 10000， 数组中的值 <= 500

public class Yeah {

    // 先分析下：在1～N-2位置上不可能出现波峰，所以数组的整个数值变化曲线只可能是 "V" 型  "/" 型  "\"型
    // 当波谷在0位置时就是"/"  当波谷在N-1位置时就是"\"
    // 所以只需要枚举波谷的位置算出每一种情况的步数取最小值即可。
    // 比如数组：6 7 4 2  波谷在7，那就是让6～7是下坡，7～2是上坡
    // 将6～7变为下坡的最小代价是将7改成5，就是2；将7～2改为上坡：4变成1，代价为3，将5变成0代价为5
    // 我想说的是，波谷在一个点的时候，会重复计算代价，所以我们将汇聚点分成两个。比如6、7组合，7、4组合
    // 需要考虑特例：7 5 2  将5改成7的下坡，本应该改成6，但是5本身就比7小，所以不改最合适。
    // 根据上面的分析，我们需要用到预处理结构left，right，left[i]表示从头到i将这些元素改成下坡段的代价和
    // right[i]表示从最后一个元素到i改成下坡的累计代价

    public static int minStep(int[] arr) {
        if (arr == null || arr.length < 3) // 长度不够3直接达标
            return 0;
        int N = arr.length;
        // 构造一个新数组，其长度+2，两端点处设为最大值，然后中间N个位置copy原数组，这么做是为了让原数组的两个端点值
        // 也达标，这样在循环处理时比较方便，因为有了实际比自己大的值了。
        int[] nums = new int[N + 2];
        nums[0] = Integer.MAX_VALUE;
        nums[N + 1] = Integer.MAX_VALUE;
        System.arraycopy(arr, 0, nums, 1, N);
        // left数组
        int[] left = new int[N + 2];
        int pre = nums[0];
        int change = 0;  // change表示某个位置的元素最终应该变成多少  比如：7 9  那么9应该变为6
        for (int i = 1; i <= N; i++) {
            change = Math.min(pre - 1, nums[i]);
            left[i] = nums[i] - change + left[i - 1]; // nums[1]作为波谷时left[1]为0是对的
            pre = change;
        }
        // right数组
        int[] right = new int[N + 2];
        pre = nums[N + 1];
        for (int i = N; i >= 1; i--) {
            change = Math.min(pre - 1, nums[i]);
            right[i] = nums[i] - change + right[i + 1];
            pre = change;
        }

        // 枚举两个交汇点
        int res = Integer.MAX_VALUE;
        for (int latter = 1; latter < N; latter++) {
            res = Math.min(res, left[latter] + right[latter + 1]);
        }
        return res;
    }
}
