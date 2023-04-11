package Basic.Tips;

public class BinarySearch {

    // 传统二分搜索法：若target不在有序数组arr中，返回-1；若存在则返回索引
    public static int binarySearch(int[] arr, int target){
        if (arr == null || arr.length == 0)
            return -1;
        int L = 0, R = arr.length - 1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (arr[mid] == target)
                return mid;
            else if (arr[mid] > target) {
                R = mid - 1;
            } else {
                L = mid + 1;
            }
        }
        return -1;
    }


    // 给一个无序数组，里面没有重复的元素，都是不一样的元素。我们定义一个局部最小值：若该元素不是头尾元素，如果它
    // 比左右两边的元素都小，则该元素为局部最小；若该元素为头尾元素，则只需要比它相邻的一个元素小就是局部最小。
    // 返回该数组中的任何一个局部最小值的索引。

    // 该问题说明 二分搜索不一定非得要求数组有序！！！！！
    public static int localMin(int[] arr){
        if (arr == null || arr.length < 2)
            return -1;
        if (arr[0] < arr[1])
            return 0;
        if (arr[arr.length - 1] < arr[arr.length - 2])
            return arr.length - 1;
        // 执行到这里，说明0位置不是局部最小，那就是说0、1位置呈下降趋势，因为不存在重复值；N-1位置也不是局部最小
        // 并且N-2、N-1位置呈现上升趋势
        //
        // \   /    开头下降，结尾上升，并且不存在重复元素，说明任意两个相邻元素不等，那么中间的趋势必然是有局部最小值的
        //
        // 所以就可以二分
        int L = 1, R = arr.length - 2;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            // 0～1下降，mid-1~mid上升，那么该区间内必然有局部最小值
            if (arr[mid] > arr[mid - 1])
                R = mid - 1;
            // mid~mid+1下降，N-2～N-1上升，那么该区间内必然有局部最小值
            else if (arr[mid] > arr[mid + 1]) {
                L = mid + 1;
            } else
                return mid;
        }
        return -1;
    }
    // ===================================================================================================

    // 下面是二分的边界问题

    // 1.一个有序数组，有重复值，找出>k的最左位置
    public static int mostLeftG(int[] arr, int k){
        if (arr == null || arr.length == 0)
            return -1;
        int L = 0, R = arr.length - 1, mid = 0;
        int index = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr[mid] > k){
                R = mid - 1;
                index = mid;
            }
            else
                L = mid + 1;
        }
        return index;
    }

    // 2.一个有序数组，有重复值，找出>=k的最左位置。若不存在则返回-1。
    public static int mostLeftEag(int[] arr, int k){
        if (arr == null || arr.length == 0)
            return -1;
        int L = 0, R = arr.length - 1, mid = 0;
        int index = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr[mid] >= k){
                R = mid - 1;
                index = mid;
            }
            else
                L = mid + 1;
        }
        return index;
    }

    // 3.一个有序数组，有重复值，找出<k的最右位置。若不存在则返回-1。
    public static int mostRightL(int[] arr, int k){
        if (arr == null || arr.length == 0)
            return -1;
        int L = 0, R = arr.length - 1, mid = 0;
        int index = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr[mid] >= k){
                R = mid - 1;
            }
            else {
                L = mid + 1;
                index = mid;
            }
        }
        return index;
    }


    // 4.一个有序数组，有重复值，找出<=k的最右位置。若不存在则返回-1。
    public static int mostRightLae(int[] arr, int k){
        if (arr == null || arr.length == 0)
            return -1;
        int L = 0, R = arr.length - 1, mid = 0;
        int index = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr[mid] > k){
                R = mid - 1;
            }
            else {
                L = mid + 1;
                index = mid;
            }
        }
        return index;
    }
    // 经过上面4个边界问题的分析，可以发现：这四种边界条件下，都是只分成两种情况，不想具体找某个数的时候是分成三种情况的。
    // 比如：当你想找 <=k时，那么情况就自动分成了：1> <=k  2> >k；你希望找的是<=k，那么就在对应的情况下做记录即可。
    // ================================================================================================

    public static void main(String[] args) {
        // 用对数器验证自己写的二叉搜索法是否正确
//        int maxSize = 20, maxVal = 200;
//        for (int i = 0; i < 100000; i++) {
//            int target = (int) ((maxVal + 1) * Math.random());
//            int[] src = arrays.generateRandomArray(maxSize, maxVal);
//            int[] copy = arrays.copyArray(src);
//            if (binarySearch(src, target) >= 0 && Arrays.binarySearch(src, target) >= 0 &&
//                    binarySearch(src, target) != Arrays.binarySearch(src, target)){
//                arrays.printArray(src);
//                System.out.println(target);
//                System.out.println("failed!");
//                return;
//            }
//        }
//        System.out.println("AC");
        int[] arr = {2, 4, 6, 7, 7, 9, 11, 13, 14};
        int k = 15;
        System.out.println(mostLeftG(arr, k));
        System.out.println(mostLeftEag(arr, k));
        System.out.println(mostRightL(arr, k));
        System.out.println(mostRightLae(arr, k));
    }
}
