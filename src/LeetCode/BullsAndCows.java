package LeetCode;

/**
 * ymy
 * 2023/4/3 - 14 : 49
 **/


// leetCode299
// 你在和朋友一起玩 猜数字（Bulls and Cows）游戏，该游戏规则如下：
// 写出一个秘密数字，并请朋友猜这个数字是多少。朋友每猜测一次，你就会给他一个包含下述信息的提示：
//  1.猜测数字中有多少位属于数字和确切位置都猜对了（称为 "Bulls"，公牛），
//  2.有多少位属于数字猜对了但是位置不对（称为 "Cows"，奶牛）。也就是说，这次猜测中有多少位非公
//    牛数字可以通过重新排列转换成公牛数字。
// 给你一个秘密数字 secret 和朋友猜测的数字 guess ，请你返回对朋友这次猜测的提示。
// 提示的格式为 "xAyB" ，x 是公牛个数， y 是奶牛个数，A 表示公牛，B 表示奶牛。
// 请注意秘密数字和朋友猜测的数字都可能含有重复数字。


// 1 <= secret.length, guess.length <= 1000
// secret.length == guess.length
// secret 和 guess 仅由数字组成

public class BullsAndCows {

    // 题面比较复杂，一定要去官方给的例子
    public static String getHint(String secret, String guess) {
        if (secret == null || secret.length() == 0 || guess == null || guess.length() == 0 ||
            secret.length() != guess.length())
            return null;
        char[] sec = secret.toCharArray();
        char[] gue = guess.toCharArray();
        int[] canS = new int[10];
        int[] canG = new int[10];
        int N = sec.length;
        int bulls = 0, cows = 0;
        for (int i = 0; i < N; i++) {
            if (sec[i] == gue[i])
                bulls++;
            else {
                canS[sec[i] - '0']++;
                canG[gue[i] - '0']++;
            }
        }
        for (int i = 0; i < 10; i++) {
            cows += Math.min(canS[i], canG[i]);
        }
        return bulls + "A" + cows + "B";
    }


    public static void main(String[] args) {
        String sec = "1123";
        String gue = "0111";
        System.out.println(getHint(sec, gue));
    }
}
