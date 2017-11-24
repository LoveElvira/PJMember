package com.humming.pjmember.viewutils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.humming.pjmember.R;
import com.humming.pjmember.viewutils.roundview.RoundedImageView;
import com.pjqs.dto.statistics.StatisticsCompanyBean;
import com.pjqs.dto.statistics.StatisticsFacilityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/13.
 */

public class MyMarkerView extends MarkerView {

    private PieChart pieChart;
    private LinearLayout layout;
    private List<StatisticsCompanyBean> companyBeanList;
    private ArrayList<Integer> colors;

    public MyMarkerView(Context context, int layoutResource, List<StatisticsCompanyBean> companyBeanList) {
        super(context, layoutResource);
        this.companyBeanList = companyBeanList;
        pieChart = findViewById(R.id.dialog_statistics__pie);
        layout = findViewById(R.id.dialog_statistics__layout);
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
//            initPieChart(pieChart, companyBeanList.get(Integer.parseInt(ce.getX() + "")).getFacilityBeen());
            Log.i("ee", "ce:" + ce.getX());
//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            Log.i("ee", "e:" + (int) e.getX() + companyBeanList.get((int) (e.getX())).getFacilityBeen().size());
            initPieDate(pieChart, companyBeanList.get((int) (e.getX())).getFacilityBeen(), Float.parseFloat(companyBeanList.get((int) (e.getX())).getNum()));
            pieChart.invalidate();
//            notify();
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
        pieChart.setExtraOffsets(0, 0, 0, 0);
        pieChart.setMinOffset(0);
        //设置阻尼系数,范围在[0,1]之间,越小饼状图转动越困难
//        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置PieChart中间文字的内容
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("总设施");
        //设置字体大小
        pieChart.setCenterTextSize(8f);
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
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mLegend.setDrawInside(false);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(0f);
        mLegend.setYOffset(0f);

    }


    private void initPieDate(PieChart pieChart, List<StatisticsFacilityBean> facilityBeanList, float total) {

        colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        // 遍历饼状图
//        List<String> mXList = new ArrayList<String>();
//        for (int i = 0; i < count; i++) {
//            mXList.add("业绩所占比例:" + (i + 1));//饼块上显示成业绩比例1 显示成业绩比例2 显示成业绩比例3 显示成业绩比例4
//        }
        ArrayList<PieEntry> list = new ArrayList<>(facilityBeanList.size());
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为16:16:32:36
         * 所以 16代表的百分比就是16%
         */
        layout.removeAllViews();
        for (int i = 0; i < facilityBeanList.size(); i++) {
            list.add(new PieEntry((Float.parseFloat(facilityBeanList.get(i).getNum()) / total) * 100, i));
            layout.addView(initLayout(facilityBeanList.get(i), i));
        }

        //y轴集合
        PieDataSet set = new PieDataSet(list, "");
        set.setSliceSpace(2f);//设置饼状之间的间隙
//        ArrayList<Integer> mColorIntegers = new ArrayList<Integer>();
//        //饼状的颜色
//        mColorIntegers.add(Color.rgb(54, 93, 254));
//        mColorIntegers.add(Color.rgb(227, 61, 181));
//        mColorIntegers.add(Color.rgb(32, 143, 255));

        set.setColors(colors);
        //设置颜色集
//        set.setColors(mColorIntegers);
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

    private View initLayout(StatisticsFacilityBean facilityBean, int position) {
        View view = inflate(pieChart.getContext(), R.layout.item_dialog_statistics, null);
        RoundedImageView image = view.findViewById(R.id.item_dialog_statistics__image);
        TextView name = view.findViewById(R.id.item_dialog_statistics__name);
        TextView num = view.findViewById(R.id.item_dialog_statistics__num);

        image.setBackgroundColor(colors.get(position));
        name.setText(facilityBean.getFacilityName());
        num.setText(facilityBean.getNum());

        return view;
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
