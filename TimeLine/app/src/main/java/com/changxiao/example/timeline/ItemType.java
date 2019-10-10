package com.changxiao.example.timeline;

/**
 * Created by Chang.Xiao on 2016/3/19 22:59.
 *
 * @version 1.0
 */
public enum ItemType {
    // 利用构造函数传参
    NORMAL(0),
    START(1),
    END(2),
    ATOM(3); // 只有一条数据---前后都没有轴

    // 定义私有变量
    private final int mCode;

    // 构造函数，枚举类型只能为私有
    private ItemType(int _nCode) {
        this.mCode = _nCode;
    }

    public int getValue() {
        return mCode;
    }

    @Override
    public String toString() {
        return String.valueOf(mCode);
    }
}
