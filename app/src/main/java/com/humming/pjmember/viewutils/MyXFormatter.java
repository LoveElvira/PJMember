package com.humming.pjmember.viewutils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Elvira on 2017/10/19.
 */

public class MyXFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXFormatter(String[] values) {
        this.mValues = values;
    }

    private static final String TAG = "MyXFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
//        Log.d(TAG, "----->getFormattedValue: " + value);
        return mValues[(int) value % mValues.length];
    }
}
