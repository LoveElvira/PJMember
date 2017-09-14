package com.humming.pjmember.activity.takephoto;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/4.
 */

public class DefectDetailsActivity extends BaseActivity {

    //设施名称
    private TextView name;
    //通知主管
    private TextView director;
    //报修时间
    private TextView time;
    //缺陷描述
    private TextView content;
    //位置描述
    private TextView address;
    //定位图片
    private ImageView addressImage;
    //提交处理结果
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("缺陷作业详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        name = (TextView) findViewById(R.id.activity_defect_details__name);
        director = (TextView) findViewById(R.id.activity_defect_details__director);
        time = (TextView) findViewById(R.id.activity_defect_details__time);
        content = (TextView) findViewById(R.id.activity_defect_details__content);
        address = (TextView) findViewById(R.id.activity_defect_details__address);
        addressImage = (ImageView) findViewById(R.id.activity_defect_details__address_image);

        submit = (TextView) findViewById(R.id.activity_defect_details__submit);

        String str = "<font color='#ADADAD'>" + "S26各收费站机点供电系统、收费系统、监控系统、通信系统保修故障维修。" + "</font>";
        content.setText(Html.fromHtml("<font color='#888888'>" + "缺陷描述：" + "</font>"
                + str));

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                DefectDetailsActivity.this.finish();
                break;
            case R.id.activity_defect_details__submit:
                startActivity(DefectResultActivity.class);
                break;
        }
    }
}
