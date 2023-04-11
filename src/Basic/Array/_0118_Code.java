package Basic.Array;

import java.util.ArrayList;
import java.util.List;

public class _0118_Code {
    public static void main(String[] args) {
        _0118_Code v = new _0118_Code();
        List<List<Integer>> generate = v.generate(5);
        for (int i = 0; i < generate.size(); i++) {
            for (int j = 0; j < generate.get(i).size(); j++) {
                System.out.print(generate.get(i).get(j) + "\t");
            }
            System.out.println();
        }
    }

    public List<List<Integer>> generate(int numRows) {
        if (numRows < 1)
            return null;
        List<List<Integer>> root = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 1; j <= i; j++) {
                if (j == 1 || j == i)
                    list.add(1);
                else
                    list.add(root.get(i - 2).get(j - 2) + root.get(i - 2).get(j - 1));
            }
            root.add(list);
        }
        return root;
    }
}
