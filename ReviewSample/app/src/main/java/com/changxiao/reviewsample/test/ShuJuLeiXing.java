package com.changxiao.reviewsample.test;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/8/1.
 *
 * @version 1.0
 */
public class ShuJuLeiXing {
  public static void main(String args[]) {
    // 字节byte 只能表示一个字节的数据 -128 ~ 127
    // byte b = 300;
    // System.out.println(b);
    // 短整型short 表示两个字节
    // short s = 300;

    // 整型int 表示4个字节
    // int i = 154545454;
    // 长整型 表示8个字节 *数值超过int大小 需要在末尾加L/l来表示为long类型
    long l = 1231854;
    // 浮点型 表示4个字节 必须在末尾加F/f来表示为float类型
    float f = 3.1415926F;
    // 双精度浮点型 表示8个字节, 可以在末尾加D/d来表示为double型

    double d = 15.5;
    //** 如果小数后没有后缀默认为double型
    //** 如果整数后没有后缀默认为int型
    // 字符表示单个任意的符号, 只能表示一个字符
    char c = '1';
    System.out.println(c);
        /*
           在java中char可以表示汉字字符,因为java中采用的是Unicode字符集
               在java中字符还可以用\\uxxxx 来表示unicode字符
                */
    char c1 = '呵';
    System.out.println(c1);
    char c2 = '\u0065';
    System.out.println(c2);
    char c3 = 65;
    System.out.println(c3);
    // 布尔型
    boolean b = true;
    boolean b2 = false;
    // 字符串是引用数据类型,不是基本数据类型
    String str = "我叫郭美美,我有一个好干爹.";
    // + 号
    // 在操作非字符串时,用作相加,一旦其中一端出现字符串,则会将另一端先转成字符串再进行连接
    str = str + ",我不是看见蟑螂不怕不怕的那个郭美美.";
    // str = str + (20 + 30); // xxxx2030
    //str = 20 + 30 + str;
    System.out.println(str);
  }
}
