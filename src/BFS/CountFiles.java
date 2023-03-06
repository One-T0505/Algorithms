package BFS;

// 给定一个文件目录的路径，写一个函数统计这个目录下所有的文件数量并返回
// 隐藏文件也算，但是文件夹不算.

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class CountFiles {

    // 该方法连隐藏文件也会一起统计.利用队列实现BFS，只将文件夹放入队列
    public static int countAllFiles(String path){
        if (path == null)
            return 0;
        File root = new File(path);
        if (!root.isDirectory() && !root.isFile())
            return 0;
        if (root.isFile())
            return 1;
        int res = 0;
        Queue<File> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            File poll = queue.poll();
            if (poll.isFile())
                res++;
            else if (poll.isDirectory()) {
                for (File son : poll.listFiles()){
                    if (son.isFile())
                        res++;
                    else if (son.isDirectory()) {
                        queue.add(son);
                    }
                }
            }
        }
        return res;
    }


    public static void main(String[] args) {
        System.out.println(countAllFiles("/Users/ymy/Downloads"));
    }
}
