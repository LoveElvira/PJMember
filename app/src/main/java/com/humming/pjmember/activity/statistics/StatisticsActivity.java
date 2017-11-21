package com.humming.pjmember.activity.statistics;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.scan.AccidentDetailsActivity;
import com.humming.pjmember.activity.scan.RepairDetailsActivity;
import com.humming.pjmember.adapter.DialogStatisticsAdapter;
import com.humming.pjmember.adapter.TimeAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.bean.TimeModel;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.MyMarkerView;
import com.humming.pjmember.viewutils.MyXFormatter;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.pjqs.dto.statistics.StatisticsBean;
import com.pjqs.dto.statistics.StatisticsCompanyBean;
import com.pjqs.dto.statistics.StatisticsCostBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/8.
 * 统计中心
 */

public class StatisticsActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, OnChartValueSelectedListener {

    //头部时间
    private TextView time;

    private TimeAdapter adapter;

    private List<TimeModel> timeModelList;

    private TextView personNum;
    private TextView projectNum;

    //项目折线图
    private LineChart projectLineChart;
    //现场人数折线图
    private LineChart peopleLineChart;
    //现场人数折线图
    private LineChart workLineChart;
    //突发事故折线图
    private LineChart accidentLineChart;
    //饼图父布局
    private LinearLayout pricePieLayout;
    //费用饼图
    private PieChart pricePieChart;
    //养护费用
    private TextView maintainPrice;
    //人员费用
    private TextView peoplePrice;
    //维修费用
    private TextView repairPrice;

    private StatisticsBean statisticsBean;
    private ArrayList<Integer> colors;

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
        listView.setVisibility(View.GONE);

        personNum = (TextView) findViewById(R.id.statistics_content__people_num);
        projectNum = (TextView) findViewById(R.id.statistics_content__project_num);

        projectLineChart = (LineChart) findViewById(R.id.statistics_content__project_line);
        peopleLineChart = (LineChart) findViewById(R.id.statistics_content__people_line);
        workLineChart = (LineChart) findViewById(R.id.statistics_content__work_line);
        accidentLineChart = (LineChart) findViewById(R.id.statistics_content__accident_line);
        pricePieChart = (PieChart) findViewById(R.id.statistics_content__price_pie);
        pricePieLayout = (LinearLayout) findViewById(R.id.statistics_content__price_pie_layout);
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
            model.setTime((i + 23) + "");
            timeModelList.add(model);
        }

        adapter = new TimeAdapter(timeModelList);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);


//        initLineChart(accidentLineChart);
        leftArrow.setOnClickListener(this);
        pricePieChart.setOnChartValueSelectedListener(this);

        getStatisticsDetails();
    }

    private void getStatisticsDetails() {
        progressHUD = ProgressHUD.show(StatisticsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setAccidentId(id);
        OkHttpClientManager.postAsyn(Config.GET_STATISTICS, new OkHttpClientManager.ResultCallback<StatisticsBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(StatisticsBean response) {
                progressHUD.dismiss();
                statisticsBean = response;
                if (response != null) {

                    projectNum.setText(statisticsBean.getSumProjectNum().toString());
                    personNum.setText(statisticsBean.getSumUserNum().toString());
                    if (statisticsBean.getProjectBeen() != null && statisticsBean.getProjectBeen().size() > 0) {
                        String[] values = new String[statisticsBean.getProjectBeen().size()];
                        int max = 0;
                        for (int i = 0; i < statisticsBean.getProjectBeen().size(); i++) {
                            values[i] = statisticsBean.getProjectBeen().get(i).getCompanyName().substring(0, 2);
                            int num = Integer.parseInt(statisticsBean.getProjectBeen().get(i).getNum());
                            if (max < num) {
                                max = num;
                            }
                        }
                        initLineChart(projectLineChart, values, max * 2f, statisticsBean.getProjectBeen());
                    }

                    if (statisticsBean.getUserBeen() != null && statisticsBean.getUserBeen().size() > 0) {
                        String[] values = new String[statisticsBean.getUserBeen().size()];
                        int max = 0;
                        for (int i = 0; i < statisticsBean.getUserBeen().size(); i++) {
                            values[i] = statisticsBean.getUserBeen().get(i).getCompanyName().substring(0, 2);
                            int num = Integer.parseInt(statisticsBean.getUserBeen().get(i).getNum());
                            if (max < num) {
                                max = num;
                            }
                        }
                        initLineChart(peopleLineChart, values, max * 2f, statisticsBean.getUserBeen());
                    }

                    if (statisticsBean.getWorkBeen() != null && statisticsBean.getWorkBeen().size() > 0) {
                        String[] values = new String[statisticsBean.getWorkBeen().size()];
                        int max = 0;
                        for (int i = 0; i < statisticsBean.getWorkBeen().size(); i++) {
                            values[i] = statisticsBean.getWorkBeen().get(i).getCompanyName().substring(0, 2);
                            int num = Integer.parseInt(statisticsBean.getWorkBeen().get(i).getNum());
                            if (max < num) {
                                max = num;
                            }
                        }
                        initLineChart(workLineChart, values, max * 2f, statisticsBean.getWorkBeen());
                    }

                    if (statisticsBean.getEmergencyBeen() != null && statisticsBean.getEmergencyBeen().size() > 0) {
                        String[] values = new String[statisticsBean.getEmergencyBeen().size()];
                        int max = 0;
                        for (int i = 0; i < statisticsBean.getEmergencyBeen().size(); i++) {
                            values[i] = statisticsBean.getEmergencyBeen().get(i).getCompanyName().substring(0, 2);
                            int num = Integer.parseInt(statisticsBean.getEmergencyBeen().get(i).getNum());
                            if (max < num) {
                                max = num;
                            }
                        }
                        initLineChart(accidentLineChart, values, max * 2f, statisticsBean.getEmergencyBeen());
                    }

                    if (statisticsBean.getCostBeen() != null && statisticsBean.getCostBeen().size() > 0) {
                        initPieChart(pricePieChart, statisticsBean.getCostBeen(), Float.parseFloat(statisticsBean.getSumCost().toString()));
                    }

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, StatisticsBean.class, RepairDetailsActivity.class);

    }


    private void initLineChart(LineChart lineChart, String[] values, float max, List<StatisticsCompanyBean> companyBeanList) {
//        lineChart.setOnChartGestureListener(this);
//        lineChart.setOnChartValueSelectedListener(this);
        //chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawGridBackground(false);
        //禁止绘制图表边框的线
        lineChart.setDrawBorders(false);
        //设置图表的描述 false 不显示
        lineChart.getDescription().setEnabled(false);
        // 没有数据时样式
        lineChart.setNoDataText("暂无数据");
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
        MyMarkerView mv = new MyMarkerView(this, R.layout.dialog_statistics, companyBeanList);
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
        xAxis.setAvoidFirstLastClipping(true);
        //设置x轴标签的旋转角度
//        xAxis.setLabelRotationAngle(10f);
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(values.length);
//        String[] value = {"大桥", "隧道", "高速", "浦东", "东海", "嘉青"};
        xAxis.setValueFormatter(new MyXFormatter(values));

        //获取左边的轴线
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置图表右边的y轴禁用
        leftAxis.setEnabled(true);
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        //设置Y轴的最大值
        leftAxis.setAxisMaximum(max);
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
        setLineData(lineChart, companyBeanList);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);
        //从左到右绘制图形，参数是int类型的 持续时间
        lineChart.animateX(1000);
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

    private void setLineData(LineChart lineChart, List<StatisticsCompanyBean> companyBeanList) {

        ArrayList<Entry> values = new ArrayList<>(companyBeanList.size());

        for (int i = 0; i < companyBeanList.size(); i++) {

            float val = /*(float) (Math.random() * range) + 3*/Float.parseFloat(companyBeanList.get(i).getNum());
            values.add(new Entry(i, val, null/*ContextCompat.getDrawable(getBaseContext(), R.mipmap.address)*/));
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
            set1.setCircleColor(getResources().getColor(R.color.pink));
            //设置线宽
            set1.setLineWidth(1f);
            //设置焦点圆心的大小
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
            //设置是否禁用范围背景填充
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            //是否显示原点上面的数字
            set1.setDrawValues(false);
            //是否显示原点
            set1.setDrawCircles(true);
            // 设置平滑曲线模式
            //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //高亮的线 确定哪个点
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                //设置范围背景填充
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    private void initPieChart(PieChart pieChart, List<StatisticsCostBean> statisticsCostBeanList, float total) {
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
        pieChart.setCenterText(generateCenterSpannableText(StringUtils.saveTwoDecimal(total + "")));
        //设置字体大小
        pieChart.setCenterTextSize(13);
        //是否要将PieChart设为一个圆环状/是否绘制饼状图中间的圆
        pieChart.setDrawHoleEnabled(true);
        //设置PieChart中间圆的颜色
        pieChart.setHoleColor(Color.WHITE);
        //饼状图中间的圆的半径大小
//        pieChart.setHoleRadius(58f);
        //设置中间圆的半透明圆环
//        pieChart.setTransparentCircleRadius(10f);
        //设置图表初始化时第一块数据显示的位置
        pieChart.setRotationAngle(90f);
        pieChart.setRotationEnabled(true);//设置可以手动旋转
        pieChart.setUsePercentValues(true);//显示成百分比

        // 设置 pieChart 图表Item文本属性
        pieChart.setDrawEntryLabels(false);//设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        pieChart.setEntryLabelColor(Color.WHITE);//设置pieChart图表文本字体颜色
        pieChart.setEntryLabelTextSize(10f);//设置pieChart图表文本字体大小

        Legend mLegend = pieChart.getLegend();
        mLegend.setForm(Legend.LegendForm.SQUARE);//Line线性 square
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setTextColor(Color.GRAY);
        mLegend.setTextSize(12f);
//        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        mLegend.setDrawInside(false);
//        mLegend.setXEntrySpace(7f);
//        mLegend.setYEntrySpace(0f);
//        mLegend.setYOffset(0f);

        initPieDate(pieChart, statisticsCostBeanList, total);
    }


    private void initPieDate(PieChart pieChart, List<StatisticsCostBean> statisticsCostBeanList, float total) {
        // 遍历饼状图
//        List<String> mXList = new ArrayList<String>();
//        for (int i = 0; i < count; i++) {
//            mXList.add("业绩所占比例:" + (i + 1));//饼块上显示成业绩比例1 显示成业绩比例2 显示成业绩比例3 显示成业绩比例4
//        }
        ArrayList<PieEntry> list = new ArrayList<>(statisticsCostBeanList.size());
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为16:16:32:36
         * 所以 16代表的百分比就是16%
         */

        for (int i = 0; i < statisticsCostBeanList.size(); i++) {
            list.add(new PieEntry((Float.parseFloat(statisticsCostBeanList.get(i).getCost()) / total) * 100, statisticsCostBeanList.get(i).getCompanyName(), i));
        }

//        float quarterly_one = 30;
//        float quarterly_two = 30;
//        float quarterly_three = 40;
//
//        list.add(new PieEntry(quarterly_one, 0));
//        list.add(new PieEntry(quarterly_two, 1));
//        list.add(new PieEntry(quarterly_three, 2));
        //y轴集合
        PieDataSet set = new PieDataSet(list, "");
        set.setSliceSpace(2f);//设置饼状之间的间隙
        set.setSelectionShift(5f);

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

//        ArrayList<Integer> mColorIntegers = new ArrayList<Integer>();
//        //饼状的颜色
//        mColorIntegers.add(Color.rgb(54, 93, 254));
//        mColorIntegers.add(Color.rgb(227, 61, 181));
//        mColorIntegers.add(Color.rgb(32, 143, 255));
        //设置颜色集
        set.setColors(colors);
        //选中态多出的长度
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        float px = 5 * (dm.densityDpi / 160f);
//        set.setSelectionShift(px);
        PieData pieData = new PieData(set);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueFormatter(new PercentFormatter());//设置显示 % 号
        pieData.setValueTextSize(10f);//设置百分比的字号

        pieChart.setData(pieData);

    }

    private SpannableString generateCenterSpannableText(String price) {

        SpannableString s = new SpannableString("总费用\n" + price);
        s.setSpan(new RelativeSizeSpan(.8f), 0, 3, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.9f), 3, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 3, s.length(), 0);
        return s;
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int position = (int) e.getData();
        new PopupWindows(StatisticsActivity.this, pricePieLayout, statisticsBean.getCostBeen().get(position), colors.get(position));
    }

    @Override
    public void onNothingSelected() {

    }


    class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent, StatisticsCostBean statisticsCostBean, int color) {

            View view = View
                    .inflate(mContext, R.layout.dialog_statistics, null);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);

            TextView name = view.findViewById(R.id.dialog_statistics__name);
            TextView price = view.findViewById(R.id.dialog_statistics__price);
            if (statisticsCostBean.getCompanyName() != null && !"".equals(statisticsCostBean.getCompanyName())) {
                name.setText(statisticsCostBean.getCompanyName());
            } else {
                name.setText("未知");
            }
            price.setText(StringUtils.saveTwoDecimal(statisticsCostBean.getCost()));

            RecyclerView listView = view.findViewById(R.id.common_listview__list);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(StatisticsActivity.this, 2);
            listView.setLayoutManager(gridLayoutManager);
            listView.addItemDecoration(new SpacesItemDecoration(10));

            if (statisticsCostBean.getCostType() != null && statisticsCostBean.getCostType().size() > 0) {
                DialogStatisticsAdapter adapter = new DialogStatisticsAdapter(statisticsCostBean.getCostType(), color);
                listView.setAdapter(adapter);
            }

        }
    }

}
