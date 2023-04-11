package utils;

import java.util.HashSet;

// 该类存放一些公用的方法，比如随机生成一个数组，数组的打印等。这些方法在各个算法中都有可能作为辅助验证用，
// 所以集中放在一起
public class arrays {
    // 生成一个随机数组，长度在[0, maxSize]之间，数值在[0, maxVal]之间。允许重复值，并且无序。
    public static int[] randomNoNegativeArr(int maxSize, int maxVal){
        int[] arr = new int[((int) (maxSize * Math.random())) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = randomNoNegativeNum(maxVal);
        }
        return arr;
    }

    // 生成一个随机数组，长度在[0, maxSize]之间，数值在[-maxVal, maxVal]之间。允许重复值，并且无序。
    public static int[] RandomArr(int maxSize, int maxVal){
        int[] arr = new int[((int) (maxSize * Math.random())) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = generateRandomNum(maxVal);
        }
        return arr;
    }


    // 生成一个随机数组，长度在[0, maxSize]之间，数值在[-maxVal, maxVal]之间，不允许重复值。
    public static int[] noRepeatArr(int maxSize, int maxVal){
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

    // 随机生成一个矩阵  行数在[1, maxRow]  列数在[1, maxCol]之间  值在[0, maxVal]之间
    public static int[][] randomMatrix(int maxRow, int maxCol, int maxVal){
        int rows = (int) (Math.random() * maxRow) + 1;
        int cols = (int) (Math.random() * maxCol) + 1;
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++)
                matrix[i][j] = (int) ((maxVal + 1) * Math.random());
        }
        return matrix;
    }


    // 打印矩阵
    public static void printMatrix(int[][] m){
        for (int[] ints : m) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(ints[j] + "\t");
            }
            System.out.println();
        }
    }


    // 生成一个长度固定，但是元素值随机的正整数数组
    public static int[] fixedLenArray(int len, int maxVal){
        int[] res = new int[len];
        for (int i = 0; i < len; i++)
            res[i] = (int) (Math.random() * maxVal) + 1;
        return res;
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

    public static void printArray(double[] arr){
        if (arr == null || arr.length == 0)
            return;
        for (double j : arr) {
            System.out.print(j + "  ");
        }
        System.out.println();
    }


    // 返回一个在 [0, range]的整数
    public static int randomNoNegativeNum(int range){
        return (int) (Math.random() * (range + 1));
    }

    // 返回一个在 [-range, range]的整数
    public static int generateRandomNum(int range){
        return (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
    }

    // 交换数组上i和j位置上的两个值
    public static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 判断两个数组是否完全相等，不仅长度相等，并且对应元素都相等
    public static boolean isSameArr(int[] arr1, int[] arr2){
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
        int[] array = noRepeatArr(10, 30);
        printArray(array);
    }
}
