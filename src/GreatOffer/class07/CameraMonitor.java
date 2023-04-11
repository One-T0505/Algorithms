package class07;

// leetCode968
// 相机最小覆盖问题
// 在一棵二叉树上，如果在某个结点上放置一个相机，那么可以覆盖自己本身、父结点、直接孩子结点。根结点如果放置相机，则只能覆盖自己
// 和直接孩子，没有父结点。请问，想覆盖整棵树，最少需要几台相机。

import utils.TreeNode;

public class CameraMonitor {

    public static int minCameraCover(TreeNode root) {
        if (root == null)
            return 0;
        Info all = f(root);
        return all.num + (all.isCovered ? 0 : 1);
    }

    private static Info f(TreeNode cur) {
        if (cur == null)
            return new Info(true, false, 0);
        Info left = f(cur.left);
        Info right = f(cur.right);

        boolean isPlaced = !(left.isCovered && right.isCovered);
        boolean isCovered = left.isPlaced || right.isPlaced || isPlaced;
        int num = (isPlaced ? 1 : 0) + left.num + right.num;

        return new Info(isCovered, isPlaced, num);
    }

    // 主方法
    public static int cameraCoveredV2(TreeNode root) {
        Data data = process2(root);
        return data.cameras + (data.status == Status.UNCOVERED ? 1 : 0);
    }
    // ---------------------------------------------------------------------------------------------------

    // 最关键的变化在这个方法中
    private static Data process2(TreeNode root) {
        // 此时root为空，那么默认是被覆盖了，并且只需要0个相机，root下面没有结点了；所以覆盖以root为根的树只需要0个相机
        if (root == null)
            return new Data(Status.COVERED_WITHOUT_CAMERA, 0);

        Data left = process2(root.left);
        Data right = process2(root.right);
        // 先设置下，反正不可能比这两个的和少
        int cameras = left.cameras + right.cameras;

        // 但反左右孩子中有一个没被覆盖到，那我现在如果不放相机，再到上层的结点，没人照顾得到他们，所以必须在我这里
        // 方台相机
        if (left.status == Status.UNCOVERED || right.status == Status.UNCOVERED)
            return new Data(Status.COVERED_WITH_CAMERA, cameras + 1);

        // 单反左右孩子中都被覆盖并且有一个是放了相机的，那我自己肯定不会再多余放相机了，并且我的状态就是覆盖且没相机的
        if (left.status == Status.COVERED_WITH_CAMERA || right.status == Status.COVERED_WITH_CAMERA)
            return new Data(Status.COVERED_WITHOUT_CAMERA, cameras);

        // 如果执行到了这里，说明：
        // left.status == COVERED_WITHOUT_CAMERA && right.status == COVERED_WITHOUT_CAMERA
        // 那么我自己肯定不会放相机，因为我的左右孩子都被覆盖了，但是他们身上都没相机。如果我自己放相机了，只能向上影响
        // 一个父结点，效益非常小，所以我选择不放相机，让上层父结点来抉择。这就是贪心的点。
        return new Data(Status.UNCOVERED, cameras);
    }


    // 以x为头，x下方的节点都是被covered，x自己的状况，分三种
    public static enum Status {
        UNCOVERED, COVERED_WITH_CAMERA, COVERED_WITHOUT_CAMERA
    }

    public static class Info {
        public boolean isCovered; // 该结点是否被覆盖了
        public boolean isPlaced;  // 该结点处是否放了摄像机

        public int num;    // 该结点为根的子树放了多少摄像机

        public Info(boolean isCovered, boolean isPlaced, int n) {
            this.isCovered = isCovered;
            this.isPlaced = isPlaced;
            num = n;
        }
    }

    // 以x为头，x下方的节点都是被covered，得到的最优解中：x是什么状态，在这种状态下，需要至少几个相机
    public static class Data {
        public Status status;
        public int cameras;

        public Data(Status status, int cameras) {
            this.status = status;
            this.cameras = cameras;
        }
    }
}
