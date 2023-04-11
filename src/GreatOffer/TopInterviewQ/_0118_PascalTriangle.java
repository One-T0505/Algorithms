package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.List;

// 杨辉三角

public class _0118_PascalTriangle {

    public List<List<Integer>> generate(int numRows) {
        if (numRows < 1)
            return null;
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> init = new ArrayList<>();
        init.add(1);
        res.add(init);

        for (int i = 1; i < numRows; i++) {
            List<Integer> layer = new ArrayList<>();
            for (int j = 0; j < i + 1; j++) {
                if (j == 0 || j == i)
                    layer.add(1);
                else
                    layer.add(res.get(i - 1).get(j) + res.get(i - 1).get(j - 1));
            }
            res.add(layer);
        }
        return res;
    }
}
