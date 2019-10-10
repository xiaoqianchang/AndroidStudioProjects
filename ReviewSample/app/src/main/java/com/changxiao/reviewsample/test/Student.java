package com.changxiao.reviewsample.test;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/8/10.
 *
 * @version 1.0
 */
public class Student implements Cloneable {

  private int number;

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  @Override
  protected Object clone() {
    Student student = null;
    try {
      student = (Student) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return student;
  }
}
