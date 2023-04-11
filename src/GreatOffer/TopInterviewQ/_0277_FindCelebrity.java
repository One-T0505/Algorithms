package GreatOffer.TopInterviewQ;


// 给定一个数字N，表示有多少个人，人的编号从0～N-1。现在给你一个函数know(i, j)，i和j是两个人的编号，know返回的结果
// 是i是否认识j，这种关系是单向的，j认不认识i必须得调用know(j, i)才能知道。
// 现在给出明星的定义：
//  1.所有人中除了明星外，其余所有人都认识明星
//  2.明星不认识所有人
// 这样的人称作明星。编写一个算法找出这N个人中是否含有明星，若有，返回编号，若没有，则返回-1

public class _0277_FindCelebrity {

    // 从上述的明星的定义中，N个人里最多只可能有一个明星。

    public static int searchCelebrity(int N) {
        int cand = 0;  // 明星候选人编号
        for (int i = 0; i < N; i++) {
            // 如果cand认识i，那么cand就不可能是明星了；所以要让候选变成i
            // 如果cand不认识i，那么i就不可能是明星了，所以cand继续维持原样
            if (konws(cand, i))
                cand = i;
        }
        // 到这里时，cand是唯一可能成明星的人
        // 下面的for循环要验证：cand是是否确实不认识0～cand-1
        // 不用验证cand是否认识cand+1～N-1  因为cand必然不认识后面这些，如果认识的话，那么cand的值就不会是现在的值了
        for (int i = 0; i < cand; i++) {
            if (konws(cand, i))
                return -1;
        }
        // 验证所有人是否确实认识cand
        for (int i = 0; i < N; i++) {
            if (!konws(i, cand))
                return -1;
        }
        return cand;
    }


    // 这个方法不要提交，因为系统默认会有know这个函数，这里写这个函数只是为了不报错
    // 并且如果传入的两个参数都是自己，那么该函数返回true，表示自己认识自己
    private static boolean konws(int i, int j) {
        return true;
    }

}
