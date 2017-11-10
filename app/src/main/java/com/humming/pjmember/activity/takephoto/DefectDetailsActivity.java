package com.humming.pjmember.activity.takephoto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.activity.work.BaseWorkActivity;
import com.humming.pjmember.adapter.ImageLookAdapter;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.pjqs.dto.work.WorkBean;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/9/4.
 * 缺陷详情
 */

public class DefectDetailsActivity extends BaseWorkActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //作业名称
    private TextView workName;
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

    private ImageLookAdapter adapter;

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

        listView = (RecyclerView) findViewById(R.id.activity_defect_details__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);
        listView.addItemDecoration(new SpacesItemDecoration(10));

        workName = (TextView) findViewById(R.id.activity_defect_details__work_name);
        name = (TextView) findViewById(R.id.activity_defect_details__name);
        director = (TextView) findViewById(R.id.activity_defect_details__director);
        time = (TextView) findViewById(R.id.activity_defect_details__time);
        content = (TextView) findViewById(R.id.activity_defect_details__content);
        address = (TextView) findViewById(R.id.activity_defect_details__address);
        addressImage = (ImageView) findViewById(R.id.activity_defect_details__address_image);

        submit = (TextView) findViewById(R.id.activity_defect_details__submit);

//        String str = "S26各收费站机点供电系统、收费系统、监控系统、通信系统保修故障维修。";
//        content.setText(initHtml("缺陷描述", str));

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
        getWorkDetails();
    }

    private void getWorkDetails() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.CODE_SUCCESS:
                        workBean = (WorkBean) msg.obj;
                        initData();
                        break;
                }
            }
        };
        progressHUD = ProgressHUD.show(DefectDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getWorkDetails(handler);
    }


    private void initData() {
        if (workBean != null) {
            workName.setText(workBean.getWorkName());
            name.setText(workBean.getFacilityName());
            director.setText(workBean.getFacilityUserName());
            time.setText(workBean.getCrtTime());
            address.setText(workBean.getStartLocation());
            content.setText(initHtml("缺陷描述", workBean.getRemark()));
            if (workBean.getPictureUrls() != null && workBean.getPictureUrls().size() > 0) {
                adapter = new ImageLookAdapter(workBean.getPictureUrls(), getBaseContext());
                listView.setAdapter(adapter);
                adapter.setOnItemChildClickListener(DefectDetailsActivity.this);
            }
        }

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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_image__image_bg:
            case R.id.item_image__image:
                Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imageUrl", (Serializable) workBean.getPictureUrls());
                startActivity(intent);
                break;
        }
    }
}
