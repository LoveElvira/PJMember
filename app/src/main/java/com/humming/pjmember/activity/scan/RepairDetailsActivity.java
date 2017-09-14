package com.humming.pjmember.activity.scan;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/4.
 * 维修记录详情
 */

public class RepairDetailsActivity extends BaseActivity {

    //报修时间
    private TextView time;
    //设备名称
    private TextView name;
    //设备编号
    private TextView num;
    //维修内容
    private TextView content;
    //维修金额
    private TextView price;
    //发票图片
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);
        initView();
    }


    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("维修记录详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        time = (TextView) findViewById(R.id.activity_repair_details__time);
        name = (TextView) findViewById(R.id.activity_repair_details__facility_name);
        num = (TextView) findViewById(R.id.activity_repair_details__num);
        content = (TextView) findViewById(R.id.activity_repair_details__content);
        price = (TextView) findViewById(R.id.activity_repair_details__price);
        image = (ImageView) findViewById(R.id.activity_repair_details__image);

        String str = "<font color='#ADADAD'>" + "S26各收费站机点供电系统、收费系统、监控系统、通信系统保修故障维修。" + "</font>";
        content.setText(Html.fromHtml("<font color='#888888'>" + "缺陷描述：" + "</font>"
                + str));


        leftArrow.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                RepairDetailsActivity.this.finish();
                break;
            case R.id.activity_repair_details__image://查看发票大图
                break;
        }
    }
}
