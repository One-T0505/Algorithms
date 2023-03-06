package String;

public class Manacher {
    public static int manacher(String s){
       if (s == null || s.length() == 0)
           return 0;
        char[] ori = process(s);
        int N = ori.length;
        int[] radius = new int[N];    // 回文半径数组
        int R = -1, C = -1;             // 这里的R指的是回文最右边界的再往右一个位置， 中心点C
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            // 先确定当前元素至少的回文区域。如果R > i，不管落入4种情况的哪一种，都是
            // Math.min(radius[2 * C - i], R - i)
            radius[i] = i <= R ? Math.min(radius[2 * C - i], R - i + 1) : 1;

            // 4种情况中，只有两种需要左右扩充，但是如果将扩充算法写到对应的情况下会代码冗余，所以写成如下这样，4种情况
            // 都会进入该扩充算法，但是原有的不需要扩充的两种情况进入该循环后会直接失败，所以效果等价。
            while (i + radius[i] < N && i - radius[i] > -1){
                if (ori[i + radius[i]] == ori[i - radius[i]])
                    radius[i]++;
                else
                    break;
            }
            // 判断是否需要更新R，C
            if (i + radius[i] - 1 > R){
                R = i + radius[i] - 1;
                C = i;
            }
            // 记录当前最大值
            max = Math.max(max, radius[i]);
        }
        // 经发现：标准流字符串中的回文半径-1，就可以得到原始字符串中的回文直径
        return max - 1;
    }

    // 将字符串扩充为辨准处理串：1221 --> #1#2#2#1#
    private static char[] process(String s){
        char[] res = new char[2 * s.length() + 1];
        char[] ori = s.toCharArray();
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 1 ? ori[index++] : '#';
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(manacher("aacbdbccbd"));
    }
}
