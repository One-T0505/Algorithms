package Basic.Array;

import java.util.ArrayList;
import java.util.HashMap;

// 假设 Andy 和 Doris 想在晚餐时选择一家餐厅，并且他们都有一个表示最喜爱餐厅的列表，每个餐厅的名字用字符串表示。
//你需要帮助他们用最少的索引和找出他们共同喜爱的餐厅。 如果答案不止一个，则输出所有答案并且不考虑顺序。 你可以假设答案总是存在。
// 输入: list1 = ["Shogun", "Tapioca Express", "Burger King", "KFC"]，
//      list2 = ["Piatti", "The Grill at Torrey Pines", "Hungry Hunter Steakhouse", "Shogun"]
// 输出: ["Shogun"]  解释: 他们唯一共同喜爱的餐厅是“Shogun”。
// Input: list1 = ["happy","sad","good"], list2 = ["sad","happy","good"]
//Output: ["sad","happy"]
//Explanation: There are three common strings:
//"happy" with index sum = (0 + 1) = 1.
//"sad" with index sum = (1 + 0) = 1.
//"good" with index sum = (2 + 2) = 4.
//The strings with the least index sum are "sad" and "happy".

public class _0599_Code {
    public String[] findRestaurant(String[] list1, String[] list2) {
        if (list1 == null || list2 == null)
            return null;
        HashMap<String, Integer> hashMap = new HashMap<>();
        ArrayList<String> res = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < list1.length; i++)
            hashMap.put(list1[i], i);
        for (int i = 0; i < list2.length; i++) {
            if (hashMap.containsKey(list2[i])) {
                int sum = hashMap.get(list2[i]);
                if (sum + i < min) {
                    res.clear();
                    res.add(list2[i]);
                    min = sum + i;
                } else if (sum + i == min) {
                    res.add(list2[i]);
                }
            }
        }
        return res.toArray(new String[res.size()]);
    }
}
