package Basic.Array;

public class _0219_Code {

    public static void main(String[] args) {
        _0219_Code v = new _0219_Code();
        int[] nums = {1, 2, 3, 1};
        System.out.println(v.containsNearbyDuplicate(nums, 3));
    }

    public boolean containsNearbyDuplicate(int[] nums, int k) {
        for (int i = 0; i < nums.length; i++) {
            int min = Math.max(0, i - k);
            int max = Math.min(nums.length - 1, i + k);
            for (int j = min; j <= max; j++) {
                if (j != i && nums[j] == nums[i]) {
                    return true;
                }
            }
        }
        return false;
    }
}
