package class04;

// leetCode218
// 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
// 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
// lefti 是第 i 座建筑物左边缘的 x 坐标; righti 是第 i 座建筑物右边缘的 x 坐标; heighti 是第 i 座建筑物的高度。
// 你可以假设所有的建筑都是完美的长方形，在高度为0的绝对平坦的表面上。天际线应该表示为由 “关键点” 组成的列表，格式：
// [[x1,y1],[x2,y2],...]，并按 x 坐标进行排序。关键点是水平线段的左端点，列表中最后一个点是最右侧建筑物的终点，y坐标
// 始终为0，仅用于标记天际线的终点。此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。
// 注意：输出天际线中不得有连续的相同高度的水平线。例如 [...[2 3], [4 5], [7 5], [11 5], [12 7]...]是不正确的答案；
// 三条高度为5的线应该在最终输出中合并为一个：[...[2 3], [4 5], [12 7], ...]。
//
//      |
//      |
//   5  |         |----------------|
//      |         |                |
//   3  |     |---|---|            |                             这样一个建筑就可以用三元组表示：[2, 4, 3]
//      |     |   |   |            |      |------|               如果像这样两个建筑组成的天际线就可以这样表示：
//      |_____|___|___|____________|______|______|______  x      [[2, 3], [3, 5], [8, 0]]
//            2   3   4            8      10     12              在2的时候高度2为2，到3的时候高度为5，到8的时候
//                                                               高度为0，这样就勾画出了天际线。
//
//  如果再新加一个建筑：[10, 12, 2]，那么就如上图所示。那么新组成的天际线就是：[[2, 3], [3, 5], [8, 0], [10, 2], [12, 0]]
// 8~10这块也算在天际线内。

// 1 <= buildings.length <= 10^4
// 0 <= lefti < righti <= 2^31 - 1
// 1 <= heighti <= 2^31 - 1
// buildings 按 lefti 非递减排序

import java.util.*;

public class SkyLine {

    // 整个地平线的高度最开始都是为0，当一栋建筑来了，会让地平线从某个地方上升，再在某个地方下降，所以，一栋建筑
    // 可以描述成从哪里升起和从哪里下降两段。下面的数据结构就是描述建筑的这种性质的。一栋建筑需要封装成两个Node
    // isAdd==true表示是上升，false表示下降。

    public static List<List<Integer>> skyLine(int[][] buildings) {
        if (buildings == null || buildings.length == 0 || buildings[0].length != 3)
            return null;
        int N = buildings.length;
        // 每个建筑都封装成两个结点，一个是高度在哪上升，一个高度在哪下降
        Node[] nodes = new Node[N << 1];
        for (int i = 0; i < N; i++) {
            nodes[i << 1] = new Node(buildings[i][0], true, buildings[i][2]);
            nodes[i << 1 | 1] = new Node(buildings[i][1], false, buildings[i][2]);
        }
        // 仅根据node.x排序
        Arrays.sort(nodes, (a, b) -> (a.x - b.x));
        // key：高度   value：次数
        TreeMap<Integer, Integer> heightTimes = new TreeMap<>();
        // key：x坐标   value：x坐标处的高度  这个结构是为了方便最后生成关键点的
        TreeMap<Integer, Integer> xHeight = new TreeMap<>();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isAdd) {  // 高度上升
                heightTimes.put(nodes[i].h, heightTimes.getOrDefault(nodes[i].h, 0) + 1);
            } else {   // 高度下降，不用判断是否存在，因为如果是下降必然是之前同样高度的上升已经存在了
                if (heightTimes.get(nodes[i].h) == 1)
                    // 不能让它等于0而放在表里，必须清除，因为后面要获取lastKey(lasyKey就是有序表里当下最大的值)，
                    // 不清除会影响结果
                    heightTimes.remove(nodes[i].h);
                else
                    heightTimes.put(nodes[i].h, heightTimes.get(nodes[i].h) - 1);
            }
            // 接下来就是记录当前位置x处的高度
            if (heightTimes.isEmpty())
                xHeight.put(nodes[i].x, 0);   // 就像上图中8～10那段一样
            else
                // lastKey表示最大的key；heightTimes此时有的key的最大值就表示当前x位置天际线的高度
                xHeight.put(nodes[i].x, heightTimes.lastKey());
        }
        // 此时xHeight就记录了每个关键点位置的高度
        List<List<Integer>> skyLine = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : xHeight.entrySet()) {
            int pos = entry.getKey();
            int height = entry.getValue();
            if (skyLine.isEmpty() || skyLine.get(skyLine.size() - 1).get(1) != height)
                skyLine.add(new ArrayList<>(Arrays.asList(pos, height)));
        }
        return skyLine;
    }

    public static class Node {
        public int x;
        public boolean isAdd;
        public int h;

        public Node(int x, boolean isAdd, int h) {
            this.x = x;
            this.isAdd = isAdd;
            this.h = h;
        }
    }


}
