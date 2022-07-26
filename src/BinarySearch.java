public class BinarySearch {
    // 给一个无序数组，里面没有重复的元素，都是不一样的元素。我们定义一个局部最小值：若该元素不是头尾元素，如果它
    // 比左右两边的元素都小，则该元素为局部最小；若该元素为头尾元素，则只需要比它相邻的一个元素小就是局部最小。
    // 返回该数组中的一个局部最小值的索引。

    // 该问题说明 二分搜索不一定非得要求数组有序！！！！！

    public static int localMin(int[] arr){
        if (arr.length == 0)
            return -1;
        if (arr.length == 1 || arr[0] < arr[1])
            return 0;
        if (arr[arr.length - 1] < arr[arr.length - 2])
            return arr.length - 1;
        int low = 1, high = arr.length - 2, mid = 0;
        while (low <= high) {
            mid = (low + high)/2;
            if (arr[mid] > arr[mid - 1])
                high = mid - 1;
            else if (arr[mid] > arr[mid + 1]) {
                low = mid + 1;
            } else
                return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = {9, 2, 4, 3, 7, 11};
        System.out.println(BinarySearch.localMin(arr));
    }
}
