public class BloomFilter {

    public static void main(String[] args) {
        // arr有10个int型元素，每个int是4个B=32b，所以该数组一共可表示320位
        // arr[0] 可表示 0 -- 31 位
        // arr[1] 可表示 32 -- 63 位
        int[] arr = new int[10];

        // 1.查询178位的状态
        int i = 178;
        int elementIndex = 178 / 32;  // 算出是在哪个元素
        int bitIndex = 178 % 32;    // 算出是该元素的第几位
        // 得到178位的值 arr[elementIndex] >> bitIndex 后，最低位就是178位，再和1做与操作
        int state = ((arr[elementIndex] >> bitIndex) & 1);

        // 2.将178位的值改为1
        arr[elementIndex] = arr[elementIndex] | (1 << bitIndex);

        // 3.将178位的值改为0
        arr[elementIndex] = arr[elementIndex] & (~(1 << bitIndex));
    }
}
