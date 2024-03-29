package GreatOffer.Interview;

// leetCode489
// 房间（用格栅表示）中有一个扫地机器人。格栅中的每一个格子有空和障碍物两种可能  0表示障碍物  1表示可以通过。
// 扫地机器人提供4个API，可以向前进，向左转或者向右转。每次转弯90度。
// 当扫地机器人试图进入障碍物格子时，它的碰撞传感器会探测出障碍物，使它停留在原地。
// 请利用提供的4个API编写让机器人清理整个房间的算法。

// 1.输入只用于初始化房间和机器人的位置。你需要“盲解”这个问题。换而言之，你必须在对房间和机器人位置一无所知的情
//   况下，只使用4个给出的API解决问题。
// 2.扫地机器人的初始位置一定是空地。
// 3.扫地机器人的初始方向向上。
// 4.所有可抵达的格子都是相连的，亦即所有标记为1的格子机器人都可以抵达。
// 5.可以假定格栅的四周都被墙包围。

import java.util.HashSet;

public class CleanRobot {

    // step[0]是朝着左方向走一步坐标应该变化的值  1是向上  2是向右  3是向下
    public static final int[][] step = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    // 要实现的方法
    // 给的参数里不包括这个二维矩阵位置图，说明这个边界和下一步是否为障碍不能通过从矩阵读取数据直接判断，
    // 只能通过move来判断，也就是说机器人是对边界没有感觉的，他只能通过move方法感知世界。
    // 一开始机器人的初始位置就默认为(0, 0)，就把随机的方向当作0
    public static void cleanRoom(Robot robot) {
        sweep(robot, 0, 0, 0, new HashSet<>());
    }


    // 机器人robot，当前来到的位置(x,y)，且之前没来过
    // 机器人脸朝什么方向d， 0 1 2 3
    // visited里记录了机器人走过哪些位置
    // 函数的功能：不要重复走visited里面的位置，把剩下的位置，都打扫干净！
    //           而且要返回当前原位
    private static void sweep(Robot robot, int x, int y, int d, HashSet<String> visited) {
        // 最开始上游调用时就是机器人的初始位置和随机方向，所以该位置必然是可行的，并且从没来过，第一步
        // 直接打扫加入集合没问题。
        // 如果是被递归调用的，那么我们必然会对可行性进行判断，只有当前位置可行，我们才会进入到这个递归，所以
        // 第一步直接打扫加集合也没问题
        robot.clean();  // 先打扫当前位置
        // 之前标记一个位置是直接生成一个和原始矩阵等大的矩阵，但是这里我们并没有原始矩阵信息，所以只能通过集合
        // 来标记去过的位置  x_y  以这样一种字符串形式标记去过了(x, y) 这个点
        visited.add(x + "_" + y);
        // 尝试4个方向。这样尝试：不管当前方向是怎么样的，我们就按照顺时针方向尝试一圈，并且开头都得是原有方向
        // 用0～3表示4个方向
        // 如果原有方向是0，那尝试顺序就是：0 1 2 3
        // 如果原有方向是1，那尝试顺序就是：1 2 3 0
        // 如果原有方向是2，那尝试顺序就是：2 3 0 1
        // 如果原有方向是3，那尝试顺序就是：3 0 1 2
        for (int i = 0; i < 4; i++) {
            // 下一步的方向
            int nd = (i + d) % 4;
            // 当下一步的方向定了！下一步的位置在哪？(nx, ny)
            int nx = x + step[nd][0];
            int ny = y + step[nd][1];
            // 如果决定走的下一个点(nx, ny)没走过，并且move是可行的，那么调用完move后，机器人已经来到了
            // (nx, ny)这个点了
            if (!visited.contains(nx + "_" + ny) && robot.move()) {
                sweep(robot, nx, ny, nd, visited);
            }
            // 现在你要明白sweep方法的作用，如果我调用了sweep(m, n, p) 那么执行完之后，机器人依然会回到
            // (m, n)并且方向是p，但是上面的都是逻辑上的，机器人真实的方向还是一开始传入的d，我们必须要实际地
            // 让它转动才行。因为我们规定了尝试的方向是按照顺时针进行的，所以下面需要向右转。
            robot.turnRight();
        }
        // 每个方向机器人都会尽力打扫能打扫的区域，现在4个方向都打扫完了，此时机器人又回到了该方法传入的起始点
        // (x, y, d) 方向也是d
        // 为了让递归的含义通顺，所以上游调用了sweep(x, y, d)后，我们依然要让其回到起点，起点就是谁调用的(x, y, d)
        // 那就是朝着d的反方向前进一步，然后再把头调过来
        // 比如我现在在(2, 3, 2) 那谁让我来到(2, 3, 2)的？是(2, 2, 2) 于是先要让机器人朝左，所以要先
        // 向右转两次，然后移动一步，此时方向还是朝着左的，于是再向右转两次，就回到了一样的方向，
        robot.turnRight();
        robot.turnRight();
        robot.move();
        robot.turnRight();
        robot.turnRight();
    }

    // 题目给出的接口，不用管具体实现
    public interface Robot {
        // 机器人是有方向的，如果朝着这个方向的下一步是障碍，则机器人停在原地，并返回false
        // 如果下一步可走，则机器人会移动到下一步，并返回true，表示移动成功
        public boolean move();

        // 在调用turnLeft/turnRight后机器人会停留在原位置  每次转弯90度
        public void turnLeft();

        public void turnRight();

        // 清理所在方格
        public void clean();
    }
}
