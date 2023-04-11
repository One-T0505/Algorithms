package GreatOffer.class25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 给定一个起始IP地址ip和一个我们需要分配的IP的数量n，返回用列表(最小可能的长度)表示的CIDR块的范围。
// CIDR块是包含IP的字符串，后接斜杠和固定长度。例如:“123.45.67.89/20"。固定长度“20"表示在特定的
// 范围中公共前缀位的长度。
//
// 输入: ip = "255.0.0.7"， n = 10
// 输出: ["255.0.0.7/32","255.0.0.8/29" ，"255.0.0.16/32"]
// 解释:
//  转换为二进制时，初始IP地址如下所示(为清晰起见添加了空格) :
//  255.0.0.7 -> 11111111 00000000 00000000 00000111
//  地址"255.0.0.7/32" 表示与给定地址有相同的32位前缀的所有地址，在这里只有这一个地址。
//  地址"255.0.0.8/29" 表示与给定地址有相同的29位前缀的所有地址，
//  255.0.0.8 -> 11111111 00000000 00000000 00001000 有相同的29位前缀的地址如下:
//  11111111 00000000 00000000 00001000
//  11111111 00000000 00000000 00001001
//  11111111 00000000 00000000 00001010
//  11111111 00000000 00000000 00001011
//  11111111 00000000 00000000 00001100
//  11111111 00000000 00000000 00001101
//  11111111 00000000 00000000 00001110
//  11111111 00000000 00000000 00001111
//
// 地址"255.0. 0.16/32"表示与给定地址有相同的32 位前缀的所有地址，这里只有1111111 0000000 0000000 00010000。
// 总之，答案指定了从255.0.0.7 开始的10个IP的范围。有一些其他的表示方法，例如:
// ["255.0.0.7/32", "255.0.0.8/30"，"255.0.0.12/30"， "255.0.0.16/32"] ，但是我们的答案是最短可能的答案。
// 另外请注意以"255.0.0.7/30" 开始的表示不正确，因为其包括了255.0.0.4 = 11111111 00000000 00000000 00000100
// 这样的地址，超出了需要表示的范围。

public class IPToCIDR {

    // 任何一个ip：x.x.x.x 都可以转化成一个int型的整数，该整数的数值并没有意义，有意义的是其32个二进制位上的数值。
    // 我们现在的第一个任务就是将一个String类型的"x.x.x.x" 转换成int型的数。每一个x就是8个二进制位，最多表示到255。
    private static int getStatus(String ip) {
        int status = 0;
        // 以"."为分隔符，但是需要\\来转义
        String[] parts = ip.split("\\.");
        int move = 24;
        // parts长度一定为4
        for (int i = 0; i < 4; i++) {
            // 首先将每部分转换成int型，就变成32位了，但是只有最低的8位有效
            // 根据parts在原ip地址的位置，每部分需要移动24位、16位、8位、0位
            status |= Integer.parseInt(parts[i]) << move;
            move -= 8;
        }
        return status;
    }



    // 完成了第一个小任务后，现在要考虑下一个任务。给定了起始ip后，我们需要先找到最右侧的1，有两种情况：
    // 1.最右侧的1右侧的位数够分配n个ip地址; 2.不够
    // 比如：x.x.x.01010000   n==13   那么这就是够用的情况，因为最右侧的1后面还有4个位，可分配16个ip

    private static int mostRightPower(int status) {
        // 如果dp为空，那就生成，当下次需要用到时就可以直接获取了
        if (dp.isEmpty()) {
            // 认为0这个数是32全为0，最高位的1已经溢出了，所以能搞定2的32次方
            dp.put(0, 32);
            for (int i = 0; i < 32; i++) {
                dp.put(1 << i, i);
            }
        }
        // status & (-status) 是获取最右侧的1
        return dp.get(status & (-status));
    }


    // 在当前status这个状态下，能搞定2的power次方个ip地址，现在要把它们转换成要求的输出形式
    private static String content(int status, int power) {
        StringBuilder sb = new StringBuilder();
        for (int move = 24; move >= 0; move -= 8) {
            sb.append((status & (255 << move)) >>> move).append(".");
        }
        sb.setCharAt(sb.length() - 1, '/');
        sb.append(32 - power);
        return sb.toString();
    }




    // key：某个2的次幂   value：能搞定的2的几次方
    private static HashMap<Integer, Integer> dp = new HashMap<>();

    public static List<String> ipToCIDR(String ip, int n) {
        int status = getStatus(ip);
        // ip地址最右侧的1能分配2的几次方  比如：x.x.x.01010000 那么 maxPower == 4
        int maxPower = 0;
        // 表示已经解决了多少个
        int solved = 0;
        // 临时变量
        int power = 0;
        ArrayList<String> res = new ArrayList<>();

        while (n > 0) {
            // status最右侧的1能搞定2的几次方
            maxPower = mostRightPower(status);
            // 现在只解决了自己
            solved = 1;
            power = 0;
            // status : x.x.x.00001000 -> 2的3次方的问题
            // solved : x.x.x.00000001 -> 1 2的0次方
            // solved : x.x.x.00000010 -> 2 2的1次方
            // solved : x.x.x.00000100 -> 4 2的2次方
            // solved : x.x.x.00001000 -> 8 2的3次方
            // 下面的循环在检查，当前状态是否够用，并且还防溢出了，先看看solved移动后是否会超过n，power+1是否
            // 不超过maxPower，都合法的情况下，才会真的行动
            while ((solved << 1) <= n && (power + 1) <= maxPower) {
                solved <<= 1;
                power++;
            }
            res.add(content(status, power));
            n -= solved;
            // 以status为起点搞定了solved个ip地址，那么下一块的起点就应该从status+solved开始
            // x.x.x.00001000 可以搞定8个；那么下一块的起点应该是：x.x.x.00001000 + 8 == x.x.x.00010000
            status += solved;
        }
        return res;
    }


}
