package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentInsuranceBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/12/6.
 * 获取保险记录详情
 */

public class InsuranceDetailsActivity extends BaseActivity {

    //保险名称title
    private TextView nameTitle;
    //保险名称
    private TextView name;
    //保险单号title
    private TextView numTitle;
    //保险单号
    private TextView num;
    //被保险人title
    private LinearLayout assuredManLayout;
    private TextView assuredManTitle;
    //被保险人
    private TextView assuredMan;
    //投保人title
    private LinearLayout applicantManLayout;
    private TextView applicantManTitle;
    //投保人
    private TextView applicantMan;
    //保险种类
    private TextView type;
    //有效时间title
    private TextView timeTitle;
    //有效时间
    private TextView time;
    //保险费用title
    private TextView priceTitle;
    //保险费用
    private TextView price;

    //
    private LinearLayout imageLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");

        title = findViewById(R.id.base_toolbar__title);
        title.setText("保险记录详情");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        nameTitle = findViewById(R.id.activity_log_details__facility_name_title);
        name = findViewById(R.id.activity_log_details__facility_name);
        numTitle = findViewById(R.id.activity_log_details__num_title);
        num = findViewById(R.id.activity_log_details__num);
        timeTitle = findViewById(R.id.activity_log_details__time_title);
        time = findViewById(R.id.activity_log_details__time);
        priceTitle = findViewById(R.id.activity_log_details__price_title);
        price = findViewById(R.id.activity_log_details__price);

        assuredManLayout = findViewById(R.id.activity_log_details__company_layout);
        assuredManTitle = findViewById(R.id.activity_log_details__company_title);
        assuredMan = findViewById(R.id.activity_log_details__company);
        applicantManLayout = findViewById(R.id.activity_log_details__type_layout);
        applicantManTitle = findViewById(R.id.activity_log_details__type_title);
        applicantMan = findViewById(R.id.activity_log_details__type);
        type = findViewById(R.id.activity_log_details__content);

        imageLayout = findViewById(R.id.activity_log_details__listview_layout);

        nameTitle.setText("保险名称：");
        numTitle.setText("保险单号：");
        timeTitle.setText("有效时间：");
        priceTitle.setText("保险费用：");
        assuredManTitle.setText("被保险人：");
        applicantManTitle.setText("投保人：");

        assuredManLayout.setVisibility(View.VISIBLE);
        applicantManLayout.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);

        leftArrow.setOnClickListener(this);
        getInsuranceDetails();
    }

    //获取设备保险信息详情
    private void getInsuranceDetails() {
        progressHUD = ProgressHUD.show(InsuranceDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setInsuranceId(id);
        OkHttpClientManager.postAsyn(Config.GET_INSURANCE_DETAILS, new OkHttpClientManager.ResultCallback<EquipmentInsuranceBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentInsuranceBean response) {
                progressHUD.dismiss();
                if (response != null) {
                    name.setText(response.getInsuranceName());
                    num.setText(response.getOrderNo());
                    assuredMan.setText(response.getAssuredMan());
                    applicantMan.setText(response.getApplicantMan());
                    type.setText(initHtml("保险类型", response.getType()));
                    price.setText(StringUtils.saveTwoDecimal(response.getMoney().toString()));
                    time.setText(response.getStartTime() + "~" + response.getEndTime());
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EquipmentInsuranceBean.class, InsuranceDetailsActivity.class);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                InsuranceDetailsActivity.this.finish();
                break;
        }
    }
}
