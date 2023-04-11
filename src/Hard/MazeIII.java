package Hard;

// leetCode499
// 给你一个二维矩阵maze，0表示路可以走，1表示障碍；再给一个一维数组ball，其长度为2，表示一个足球的初始位置；
// 再给一个长度为2的数组hole，表示球洞的位置。
// 我们只要往一个方向拨动这个足球，那么它就会朝着这个方向一直走直到碰到障碍物或者到边界才会停下。向上拨记为u
// 下d  左l  右r
// 最少拨动几次才能让球进洞，返回拨动轨迹。如果球不可能进洞 返回 impossible
// eg: 如果向下拨动一次，向右拨动一次就能进洞 则返回 dr

public class MazeIII {

    public static String[] DIR = {"d", "u", "r", "l"};


    // dir[0~3] 和 step[0~3] 每个对应的元素是相反的，这样在下面写算法时方便处理
    // 比如：当前位置是[3,5]是从从左边来的，那么封装的结点cur就是 (3,5,2, "l") 若其右边就是障碍，那么就得在此停下
    // 重新选择方向，假如选择了向上，那新封装的结点就是(3 + step[1][0], 5 + step[1][1], 1, "l" + dir[1])
    // 只要确定了使用下面两个全局变量的1号元素，新封装的结点就很好写了。
    // 为什么是1呢？因为选择了向上，那来到[2,5]时，是从下面来的，所以要用step[1]，因为从下往上走只会让行-1；
    // 从下往上走，拨动的方向是向上，所以要使用dir[1]
    // step[0~3]对应
    public static int[][] STEP = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {0, 0}};



    // 用宽度优先遍历，并且是每次处理一层的BFS。
    // 封装结点：(r, c)表示坐标几行几列
    // dir表示来到这个位置是从哪个方向过来的。pre表示之前如何拨动才来到了当前位置
    public static class Node {
        public int r;
        public int c;
        public int dir;
        public String pre;

        public Node(int x, int y, int dir, String pre) {
            this.r = x;
            this.c = y;
            this.dir = dir; // 0  1  2  3 分别表示上下左右   4表示是初始位置没有方向
            this.pre = pre;
        }
    }



    // 主方法
    public static String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        int N = maze.length;
        int M = maze[0].length;
        // 标记格子是否访问过。每个格子对应4个方向，只有位置相同并且方向相同才叫被访问过
        boolean[][][] visited = new boolean[N][M][4];
        // 两个数组模拟队列。cur表示当前层的结点，next用于存放cur中每个结点能继续BFS的结点
        Node[] cur = new Node[N * M];
        Node[] next = new Node[N * M];
        int s1 = 0, s2 = 0;  // 上面两个队列的长度比较大，里面存放的东西比较少，这两个变量就记录两个队列元素的数量
        s1 = spread(maze, N, M, new Node(ball[0], ball[1], 4, ""), visited, cur, s1);
        while (s1 != 0) {
            for (int i = 0; i < s1; i++) {
                Node c = cur[i];
                if (hole[0] == c.r && hole[1] == c.c)
                    return c.pre;
                s2 = spread(maze, N, M, c, visited, next, s2);
            }
            // 交换使用，让cur指向next，这样就能两个队列交替使用
            Node[] tmp = next;
            next = cur;
            cur = tmp;
            s1 = s2;
            s2 = 0;
        }
        return "impossible";
    }

    // maze  N  M 都是固定参数
    // cur表示当前的结点，其封装了当前来到的位置和从哪里来的
    // queue 下一层的队列  cur能走的所有下一步都会放入queue中
    // size 下一层队列填到了哪
    // 当前点cur，该分裂分裂，分裂的意思就是：如果cur紧挨着障碍了或者碰到边界了，那么他就可以寻找新的方向走了
    // 该继续走继续走，所产生的一下层的点，进入queue，s++
    // 返回值：q增长到了哪？返回size -> s
    private static int spread(int[][] maze, int N, int M, Node cur, boolean[][][] visited,
                              Node[] queue, int size) {
        int dir = cur.dir;
        // 一般说来到cur这个位置时都是带着某个方向过来的，一般情况下我们只能顺着这个方向继续走
        // step就是为了方便在原有方向上修改位置的。比如cur==(3,5) dir==2，说明来到3行5列这个位置是从左边来的，
        // 所以方向是朝右的，那么继续走一步的位置就应该是(3,6)
        // 同时也考虑了特例，就是方向是4表示是初始位置，所以+step[4]后不会有任何变化
        int r = cur.r + STEP[dir][0];
        int c = cur.c + STEP[dir][1];
        // 如下情况就需要分裂了，就是说不能继续朝着原有方向走了，得停住，重新选择了
        if (dir == 4 || r < 0 || r == N || c < 0 || c == M || maze[r][c] != 0) {
            // 4个方向枚举
            for (int i = 0; i < 4; i++) {
                if (i != dir) { // 不能继续朝着原有方向走
                    r = cur.r + STEP[i][0];
                    c = cur.c + STEP[i][1];
                    // 符合如下情况，这个方向才是真的可行的
                    if (r >= 0 && r < N && c >= 0 && c < M && maze[r][c] == 0 && !visited[r][c][i]) {
                        visited[r][c][i] = true;
                        Node follow = new Node(r, c, i, cur.pre + DIR[i]);
                        queue[size++] = follow;
                    }
                }
            }
        } else { // 不用分裂，继续朝着原有方向走
            if (!visited[r][c][dir]) {
                visited[r][c][dir] = true;
                queue[size++] = new Node(r, c, dir, cur.pre);
            }
        }
        return size;
    }

}
