package utils;

import java.util.HashSet;

// 该类存放一些公用的方法，比如随机生成一个数组，数组的打印等。这些方法在各个算法中都有可能作为辅助验证用，
// 所以集中放在一起
public class arrays {
    // 生成一个随机数组，长度在[0, maxSize]之间，数值在[0, maxVal]之间。允许重复值，并且无序。
    public static int[] generateRandomArray(int maxSize, int maxVal){
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    // 生成一个随机数组，长度在[0, maxSize]之间，数值在[-maxVal, maxVal]之间，不允许重复值。
    public static int[] noRepeatArray(int maxSize, int maxVal){
        int[] arr = new int[(int) (Math.random() * (maxSize + 1))];
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            int val = (int) (Math.random() * (maxVal + 1)) - (int) (Math.random() * (maxVal + 1));
            if (!set.contains(val)){
                set.add(val);
                arr[i] = val;
            }else {
                while (set.contains(val))
                    val = (int) (Math.random() * (maxVal + 1)) - (int) (Math.random() * (maxVal + 1));
                set.add(val);
                arr[i] = val;
            }
        }
        return arr;
    }

    // 传入一个源数组，返回一模一样的新数组
    public static int[] copyArray(int[] src){
        int[] des = new int[src.length];
        System.arraycopy(src, 0, des, 0, src.length);
        return des;
    }

    // 数组的展示
    public static void printArray(int[] arr){
        if (arr == null || arr.length == 0)
            return;
        for (int j : arr) {
            System.out.print(j + "  ");
        }
        System.out.println();
    }

    // 返回一个在 [0, range]的整数
    public static int generateRandomNum(int range){
        return (int) (Math.random() * (range + 1));
    }

    // 交换数组上i和j位置上的两个值
    public static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 判断两个数组是否完全相等，不仅长度相等，并且对应元素都相等
    public static boolean isSameArray(int[] arr1, int[] arr2){
        if (arr1 == null && arr2 == null)
            return true;
        if (arr1 == null || arr2 == null)
            return false;
        // 运行到这里说明两个数组都不为空
        if (arr1.length >= 1 && arr2.length >= 1 && arr1.length == arr2.length){
            int len = arr1.length;
            boolean flag = true;
            for(int i = 0; i < len; i++) {
                if (arr1[i] != arr2[i]) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }else
            return false;
    }

    public static void main(String[] args) {
        int[] array = noRepeatArray(10, 30);
        printArray(array);
    }
}