package com.humming.pjmember.viewutils;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.humming.pjmember.R;

import java.util.ArrayList;

/**
 * Created by Elvira on 2017/9/13.
 */

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private PieChart pieChart;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        pieChart = findViewById(R.id.dialog_statistics__pie);
        initPieChart(pieChart);
//        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface) 每次 MarkerView 重绘此方法都会被调用，并为您提供更新它显示的内容的机会
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //这里就设置你想显示到makerview上的数据，Entry可以得到X、Y轴坐标，也可以e.getData()获取其他你设置的数据
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

//            tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    private void initPieChart(PieChart pieChart) {
        //使用百分比显示
        pieChart.setUsePercentValues(true);
        //设置图图表的描述
        pieChart.getDescription().setEnabled(false);
        //设置图表上下左右的偏移，类似于外边距
//        pieChart.setExtraOffsets(5, 10, 5, 5);
        //设置阻尼系数,范围在[0,1]之间,越小饼状图转动越困难
//        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置PieChart中间文字的内容
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("总费用");
        //设置字体大小
        pieChart.setCenterTextSize(6f);
        //是否要将PieChart设为一个圆环状/是否绘制饼状图中间的圆
        pieChart.setDrawHoleEnabled(true);
        //设置PieChart中间圆的颜色
        pieChart.setHoleColor(Color.WHITE);
        //饼状图中间的圆的半径大小
//        pieChart.setHoleRadius(58f);
        //设置中间圆的半透明圆环
//        pieChart.setTransparentCircleRadius(5f);
        //设置图表初始化时第一块数据显示的位置
        pieChart.setRotationAngle(90f);
        pieChart.setRotationEnabled(true);//设置可以手动旋转
        pieChart.setUsePercentValues(true);//显示成百分比

        // 设置 pieChart 图表Item文本属性
        pieChart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        pieChart.setEntryLabelColor(Color.WHITE);       //设置pieChart图表文本字体颜色
        pieChart.setEntryLabelTextSize(10f);            //设置pieChart图表文本字体大小


        Legend mLegend = pieChart.getLegend();
        mLegend.setForm(Legend.LegendForm.NONE);//Line线性 square

        initPieDate(3, 100, pieChart);
    }


    private void initPieDate(int count, float range, PieChart pieChart) {
        // 遍历饼状图
//        List<String> mXList = new ArrayList<String>();
//        for (int i = 0; i < count; i++) {
//            mXList.add("业绩所占比例:" + (i + 1));//饼块上显示成业绩比例1 显示成业绩比例2 显示成业绩比例3 显示成业绩比例4
//        }
        ArrayList<PieEntry> list = new ArrayList<>();
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为16:16:32:36
         * 所以 16代表的百分比就是16%
         */
        float quarterly_one = 30;
        float quarterly_two = 30;
        float quarterly_three = 40;

        list.add(new PieEntry(quarterly_one, 0));
        list.add(new PieEntry(quarterly_two, 1));
        list.add(new PieEntry(quarterly_three, 2));
        //y轴集合
        PieDataSet set = new PieDataSet(list, "");
        set.setSliceSpace(0f);//设置饼状之间的间隙
        ArrayList<Integer> mColorIntegers = new ArrayList<Integer>();
        //饼状的颜色
        mColorIntegers.add(Color.rgb(54, 93, 254));
        mColorIntegers.add(Color.rgb(227, 61, 181));
        mColorIntegers.add(Color.rgb(32, 143, 255));
        //设置颜色集
        set.setColors(mColorIntegers);
        //选中态多出的长度
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        float px = 5 * (dm.densityDpi / 160f);
//        set.setSelectionShift(px);
        PieData pieData = new PieData(set);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueFormatter(new PercentFormatter());//设置显示 % 号
        pieData.setValueTextSize(8f);//设置百分比的字号

        pieChart.setData(pieData);

    }

    /**
     * offset 是以點到的那個點作為 (0,0) 中心然後往右下角畫出來 该方法是让markerview现实到坐标的上方
     * 所以如果要顯示在點的上方
     * X=寬度的一半，負數
     * Y=高度的負數
     */
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
