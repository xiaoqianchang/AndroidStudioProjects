package com.changxiao.example.timeline.model;

/**
 * Created by Chang.Xiao on 2016/3/19 22:33.
 *
 * @version 1.0
 */
public class TimeLineModel {

    private String name;
    private int age;

    public TimeLineModel(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
