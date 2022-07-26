public class MagicMethod {

    //    对数器：为了测试用的一种，生成随机长度，随机数值的数组
    public int[] getRandomArray(int maxsize, int maxvalue){
        // Math.random() -> 返回一个 [0,1) 等概率的小数
        // Math.random()*N -> 返回一个 [0,N) 等概率的小数
        // (int)(Math.random()*N) -> 返回一个 [0,N-1] 等概率的整数

        int[] arr =new int[(int) (Math.random()*(maxsize+1))];  //随机生成一个长度为 [0,maxsize] 的数组
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random()*(maxvalue+1)) - (int) (Math.random()*maxvalue);
        }
        return arr;
    }
}
