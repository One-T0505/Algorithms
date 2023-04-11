package Basic.Array;

import java.util.Arrays;

public class _0035_Code {

    public static void main(String[] args) {
        _0035_Code v = new _0035_Code();
    }

    public int searchInsert(int[] nums, int target) {
        if (Arrays.binarySearch(nums, target) < 0)
            return -(Arrays.binarySearch(nums, target) + 1);
        else
            return Arrays.binarySearch(nums, target);
    }
}
