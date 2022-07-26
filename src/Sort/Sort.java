package Sort;


public class Sort {

    //    简单插入排序
    public void insert_sort(int[] arr){
        for (int i = 1; i < arr.length; i++) {
            for (int j = i-1; j >=0 && arr[j]>arr[j+1]; j--) {
                int temp = arr[j];
                arr[j] = arr[j+1];
                arr[j+1] = temp;
            }
        }
    }

    // 选择排序
    public void selectSort(int[] arr) {
        if (arr == null || arr.length < 2)
            return;
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                minIndex = arr[j] < arr[minIndex] ? j : minIndex;
            }
            swap(arr, i ,minIndex);
        }
    }

    public void swap(int[] arr, int i, int j){
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }


    //    冒泡排序
    public void bubble_sort(int[] arr){
        for (int i = arr.length-2; i >=0; i--) {
            for (int j = 0; (j <= i) && (arr[j]>arr[j+1]); j++) {
                int temp = arr[j];
                arr[j] = arr[j+1];
                arr[j+1] = temp;
            }
        }
    }
}
