package com.changxiao.mpandroidchartdemo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.changxiao.mpandroidchartdemo.indicator.LineIndicator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.line_chart)
    LineChart mLineChart;

    @Bind(R.id.radar_chart)
    RadarChart mRadarChart;

    @Bind(R.id.tv_near_three_month)
    TextView mNearThreeMonth;
    @Bind(R.id.tv_near_six_month)
    TextView mNearSixMonth;
    @Bind(R.id.tv_near_one_year)
    TextView mNearOneYear;
    @Bind(R.id.tv_near_three_year)
    TextView mNearThreeYear;

    @Bind(R.id.line_indicator)
    LineIndicator mLineIndicator;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);

        // 初始化折线图
        initLineChart();
        // 初始化雷达图
        initRadarChart();
        // 初始化LineIndicator
        initLineIndicator();
    }

    private void initLineChart() {
        //        mLineChart.setOnChartGestureListener(this);
        //        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setDrawGridBackground(false); // 是否显示表格颜色
        mLineChart.setDrawBorders(false); // 是否在折线图上添加边框

        // no description text
        mLineChart.setDescription(""); // 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mLineChart.setTouchEnabled(false); // 设置是否可以触摸旋转

        // enable scaling and dragging
        mLineChart.setDragEnabled(true); // 是否可以拖拽
        mLineChart.setScaleEnabled(true); // 是否可以缩放
        // mLineChart.setScaleXEnabled(true);
        // mLineChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);

        // set an alternative background color
        // mLineChart.setBackgroundColor(Color.GRAY);

        // x-axis limit line
        //        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        //        llXAxis.setLineWidth(4f);
        //        llXAxis.enableDashedLine(10f, 10f, 0f);
        //        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        //        llXAxis.setTextSize(10f);

        // 左边y轴
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setEnabled(true); // 设置左边轴component(轴线、轴值)是否被draw
        leftAxis.setDrawAxisLine(false); // 设置哪边的y轴线是否被draw
        leftAxis.setAxisLineWidth(0f); // 设置轴线宽度
        leftAxis.setAxisLineColor(Color.BLACK); // 设置轴线颜色
        leftAxis.setAxisMaxValue(220f); // 设置y轴最大值
        leftAxis.setAxisMinValue(-220f); // 设置y轴最小值
        leftAxis.setDrawLabels(true); // 设置哪边的y轴值是否被draw
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART); // 设置y轴值的位置在y轴线的内部还是外部
        //        leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 0f, 0f); // 设置背景x轴网格线
        leftAxis.setDrawZeroLine(true); // 设置是否画0轴
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true); // 设置左边轴对应的横线是否被draw
        leftAxis.setGridColor(Color.GRAY); // 设置左边轴对应的横线的颜色
        leftAxis.setGridLineWidth(0f); // 设置左边轴对应的横线的厚度

        // 右边y轴
        mLineChart.getAxisRight().setEnabled(false); // 影藏右边的y轴

        //mLineChart.getViewPortHandler().setMaximumScaleY(2f);
        //mLineChart.getViewPortHandler().setMaximumScaleX(2f);

        // x轴
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setEnabled(true); // 设置x轴component(轴线、轴值)是否被draw
        xAxis.setDrawAxisLine(false); // 设置x轴线是否被draw
        xAxis.setAxisLineWidth(0f); // 设置轴线宽度
        xAxis.setAxisLineColor(Color.BLACK); // 设置轴线颜色
//        xAxis.setAxisMaxValue(100);
//        xAxis.setAxisMinValue(0);
        xAxis.setDrawLabels(true); // 设置是否draw轴值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置x轴值的位置在x轴线的内部还是外部
        xAxis.enableGridDashedLine(10f, 0f, 0f); // 设置背景x轴网格线
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setDrawGridLines(false); // 设置x轴对应的竖线是否被draw
        xAxis.setGridColor(Color.BLACK); // 设置x轴对应的竖线的颜色
//        xAxis.setGridColor(getResources().getColor(R.color.transparent));
        xAxis.setGridLineWidth(0f); // 设置x轴对应的竖线的厚度

        // add data
        setData(3, 100); // X、Y轴的数据范围

        //        mLineChart.setVisibleXRange(20);
        //        mLineChart.setVisibleYRange(20f, AxisDependency.LEFT);
        //        mLineChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mLineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        //        mLineChart.invalidate();

        /*
         * 设置图例，就是那个一组y的value的DataSet标志
         */
        // get the legend (only possible after setting data)
        Legend l = mLineChart.getLegend();
        // modify the legend ...
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setCustom(new int[] {Color.RED, Color.GREEN, Color.parseColor("#45a4aa")}, new String[] {"正常表现", "最好表现", "最差表现"});
        l.setForm(Legend.LegendForm.LINE); // 设置图例的形状
        l.setFormSize(7f); // 设置图例的大小
        l.setTextSize(11f); // size of the label text in pixels min = 6f, max = 24f, default* 10f
        l.setTextColor(Color.BLACK); // 图例text color
        l.setFormToTextSpace(7f); // 设置图例、文字间距
        l.setTypeface(tf);
        l.setEnabled(true); // 设置小图例描述是否可见

        // // dont forget to refresh the drawing
//        mLineChart.invalidate();
    }

    private void initRadarChart() {
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mRadarChart.setDescription(""); // 数据描述

        // enable touch gestures
        mRadarChart.setTouchEnabled(false); // 设置是否可以触摸旋转

        mRadarChart.setWebLineWidth(1.5f); // 设置从中心点出发的轴线宽度
        mRadarChart.setWebLineWidthInner(0.75f); // 设置圆环线的宽度
        mRadarChart.setWebAlpha(100); // 设置射线、圆环线的透明度

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
//        mRadarChart.setMarkerView(mv);

        // add data
        setData();

        mRadarChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        // x轴
        XAxis xAxis = mRadarChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(9f); // 设置圆环外字体显示的值得大小

        // y轴
        YAxis yAxis = mRadarChart.getYAxis();
        yAxis.setEnabled(false); // 设置y轴数值是否被draw
        yAxis.setTypeface(tf);
        yAxis.setLabelCount(5, false); // 设置y轴数值显示(圆环个数)、参数二设置节点处数值是否去零(数值变化而圆环个数是否变化)
        yAxis.setTextSize(9f); // 设置y轴显示的值得大小
        yAxis.setAxisMinValue(0f); // 设置中心数值的起点

        /*
         * 设置标示，就是那个一组y的value的DataSet标志
         */
        // get the legend (only possible after setting data)
        Legend l = mRadarChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART); // 设置小图标的位置
        l.setForm(Legend.LegendForm.SQUARE); // 设置小图标的形状
        l.setEnabled(false); // 设置小角标描述不可见
        l.setTypeface(tf);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    /**
     * 设置曲线的属性
     *
     * @param count X轴范围
     * @param range Y轴范围
     */
    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        // 第一条
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {

            float mult = (range + 1);
            float val = ((float) (Math.random() * mult) + 3) * (Math.random() > 0.5 ? 1 : -1); // 包含负数
//            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);x
            yVals1.add(new Entry(val, i));
        }

        LineDataSet set1;

        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mLineChart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals1);
            mLineChart.getData().setXVals(xVals);
//            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");

            // set1.setFillAlpha(110);
            // set1.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 0f, 0f); // 设置线条的样式(虚线、)
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setLineWidth(1f); // 设置数据点间连线的线的宽度

            // 数据点小圆圈设置
            set1.setDrawCircles(false); // 图标上的数据点不用小圆圈显示
            set1.setCircleColor(Color.BLACK); // 设置数据点颜色
            set1.setCircleRadius(2f); // 设置数据点的半径
            set1.setDrawCircleHole(false); // 设置数据点是否高亮(空心)

            // 设置折线上的数值
            set1.setDrawValues(false); // 设置是否draw value值
            set1.setValueTextSize(7f); // 设置转折点显示的值得大小

            // 曲线设置
            set1.setColor(Color.BLACK); // 设置曲线颜色
            set1.setDrawCubic(false); // 设置是否允许曲线平滑
            set1.setCubicIntensity(0.2f); // 设置折线的平滑度

            // 填充设置
            // 设置为true,数据集应该画满(表面),而不只是像一条线,禁用这将给伟大的性能提升!默认值:false
            set1.setDrawFilled(false); // 设置线图是否填充
            // set fill color
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }
            set1.setFillAlpha(65); // 设置填充线的alpha

        }

        // 第二条
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {

            float mult = (range + 1);
            float val = ((float) (Math.random() * mult) + 3) * (Math.random() > 0.5 ? 1 : -1); // 包含负数
            //            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);x
            yVals2.add(new Entry(val, i));
        }

        LineDataSet set2;

        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set2 = (LineDataSet)mLineChart.getData().getDataSetByIndex(1);
            set2.setYVals(yVals2);
            mLineChart.getData().setXVals(xVals);
//            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set2 = new LineDataSet(yVals2, "DataSet 2");

            // set2.setFillAlpha(110);
            // set2.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            set2.enableDashedLine(10f, 0f, 0f); // 设置线条的样式(虚线、)
            set2.enableDashedHighlightLine(10f, 0f, 0f);
            set2.setLineWidth(1f); // 设置数据点间连线的线的宽度

            // 数据点小圆圈设置
            set2.setDrawCircles(false); // 图标上的数据点不用小圆圈显示
            set2.setCircleColor(Color.BLACK); // 设置数据点颜色
            set2.setCircleRadius(2f); // 设置数据点的半径
            set2.setDrawCircleHole(false); // 设置数据点是否高亮(空心)

            // 设置折线上的数值
            set2.setDrawValues(false); // 设置是否draw value值
            set2.setValueTextSize(7f); // 设置转折点显示的值得大小

            // 曲线设置
            set2.setColor(Color.BLACK); // 设置曲线颜色
            set2.setDrawCubic(false); // 设置是否允许曲线平滑
            set2.setCubicIntensity(0.2f); // 设置折线的平滑度

            // 填充设置
            // 设置为true,数据集应该画满(表面),而不只是像一条线,禁用这将给伟大的性能提升!默认值:false
            set2.setDrawFilled(false); // 设置线图是否填充
            // set fill color
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set2.setFillDrawable(drawable);
            }
            else {
                set2.setFillColor(Color.BLACK);
            }
            set2.setFillAlpha(65); // 设置填充线的alpha

        }

        // 第三条
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {

            float mult = (range + 1);
            float val = ((float) (Math.random() * mult) + 3) * (Math.random() > 0.5 ? 1 : -1); // 包含负数
            //            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);x
            yVals3.add(new Entry(val, i));
        }

        LineDataSet set3;

        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set3 = (LineDataSet)mLineChart.getData().getDataSetByIndex(2);
            set3.setYVals(yVals3);
            mLineChart.getData().setXVals(xVals);
//            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set3 = new LineDataSet(yVals3, "DataSet 3");

            // set3.setFillAlpha(110);
            // set3.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            set3.enableDashedLine(10f, 0f, 0f); // 设置线条的样式(虚线、)
            set3.enableDashedHighlightLine(10f, 0f, 0f);
            set3.setLineWidth(1f); // 设置数据点间连线的线的宽度

            // 数据点小圆圈设置
            set3.setDrawCircles(false); // 图标上的数据点不用小圆圈显示
            set3.setCircleColor(Color.BLACK); // 设置数据点颜色
            set3.setCircleRadius(2f); // 设置数据点的半径
            set3.setDrawCircleHole(false); // 设置数据点是否高亮(空心)

            // 设置折线上的数值
            set3.setDrawValues(false); // 设置是否draw value值
            set3.setValueTextSize(7f); // 设置转折点显示的值得大小

            // 曲线设置
            set3.setColor(Color.BLACK); // 设置曲线颜色
            set3.setDrawCubic(false); // 设置是否允许曲线平滑
            set3.setCubicIntensity(0.2f); // 设置折线的平滑度

            // 填充设置
            // 设置为true,数据集应该画满(表面),而不只是像一条线,禁用这将给伟大的性能提升!默认值:false
            set3.setDrawFilled(false); // 设置线图是否填充
            // set fill color
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set3.setFillDrawable(drawable);
            }
            else {
                set3.setFillColor(Color.BLACK);
            }
            set3.setFillAlpha(65); // 设置填充线的alpha

        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        dataSets.add(set3);

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mLineChart.setData(data);

        // // dont forget to refresh the drawing
        mLineChart.invalidate();

    }

    private String[] mParties = new String[] {
            "择时能力", "选股能力", "基金经理", "行业配置", "风险管理"
    };

    public void setData() {

        float mult = 150;
        int cnt = mParties.length; // 射线条数

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }

//        for (int i = 0; i < cnt; i++) {
//            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
//        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[4]); // 折线颜色
        set1.setFillColor(ColorTemplate.VORDIPLOM_COLORS[4]); // 折线填充颜色
        set1.setDrawFilled(false); // 是否填充
        set1.setLineWidth(2f); // 折线宽度

//        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
//        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]); // 折线颜色
//        set2.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]); // 折线填充颜色
//        set2.setDrawFilled(true); // 是否填充
//        set2.setLineWidth(2f); // 折线宽度

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
//        sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
        data.setDrawValues(false); // 是否draw网状图每个角的值
        data.setValueTypeface(tf);
        data.setValueTextSize(8f);

        mRadarChart.setData(data);

        mRadarChart.invalidate();
    }

    @OnClick({R.id.tv_near_three_month, R.id.tv_near_six_month, R.id.tv_near_one_year, R.id.tv_near_three_year})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_near_three_month:
                setData(3, 100);
                break;
            case R.id.tv_near_six_month:
                setData(6, 100);
                break;
            case R.id.tv_near_one_year:
                setData(10, 100);
                break;
            case R.id.tv_near_three_year:
                setData(30, 100);
                break;
        }
    }

    private void initLineIndicator() {
        // 初始化indicator内容与alert值
        mLineIndicator.setContent("开始", "0.00元", "目标", "8,500元");
        // 初始化indicator开始、结束、当前，并计算progress
//        mLineIndicator.setIndicator(60f, 50f, 55f, "2,000元");
        mLineIndicator.setIndicator(0f, 8500f, 2000f, "2,000元");
    }
}
