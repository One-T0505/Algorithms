package TopInterviewQuestions;

import java.util.ArrayList;
import java.util.List;

// 杨辉三角II

public class _0119_PascalTriangleII {

    public List<Integer> getRow(int rowIndex) {
        if (rowIndex < 0)
            return null;
        ArrayList<Integer> res = new ArrayList<>();
        res.add(1);

        for (int i = 1; i <= rowIndex; i++) {
            int pre = 1;
            int cur = 1;
            for (int j = 1; j < res.size(); j++) {
                cur = res.get(j);
                res.set(j, pre + cur);
                pre = cur;
            }
            res.add(1);
        }
        return res;
    }


    // 简化方法
    public List<Integer> getRow2(int rowIndex) {
        if (rowIndex < 0)
            return null;
        ArrayList<Integer> res = new ArrayList<>();

        for (int i = 0; i <= rowIndex; i++) {

            for (int j = res.size() - 1; j > 0; j--) {
                res.set(j, res.get(j) + res.get(j - 1));
            }
            res.add(1);
        }
        return res;
    }
}
