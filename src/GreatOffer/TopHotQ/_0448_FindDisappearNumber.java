package GreatOffer.TopHotQ;

import java.util.ArrayList;
import java.util.List;

// 给你一个含 n 个整数的数组 nums ，其中 nums[i] 在区间 [1, n] 内。请你找出所有在 [1, n] 范围
// 内但没有出现在 nums 中的数字，并以数组的形式返回结果。

public class _0448_FindDisappearNumber {


    // 本题要使用之前讲过的下标循环怼方法，就是让nums[i]上争取放的数是i+1
    // 假如数组是：[4, 2, 6, 3, 1, 3, 5]  先从0位置玩下标循环怼
    // 0位置的4应该放在3位置，3位置上放的不是4，所以让4直接填在3位置，并把3位置的3挤出来，被挤出的3应该放在2位置，
    // 2位置放的是6，所以将3放在2位置，并把2位置的6挤出来，6应该放在5位置，5位置放的3，不是6，所以把3挤出来，3应该
    // 去2位置，发现2位置已经放了3，此时下标循环怼结束，最后被挤出的3可以不用管了，因为既然流程结束了，就说明
    // 当前被挤出的元素应该放的位置上已经放了这个元素，所以当前被挤出的元素是多余的，不管即可。从0位置运行一次算法后，
    // 数组就变成了：[4, 2, 3, 4, 1, 6, 5]
    // 再从1位置运行一边算法后，数组变成了：[4, 2, 3, 4, 1, 6, 5]  数组没变化，因为1位置放的就是2，是正确的
    // 2、3位置也是一样了；现在从4位置开始运行算法，数组变成了：[1. 2. 3. 4. 1. 6. 5]
    // 最后从6位置运行一遍，数组变成了：[1, 2, 3, 4, 5, 6, 5]
    // 最后遍历数组，i位置上放的不是i+1的，就是缺少的数
    public List<Integer> findDisappearedNumbers(int[] nums) {
        ArrayList<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0)
            return res;
        int N = nums.length;
        for (int i = 0; i < N; i++)
            walk(nums, i);
        for (int i = 0; i < N; i++) {
            if (nums[i] != i + 1)
                res.add(i + 1);
        }
        return res;
    }


    // 从i位置运行下标循环怼算法  这里实现下标循环怼的方式和上面描述的那样有一点不同
    // 实现的思路：当前位置不合法，就把这个位置的值送去合法的位置，并把那个合法位置上的值交换回来继续处理
    // 实现时每次都只认准一个位置，不停地把别的值送回这个位置。
    // 这样实现是不会丢弃值的。
    private void walk(int[] nums, int i) {
        while (nums[i] != i + 1) {
            int pos = nums[i] - 1;
            if (nums[pos] == pos + 1)
                break;
            // swap(i, pos)
            int tmp = nums[i];
            nums[i] = nums[pos];
            nums[pos] = tmp;
        }
    }
}
