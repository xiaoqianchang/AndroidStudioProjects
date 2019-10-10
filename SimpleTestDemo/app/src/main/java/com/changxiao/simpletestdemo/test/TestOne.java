package com.changxiao.simpletestdemo.test;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/10/31.
 *
 * @version 1.0
 */

public class TestOne {

    public static void main(String[] args) {
        double plusRate = 85;

        System.out.println(plusRate / 10);
        System.out.println(getRateValue(plusRate));

        SpannableString sp = new SpannableString("ss");
        int end = "ss".indexOf("");
        sp.setSpan(new StyleSpan(Typeface.BOLD), 0, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new RelativeSizeSpan(1.3f), 0, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        System.out.print(sp.toString());
    }

    public static String getRateValue(double plusRate) {
        double v = plusRate / 10;
        String s = String.valueOf(v);
        if (s.contains(".")) {
            String substring = s.substring(s.indexOf(".") + 1);
            if ("0".equals(substring)) {
                return s.substring(0, s.indexOf("."));
            }
        }
        return s;
    }

    public static int getIntByFormat(double d) {
        try {
            BigDecimal input = new BigDecimal(d);
            DecimalFormat _df = new DecimalFormat("######0"); // 四舍五入转为整数
            return Integer.parseInt(_df.format(input));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
