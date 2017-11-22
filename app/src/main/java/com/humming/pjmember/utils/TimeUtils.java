package com.humming.pjmember.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Elvira on 2017/11/22.
 */

public class TimeUtils {

    public static boolean getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int k = Integer.parseInt(hour);
        if ((k >= 0 && k < 6) || (k >= 18 && k < 24)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 1、先使用Calendar获取当前的年份、当前的月份、当前的日期。
     * 2、在用Calendar的set函数设定当前时间。
     * 3、通过Calendar的add函数来寻找前或者后几天的日期。
     * 4、最后用Calendar的get函数获取当时的具体日期。
     */
    public static List<Map<String, String>> getDateAfter() {
        List<Map<String, String>> dateList = new ArrayList<>();
        String mYear; // 当前年
        String mMonth; // 月
        String mDay;
        int current_day;
        int current_month;
        int current_year;

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        current_day = c.get(Calendar.DAY_OF_MONTH);
        current_month = c.get(Calendar.MONTH);
        current_year = c.get(Calendar.YEAR);
        for (int i = 0; i < 7; i++) {
            Map<String, String> map = new HashMap<>();
            c.clear();//记住一定要clear一次
            c.set(Calendar.MONTH, current_month);
            c.set(Calendar.DAY_OF_MONTH, current_day);
            c.set(Calendar.YEAR, current_year);
            c.add(Calendar.DATE, +i);//j记住是DATE
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前日份的日期号码
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            String date = mYear + "年" + mMonth + "月" + mDay + "日";
            map.put("year", mYear);
            map.put("month", mMonth);
            map.put("day", mDay);
            map.put("isSelect", "false");
            dateList.add(map);
        }
        return dateList;
    }

    public static String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int current_day = c.get(Calendar.DAY_OF_MONTH);
        int current_month = c.get(Calendar.MONTH) + 1;
        int current_year = c.get(Calendar.YEAR);

        String mYear = String.valueOf(current_year); // 当前年
        String mMonth = String.valueOf(current_month); // 月
        String mDay = String.valueOf(current_day);

        return mYear + "-" + mMonth + "-" + mDay;
    }
}
