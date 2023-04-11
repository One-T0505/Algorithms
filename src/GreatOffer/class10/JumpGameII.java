package class10;

// leetCode45
// 给你一个非负整数数组nums，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 你的目标是使用最少的跳跃次数到达数组的最后一个位置。假设你总是可以到达数组的最后一个位置。返回最少的步数。

public class JumpGameII {

    // 思路：维持三个变量：step 表示走了几步； cur 当前步数下能达到的最远距离；  next 如果再多走一步能达到的最远距离。
    // 遍历数组：arr = [2,3,1,1,4]  初始化时，因为在开头位置，所以，step=0  cur=0，因为还没开始走所以只能待在原位置0，
    // next=2，因为如果走一步的话那现在的位置arr[0]==2就是我能走到的最远距离。来到3时，i==1，已经超过了cur，就是说当前步数
    // 已经不能覆盖现在的位置了，所以step++，然后cur的值直接更新为next的值2，next值更新为1+3==4；来到1时，i==2，
    // 没超过cur，说明当前步数step仍能覆盖住现在的位置，所以step不变，2+1=2<4，所以next不变；来到第2个1时，i==3，超过了cur
    // 所以step++，然后cur更新为next的值4，next不变；来到最后的位置4时，此时i并没有超过cur，于是最后的结果就是step==2

    public static int jump(int[] nums) {
        if (nums == null || nums.length < 2)
            return 0;
        if (nums[0] == 0)
            return -1;
        int step = 0, cur = 0, next = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (i > cur) {
                step++;
                cur = next;
                next = 0;
            }
            next = Math.max(next, i + nums[i]);

        }
        return step;
    }
    // ----------------------------------------------------------------------------------------------------

}
