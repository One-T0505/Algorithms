
// 比较器
//  1.比较器的实质是重载比较运算符
//  2.比较器可以很好地应用在特殊标准的排序上
//  3.基本类型是默认完成了比较的功能：可以是实现 Comparator接口，也可以是继承 Comparable 类
//  4.当实现 Comparator接口时，在覆盖compar方法时：compare(T o1, T o2)
//      1.如果你认为o1 > o2 则应返回负数
//      2.如果你认为o1 < o2 则应返回正数
//      2.如果你认为o1 = o2 则应返回0

import java.util.Arrays;
import java.util.Comparator;

public class comparator {

    public static void main(String[] args) {
        Student ym = new Student(1, "ym", 36);
        Student lss = new Student(2, "lss", 37);
        Student sze = new Student(3, "sze", 24);
        Student cg = new Student(4, "cg", 27);
        Student angelababy = new Student(5, "Angelababy", 34);
        Student tly = new Student(6, "tly", 38);
        Student[] students = new Student[] {ym, lss, cg, sze, angelababy, tly};

        // 举例：定义Student类，如果两个学生比较，按照 age 升序排列
        Arrays.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.age - o2.age;
            }
        });
        for (Student s : students) System.out.println(s.toString());
    }
}




class Student{
    public int id;
    public String name;
    public int age;

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "[ " + this.id + " " + this.name + " " + this.age + " ]\n";
    }
}