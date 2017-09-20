package com.humming.pjmember.activity.statistics;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.TimeAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.bean.TimeModel;
import com.humming.pjmember.viewutils.MyMarkerView;
import com.humming.pjmember.viewutils.MyXAxisRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/8.
 * 统计中心
 */

public class StatisticsActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //头部时间
    private TextView time;

    private TimeAdapter adapter;

    private List<TimeModel> timeModelList;

    //项目折线图
    private LineChart projectLineChart;
    //现场人数折线图
    private LineChart peopleLineChart;
    //突发事故折线图
    private LineChart accidentLineChart;
    //费用饼图
    private PieChart pricePieChart;
    //养护费用
    private TextView maintainPrice;
    //人员费用
    private TextView peoplePrice;
    //维修费用
    private TextView repairPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("统计中心");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        time = (TextView) findViewById(R.id.item_time_top__time);
        time.setVisibility(View.VISIBLE);
        listView = (RecyclerView) findViewById(R.id.item_time_top__listview);

        projectLineChart = (LineChart) findViewById(R.id.statistics_content__project_line);
        peopleLineChart = (LineChart) findViewById(R.id.statistics_content__people_line);
        accidentLineChart = (LineChart) findViewById(R.id.statistics_content__accident_line);
        pricePieChart = (PieChart) findViewById(R.id.statistics_content__price_pie);
        maintainPrice = (TextView) findViewById(R.id.statistics_content__price_maintain);
        peoplePrice = (TextView) findViewById(R.id.statistics_content__price_people);
        repairPrice = (TextView) findViewById(R.id.statistics_content__price_repair);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        listView.setLayoutManager(gridLayoutManager);

        timeModelList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            TimeModel model = new TimeModel();
            if (i == 0) {
                model.setSelect(true);
            } else {
                model.setSelect(false);
            }
            model.setTime((i + 5) + "");
            timeModelList.add(model);
        }

        adapter = new TimeAdapter(timeModelList);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        initLineChart(projectLineChart);
        initLineChart(peopleLineChart);
        initLineChart(accidentLineChart);
        initPieChart(pricePieChart);
        leftArrow.setOnClickListener(this);
    }

    private void initLineChart(LineChart lineChart) {
//        lineChart.setOnChartGestureListener(this);
//        lineChart.setOnChartValueSelectedListener(this);
        //chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawGridBackground(false);
        //禁止绘制图表边框的线
        lineChart.setDrawBorders(false);
        //设置图表的描述 false 不显示
        lineChart.getDescription().setEnabled(false);

        // 设置是否可以触摸
        lineChart.setTouchEnabled(true);

        // 是否可以拖拽
        lineChart.setDragEnabled(true);
        // 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleEnabled(true);
        //是否可以缩放 仅x轴
        // mChart.setScaleXEnabled(true);
        //是否可以缩放 仅y轴
        // mChart.setScaleYEnabled(true);

        //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setPinchZoom(true);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.dialog_statistics);
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = lineChart.getXAxis();
        //设置轴启用或禁用 如果禁用以下的设置全部不生效
        xAxis.setEnabled(true);
        //是否绘制轴线
        xAxis.setDrawAxisLine(true);
        //spaceLength控制线之间的空间
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
//        xAxis.setAvoidFirstLastClipping(true);
        //设置x轴标签的旋转角度
//        xAxis.setLabelRotationAngle(10f);
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //获取左边的轴线
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置图表右边的y轴禁用
        leftAxis.setEnabled(true);
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        //设置Y轴的最大值
        leftAxis.setAxisMaximum(100f);
        //设置Y轴的最小值
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        //设置网格线为虚线效果
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //是否绘制0所在的网格线
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        //获取右边的轴线 让其不显示
        lineChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setLineData(20, 100, lineChart);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);
        //从左到右绘制图形，参数是int类型的 持续时间
        lineChart.animateX(2500);
        //mChart.invalidate();

        // 设置折线的描述的样式（默认在图表的左下角）
        Legend l = lineChart.getLegend();
        //正方形，圆形或线
        l.setForm(Legend.LegendForm.NONE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        l.setWordWrapEnabled(true);
        //设置图例的位置
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        // // dont forget to refresh the drawing
        // mChart.invalidate();

    }

    private void setLineData(int count, float range, LineChart lineChart) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val, getResources().getDrawable(R.mipmap.address)));
        }

        //LineDataSet每一个对象就是一条连接线
        LineDataSet set1;

        //判断图表中原来是否有数据
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            //获取数据
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            //刷新数据
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(values, "");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            //设置该线的颜色
            set1.setColor(ContextCompat.getColor(getBaseContext(), R.color.pink));
            //设置每个点的颜色
//            set1.setCircleColor(getResources().getColor(R.color.pink));
            //设置线宽
            set1.setLineWidth(1f);
            //设置焦点圆心的大小
//            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
            //设置是否禁用范围背景填充
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);
            //是否显示原点上面的数字
            set1.setDrawValues(false);
            //是否显示原点
            set1.setDrawCircles(false);
            // 设置平滑曲线模式
            //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                //设置范围背景填充
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    private void initPieChart(PieChart pieChart) {
        //使用百分比显示
        pieChart.setUsePercentValues(true);
        //设置图图表的描述
        pieChart.getDescription().setEnabled(false);
        //设置图表上下左右的偏移，类似于外边距
//        pieChart.setExtraOffsets(5, 10, 5, 5);
        //设置PieChart中间文字的内容
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("总费用");
        //设置字体大小
        pieChart.setCenterTextSize(15);
        //是否要将PieChart设为一个圆环状
        pieChart.setDrawHoleEnabled(true);
        //设置PieChart中间圆的颜色
        pieChart.setHoleColor(Color.WHITE);
        //设置中间圆的半透明圆环
        pieChart.setTransparentCircleRadius(10f);
        //设置图表初始化时第一块数据显示的位置
        pieChart.setRotationAngle(90f);
        pieChart.setRotationEnabled(true);//设置可以手动旋转
        pieChart.setUsePercentValues(true);//显示成百分比

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
        mColorIntegers.add(Color.rgb(215, 215, 215));
        mColorIntegers.add(Color.rgb(117, 18, 223));
        mColorIntegers.add(Color.rgb(255, 115, 125));
        //设置颜色集
        set.setColors(mColorIntegers);
        //选中态多出的长度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float px = 5 * (dm.densityDpi / 160f);
        set.setSelectionShift(px);
        PieData pieData = new PieData(set);

        pieChart.setData(pieData);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                StatisticsActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_time_top__parent:

                if (!timeModelList.get(position).isSelect()) {
                    for (int i = 0; i < timeModelList.size(); i++) {
                        timeModelList.get(i).setSelect(false);
                    }
                    timeModelList.get(position).setSelect(true);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

//    @Override
//    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
//    }
//
//    @Override
//    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//
//    }
//
//    @Override
//    public void onChartLongPressed(MotionEvent me) {
//        Log.i("LongPress", "Chart longpressed.");
//    }
//
//    @Override
//    public void onChartDoubleTapped(MotionEvent me) {
//        Log.i("DoubleTap", "Chart double-tapped.");
//    }
//
//    @Override
//    public void onChartSingleTapped(MotionEvent me) {
//        Log.i("SingleTap", "Chart single-tapped.");
//    }
//
//    @Override
//    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
//    }
//
//    @Override
//    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
//    }
//
//    @Override
//    public void onChartTranslate(MotionEvent me, float dX, float dY) {
//        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
//    }
//
//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
////        Log.i("Entry selected", e.toString());
////        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
////        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
//
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("Nothing selected", "Nothing selected.");
//    }
}
