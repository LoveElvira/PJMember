package com.humming.pjmember.base;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.pjmember.utils.FileUtils;
import com.humming.pjmember.utils.ImageFileUtils;
import com.humming.pjmember.utils.NetWorkUtils;
import com.humming.pjmember.viewutils.SelectPhotoPopupWindow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Elvira on 2017/5/31.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    //版本比较：是否是4.4及以上版本
    public static boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    /**
     * 存放拍照上传的照片路径
     */
    public static String mPublishPhotoPath;


    /*分页 每页10条数据*/
    protected static final int PAGE_SIZE = 10;
    /*延迟时间 1s*/
    protected int delayMillis = 1000;
    /*退出时间*/
    protected long exitTime;
    /*无数据VIEW*/
    protected View notLoadingView;
    /*软键盘管理者*/
    protected InputMethodManager imm;
    /*无网络情况的VIEW*/
    protected LinearLayout noWifiLayout;
    /*网络类型*/
    protected int netMobile;
    //头部 左边的箭头
    protected ImageView leftArrow;
    protected LinearLayout titleLayout;
    //头部 中间的文字
    protected TextView title;
    //头部 中间的图片
    protected ImageView titleImage;
    //头部 左边的文字
    protected TextView rightText;
    //头部 右边的图标
    protected ImageView rightImage;

    //刷新
    protected SwipeRefreshLayout refresh;
    //列表
    protected RecyclerView listView;

    protected SelectPhotoPopupWindow selectPhotoPopupWindow;
    protected LinearLayout selectPhotoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//当前手机版本5.0以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //当前手机版本为4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight());
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
            contentView.addView(statusBarView, lp);
        }

        initSelectPhoto();
        Application.getInstance().setCurrentActivity(this);
//        initView();
    }

    private void initSelectPhoto() {
        if (selectPhotoPopupWindow == null) {
            selectPhotoPopupWindow = new SelectPhotoPopupWindow();
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    protected void initView() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Application.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 短暂显示Toast提示(来自String)
     */
    protected void showShortToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity, String name, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(name, bundle);
        startActivity(intent);
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity, String name, String value) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    /**
     * 初始化时判断有没有网络
     */
    protected boolean inspectNet() {
        this.netMobile = NetWorkUtils.getNetWorkState(this);
        return isNetConnect();
    }

    protected boolean isNetConnect() {
        if (netMobile == 1) {//连接wifi
            return true;
        } else if (netMobile == 0) {//连接移动数据
            return true;
        } else if (netMobile == -1) {//当前没有网络
            return false;

        }
        return false;
    }

    protected String initHtml(String header, String footer) {
        String str = "<font color='#888888'>" + header + "：" + "</font>"
                + "<font color='#ADADAD'>" + footer + "</font>";
        return str;
    }

    //判断手机号是否正确
    protected boolean isMobileNum(String mobile) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void getCamerePhoto() {

        // 根目录
        String rootDir = FileUtils.getRootDir(Application.getInstance().getCurrentActivity());
        // 创建应用缓存目录
        String appCacheDir = rootDir + File.separator + FileUtils.LBTWYYJS;
        File dir = new File(appCacheDir);
        if (!dir.exists())
            dir.mkdirs();
        // 创建应用缓存文件
        mPublishPhotoPath = appCacheDir + File.separator + UUID.randomUUID() + ".jpg";
        File file = new File(mPublishPhotoPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // 调用相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        try {
            if (mIsKitKat)
                startActivityForResult(intent, Constant.CODE_REQUEST_ONE);
            else
                startActivityForResult(intent, Constant.CODE_REQUEST_TWO);

        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(this).setMessage("没有合适的相机应用程序").setPositiveButton("OK", null).show();
        }
    }


    //获取图片路径 4.4以下
    protected String getPhotoName(Uri uri, boolean flag, String path) {

        int degree = 0;
        if (flag) {
            ContentResolver cr = Application.getInstance().getCurrentActivity().getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            degree = readPictureDegree(cursor.getString(1));
            cursor.close();
        } else
            degree = readPictureDegree(path);

        Bitmap bitmap = ImageFileUtils.getBitmapUseUri(uri, Application.getInstance().getCurrentActivity().getContentResolver());
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // 根目录
        String rootDir = ImageFileUtils.getRootDir(this);

        // 创建应用缓存目录(一级)
        String appCacheDir = rootDir + File.separator + ImageFileUtils.LBTWYYJS;
        File dir = new File(appCacheDir);
        if (!dir.exists())
            dir.mkdirs();

        // 创建应用缓存目录(二级)
        String appCacheDir2 = appCacheDir + File.separator + ImageFileUtils.LBTWYYJS_SMALL;
        File dir2 = new File(appCacheDir2);
        if (!dir2.exists())
            dir2.mkdirs();

        // 创建应用缓存文件
        String filePath = appCacheDir2 + File.separator + UUID.randomUUID() + ".jpg";
        File fileFile = new File(filePath);
        if (!fileFile.exists())
            try {
                fileFile.createNewFile();
                // 写入文件
                FileOutputStream fos = new FileOutputStream(fileFile);
                if (null != fos)
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        if (resizedBitmap != null) {
            resizedBitmap.recycle();
            bitmap.recycle();
        }
        return filePath;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    protected int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
