package Basic.Sort.Exercise;

// leetCode215
// 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
// 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
// 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。

// 1 <= k <= nums.length <= 10^5
// -10^4 <= nums[i] <= 10^4


import java.util.*;

public class TopKMax {


    // 建立小根堆   用系统堆即可   这里是手动实现的，练手感
    public static int findKthLargest(int[] nums, int k) {
        if (nums == null || k < 1 || nums.length < k)
            return Integer.MIN_VALUE;
        int N = nums.length;
        Heap heap = new Heap(k);
        for (int i = 0; i < N; i++) {
            if (heap.size < k)
                heap.insert(nums[i]);
            else if (heap.size == k && nums[i] > heap.peek()) {
                heap.pop();
                heap.insert(nums[i]);
            }
        }
        return heap.peek();
    }



    public static class Heap {
        public int[] heap;
        public int size;
        public int limit;

        public Heap(int limit) {
            heap = new int[limit];
            size = 0;
            this.limit = limit;
        }


        public void insert(int num){
            if (size < limit){
                heap[size++] = num;
                int index = size - 1;
                while (index >= 0 && heap[(index - 1) / 2] > heap[index]){
                    swap(heap, (index - 1) / 2, index);
                    index = (index - 1) / 2;
                }
            }
        }


        public int peek(){
            return heap[0];
        }



        public int pop(){
            int res = heap[0];
            swap(heap, 0, --size);
            heapify(0, size);
            return res;
        }



        private void heapify(int i, int size) {
            int left = i << 1 | 1;
            while (left < size){
                int less = left + 1 < size && heap[left + 1] < heap[left] ? left + 1 : left;
                less = heap[i] < heap[less] ? i : less;
                if (less == i)
                    break;
                swap(heap, i, less);
                i = less;
                left = i << 1 | 1;
            }
        }


        private void swap(int[] arr, int i, int j){
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }
    // =================================================================================================



    // 这里提供另一种方法：基于快排的快速选择
    public static int findKthLargest2(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    private static int quickSelect(int[] nums, int L, int R, int index) {
        int pos = partition(nums, L, R);
        if (pos == index)
            return nums[pos];
        return pos < index ? quickSelect(nums, pos + 1, R, index) :
                quickSelect(nums, L, pos - 1, index);
    }


    // 默认以nums[r]作为pivot  但是要做到随机
    private static int partition(int[] nums, int l, int r) {
        int p = l + (int) (Math.random() * (r - l + 1));
        swap(nums, p, r);
        int pivot = nums[r];
        int B = l - 1;
        for (int i = l; i < r; i++) {
            if (nums[i] <= pivot){
                swap(nums, i, ++B);
            }
        }
        swap(nums, B + 1, r);
        return B + 1;
    }


    private static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    public int[] intersection(int[] nums1, int[] nums2) {
        HashSet<Integer> set1 = new HashSet<>();
        HashSet<Integer> set2 = new HashSet<>();
        for (int elem : nums1)
            set1.add(elem);
        for (int elem : nums2)
            set2.add(elem);
        ArrayList<Integer> intersect = new ArrayList<>();
        for (int elem : set1){
            if (set2.contains(elem))
                intersect.add(elem);
        }
        int[] res = new int[intersect.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = intersect.get(i);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(new Random().nextInt(5));
    }
}
