package com.changxiao.reviewsample.test;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/8/10.
 *
 * @version 1.0
 */
public class Test {

  public static void main(String args[]) {
    Student stu1 = new Student();
    stu1.setNumber(12345);
    Student stu2 = stu1;
    stu2.setNumber(54321);
    System.out.println("学生1:" + stu1.getNumber());
    System.out.println("学生2:" + stu2.getNumber());
  }
}
