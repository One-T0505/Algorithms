package Basic.Sort.Exercise;

// leetCode451
// 给定一个字符串 s ，根据字符出现的频率对其进行降序排序。一个字符出现的频率是它出现在字符串中的次数。
// 返回已排序的字符串。如果有多个答案，返回其中任何一个。

// 1 <= s.length <= 5 * 10^5
// s 由大小写英文字母和数字组成

import java.util.HashMap;
import java.util.PriorityQueue;

public class FrequencyDescend {

    public static class Node {
        public char c;
        public int times;

        public Node(char c, int times) {
            this.c = c;
            this.times = times;
        }
    }


    public static String frequencySort(String s) {
        char[] chars = s.toCharArray();
        int N = chars.length;
        // 仅按频率从高到低排序
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> (b.times - a.times));
        HashMap<Character, Node> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            char cur = chars[i];
            Node node = null;
            if (!map.containsKey(cur)){
                node = new Node(cur, 1);
            } else {
                node = map.get(cur);
                map.remove(cur);
                heap.remove(node);
                node.times++;
            }
            map.put(cur, node);
            heap.add(node);
        }
        StringBuilder sb = new StringBuilder();
        while (!heap.isEmpty()){
            Node cur = heap.poll();
            sb.append(String.valueOf(cur.c).repeat(cur.times));
        }
        return sb.toString();
    }
}
