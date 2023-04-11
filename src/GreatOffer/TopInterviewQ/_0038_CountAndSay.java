package GreatOffer.TopInterviewQ;


public class _0038_CountAndSay {

    public static String countAndSay(int n) {
        if (n < 1)
            return "";
        if (n == 1)
            return "1";
        char[] last = countAndSay(n - 1).toCharArray();
        int N = last.length;
        StringBuilder sb = new StringBuilder();
        int times = 1;
        for (int i = 1; i < N; i++) {
            if (last[i] == last[i - 1])
                times++;
            else {
                sb.append(times).append(last[i - 1]);
                times = 1;
            }
        }
        sb.append(times).append(last[N - 1]);
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(countAndSay(0));
    }
}
