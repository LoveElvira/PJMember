package com.humming.pjmember.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Constant;

import java.util.List;

/**
 * Created by Elvira on 2017/9/6.
 * 百度地图 定位
 */

public class MapActivity extends BaseActivity implements SensorEventListener, BaiduMap.OnMapStatusChangeListener, BDLocationListener, OnGetGeoCoderResultListener {

    //定位 类型
    private LinearLayout addressImageLayout;
    //切换定位类型图片
    private ImageView addressImage;

    //中心图标
    private ImageView addressCenter;
    //地图
    private MapView mapView;
    private BaiduMap baiduMap;
    //获取的地址显示的区域
    private LinearLayout addressLayout;
    //地址名字
    private TextView addressName;
    //具体地址
    private TextView address;
    //相聚距离
    private TextView addressDistance;
    //地址与距离之间的 竖线
    private View line;

    // 定位类型 普通 跟随 罗盘
    private LocationMode mode;
    //自定义定位图标
    private BitmapDescriptor marker = null;
    //定位端
    private LocationClient locClient;
    //传感器管理者
    private SensorManager mSensorManager;

    // 是否首次定位
    boolean isFirstLoc = true;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;

    //定位城市
    private String city;
    //反地理编码
    private GeoCoder geoCoder;
    //定位坐标
    private LatLng locationLatLng;

    private String addressStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("定位");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("完成");

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// 获取传感器管理服务


        addressImageLayout = (LinearLayout) findViewById(R.id.activity_map__address_image_layout);
        addressImage = (ImageView) findViewById(R.id.activity_map__address_image);
        addressCenter = (ImageView) findViewById(R.id.activity_map__address_center_image);
        mapView = (MapView) findViewById(R.id.activity_map__map);

        addressLayout = (LinearLayout) findViewById(R.id.activity_map__address_layout);
        addressName = (TextView) findViewById(R.id.activity_map__address_name);
        address = (TextView) findViewById(R.id.activity_map__address);
        addressDistance = (TextView) findViewById(R.id.activity_map__address_distance);
        line = findViewById(R.id.activity_map__address_line);


        baiduMap = mapView.getMap();

        leftArrow.setOnClickListener(this);
        rightText.setOnClickListener(this);
        addressImageLayout.setOnClickListener(this);
        startLocation();
    }

    View.OnClickListener addressClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (mode) {
                case NORMAL:
//                    requestLocButton.setText("跟随");
                    mode = LocationMode.COMPASS;
                    baiduMap.setMyLocationConfiguration(
                            new MyLocationConfiguration(mode, true, marker));
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.overlook(0);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    break;
                case COMPASS:
//                    requestLocButton.setText("普通");
                    mode = LocationMode.NORMAL;
                    baiduMap.setMyLocationConfiguration(
                            new MyLocationConfiguration(mode, true, marker));
                    MapStatus.Builder builder1 = new MapStatus.Builder();
                    builder1.overlook(0);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                    break;
                case FOLLOWING:
//                    requestLocButton.setText("罗盘");
                    mode = LocationMode.COMPASS;
                    baiduMap.setMyLocationConfiguration(
                            new MyLocationConfiguration(mode, true, marker));
                    break;
                default:
                    break;
            }
        }
    };

    private void startLocation() {
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);

        mode = LocationMode.NORMAL;
        //自定义定位图标
//      marker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        baiduMap.setMyLocationConfiguration(
                new MyLocationConfiguration(mode, true, marker));

        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("ee", "latitude:" + latLng.latitude + "--------longitude:" + latLng.longitude);
                //点击地图某个位置获取经纬度latLng.latitude、latLng.longitude
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Log.i("ee", "---------" + mapPoi.getName());
                //点击地图上的poi图标获取描述信息：mapPoi.getName()，经纬度：mapPoi.getPosition()
                return false;
            }
        });
        baiduMap.setOnMapStatusChangeListener(this);

        // 定位初始化
        locClient = new LocationClient(this);
        locClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        //设置是否打开gps进行定位
        option.setOpenGps(true);
        /**
         * coorType - 取值有3个：
         * 返回国测局经纬度坐标系：gcj02
         * 返回百度墨卡托坐标系 ：bd09
         * 返回百度经纬度坐标系 ：bd09ll
         */
        option.setCoorType("bd09ll");
        //设置扫描间隔，单位是毫秒 当<3000(1s)时，定时定位无效
        option.setScanSpan(3000);
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"， 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        //设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式
         * Battery_Saving
         * 低功耗模式
         * Device_Sensors
         * 仅设备(Gps)模式
         * Hight_Accuracy
         * 高精度模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locClient.setLocOption(option);
        locClient.start();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //每次方向改变，重新给地图设置定位数据，用上一次onReceiveLocation得到的经纬度、精度
//        double x = sensorEvent.values[SensorManager.DATA_X];
//        if (Math.abs(x - lastX) > 1.0) {// 方向改变大于1度才设置，以免地图上的箭头转动过于频繁
//            mCurrentDirection = (int) x;
//            locData = new MyLocationData.Builder().accuracy(mCurrentAccracy)
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(mCurrentDirection).latitude(mCurrentLat).longitude(mCurrentLon).build();
//            baiduMap.setMyLocationData(locData);
//
//        }
//        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 手势操作地图，设置地图状态等操作导致地图状态开始改变
     *
     * @param mapStatus 地图状态改变开始时的地图状态
     */
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    /**
     * 地图状态变化中
     *
     * @param mapStatus 当前地图状态
     */
    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    /**
     * 地图状态改变结束
     *
     * @param mapStatus 地图状态改变结束后的地图状态
     */
    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        //地图操作的中心点
        LatLng cenpt = mapStatus.target;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || baiduMap == null) {
            return;
        }
        mCurrentLat = location.getLatitude();
        mCurrentLon = location.getLongitude();
        mCurrentAccracy = location.getRadius();
        // 构造定位数据
        locData = new MyLocationData.Builder()
                //定位精度bdLocation.getRadius()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection())
                //经度
                .latitude(location.getLatitude())
                //纬度
                .longitude(location.getLongitude())
                //构建
                .build();
        // 设置定位数据
        baiduMap.setMyLocationData(locData);
        /**
         *当首次定位时，记得要放大地图，便于观察具体的位置
         * LatLng是缩放的中心点，这里注意一定要和上面设置给地图的经纬度一致；
         * MapStatus.Builder 地图状态构造器
         */
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatus.Builder builder = new MapStatus.Builder();
//            //设置缩放中心点；缩放比例；
//            builder.target(ll).zoom(18.0f);
//            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
            baiduMap.animateMapStatus(msu);
            addressStr = location.getAddrStr();
            addressLayout.setVisibility(View.VISIBLE);
            address.setText(location.getAddrStr());
            addressName.setText(location.getCity());
            line.setVisibility(View.GONE);
            addressDistance.setText("");
        }
        //获取坐标，待会用于POI信息点与定位的距离
        locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //获取城市，待会用于POISearch
        city = location.getCity();
        //创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        //发起反地理编码请求(经纬度->地址信息)
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        //设置反地理编码位置坐标
        reverseGeoCodeOption.location(new LatLng(location.getLatitude(), location.getLongitude()));
        geoCoder.reverseGeoCode(reverseGeoCodeOption);

        //设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(this);

        Log.i("ee", "---" + location.getAddrStr());
    }

    //地理编码查询结果回调函数
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    //反地理编码查询结果回调函数
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        if (poiInfos != null && poiInfos.size() > 0) {
            addressStr = poiInfos.get(0).address;
            addressLayout.setVisibility(View.VISIBLE);
            address.setText(poiInfos.get(0).address);
            addressName.setText(poiInfos.get(0).name);
            line.setVisibility(View.VISIBLE);
            addressDistance.setText((int) DistanceUtil.getDistance(locationLatLng, poiInfos.get(0).location) + "m");
        }
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        // 取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                finishThis(false);
                break;
            case R.id.base_toolbar__right_text:
                finishThis(true);
                break;
            case R.id.activity_map__address_image_layout:
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(locationLatLng, 18);
                baiduMap.animateMapStatus(msu);
                break;
        }
    }

    private void finishThis(boolean isReturn) {
        locClient.stop();
//                setResult(Constant.CODE_RESULT, new Intent().putExtra("imagePath", ""));
        // 当不需要定位图层时关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        //释放资源
        if (geoCoder != null) {
            geoCoder.destroy();
        }

        if (isReturn) {
            setResult(Constant.CODE_RESULT, new Intent()
                    .putExtra("address", addressStr));
        }

        MapActivity.this.finish();
    }

}
