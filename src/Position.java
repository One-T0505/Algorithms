public class Position {
    // 给一个数组，其长度为2n，里面有n个奇数，n个偶数。现在让偶数位上放置偶数，奇数位上放奇数。
    // 思路：建立两个指针分别指向偶数位0和奇数位1，从尾遍历数组，当前元素如果是奇数，则和奇数指针处的值交换
    //      如果是偶数，则和偶数指针处的值交换
    public static void position(int[] arr){
        int even = 0, odd = 1;
        int N = arr.length;
        while (even < N && odd < N){
            if (arr[N - 1] % 2 == 0){
                swap(arr, even, N - 1);
                even += 2;
            } else {
                swap(arr, odd, N - 1);
                odd += 2;
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    public static void main(String[] args) {
        int[] arr = {13, 7, 5, 22, 8, 12, 9, 20};
        position(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
    }
}
