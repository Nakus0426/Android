package com.zhz_zsl.glutweather;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zhz_zsl.glutweather.Toast.ToastUtils;
import com.zhz_zsl.glutweather.model.Weather_model;
import com.zhz_zsl.glutweather.utils.HttpDownloader;
import com.zhz_zsl.glutweather.utils.NetUtil;
import com.zhz_zsl.glutweather.utils.ParseNowWeatherUtil;
import com.zhz_zsl.glutweather.utils.ParsePm;
import com.zhz_zsl.glutweather.utils.ParseWeatherUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements AMapLocationListener, SwipeRefreshLayout.OnRefreshListener {

    //实时天气情况信息
    //AMap是地图对象
    private static final int REFRESH_COMPLETE = 0X110;
    private SwipeRefreshLayout mSwipeLayout;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器

    private List<Weather_model> WeatherList = new ArrayList<>();
    private int code;
    private ImageView tianqi;
    public String city;
    private String city1 = "guilin";

    //天气信息控件
    //今天
    private TextView nowDateTime;           //时间
    private TextView nowWeatherInfo;        //天气信息
    private TextView nowTempRange;          //气温
    private ImageView onexiao;
    private ImageView twoxiao;
    private ImageView threexiao;
    private ImageView fourxiao;
    //明天
    private TextView twoDateTime;
    private TextView twoWeatherInfo;
    private TextView twoTempRange;

    //后天
    private TextView threeDateTime;
    private TextView threeWeatherInfo;
    private TextView threeTempRange;

    private TextView fourDateTime;
    private TextView fourWeatherInfo;
    private TextView fourTempRange;


    private Weather_model nowWeather;
    private Weather_model pm;
    private TextView pm1;

    //UI控件
    private Toolbar toolbar;              //工具栏
    private TextView title_temp;          //toolbar上显示的温度
    private TextView main_location;       //所在城市
    private TextView main_temp;           //所在城市的实时温度
    private TextView main_info;           //所在城市的实时天气描述
    private ImageButton location;
    private StringBuffer buffer;
    private String imagePath;
    private int[] path = new int[]{R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four, R.mipmap.ll};
    //定位成功后更新UI控件中的背景图片
    private Handler mmHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    location();
                    readBCperfernces();
                    saveBCperfernces(new Random().nextInt(path.length));

                    mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };
    private TextView push;
    private ImageButton fenx;

    /**
     * 从SharedPreferences中读取数据
     */
    private void readBCperfernces() {

        SharedPreferences sharedPreferences = getSharedPreferences("BC",
                MODE_PRIVATE);
        //读取数据
        int BC = sharedPreferences.getInt("BCG", 0);
        relativeLayout.setBackgroundResource(path[BC]);
    }

    /**
     * 用SharedPreferences保存数据
     */
    private void saveBCperfernces(int shu) {
        SharedPreferences mySharedPreferences = getSharedPreferences("BC", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putInt("BCG", shu);
        editor.commit();
    }

    private ProgressDialog dialog;
    private RelativeLayout relativeLayout;

    /**
     * 应用刷新时向更新背景图片方法发送指令
     */
    public void onRefresh() {

        mmHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }


    private Handler mHandler = new Handler() {


        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            //实时天气信息
            if (msg.what == 0x12) {
                try {
                    String jsonString = (String) msg.obj;
                    Log.i("传递过来的json",jsonString);
                    ParseNowWeatherUtil parseNowWeatherUtil = new ParseNowWeatherUtil();
                    nowWeather = parseNowWeatherUtil.getInfomation(jsonString);
                    Log.i("天气", nowWeather.getName());
                    //先需要下载数据
                    if (saveperfernces(nowWeather.getName(), nowWeather.getTemperature().toString() + "°", nowWeather.getWeatherText().toString(), nowWeather.getTemperature().toString() + "°", "体感温度" + nowWeather.getFeels_like().toString() + "°", "能见度" + nowWeather.getVisibility() + "千米", " " + nowWeather.getWind_direction() + " " + nowWeather.getWind_scale(), nowWeather.getWeatherCode(), " 气压" + nowWeather.getPressure().toString() + "百帕", nowWeather.getPush().toString() + "发布")) {
                        Log.i("saveperfernces保存成功",nowWeather.getTemperature());
                    }
                    readperfernces();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 0x11) {
                try {
                    String jsonString = (String) msg.obj;
                    ParseWeatherUtil parseWeatherUtil = new ParseWeatherUtil();
                    //清除天气数据
                    clearWeatherList();
                    WeatherList = parseWeatherUtil.getInfomation(jsonString);
                    //设置数据
                    setDatas();
                    readfore();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 0x13) {
                try {
                    String jsonString = (String) msg.obj;
                    ParsePm parsePm = new ParsePm();
                    pm = parsePm.getpmInfomation(jsonString);
                    Log.i("空气质量", pm.getAir());
                    //先需要下载数据
                    savePmperfernces(pm.getPm(), pm.getAir());
                    readPmperfernces();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 空气质量信息线程
     * API使用参考：https://docs.seniverse.com/api/air/now.html
     */
    private class WeatherpmThread implements Runnable {

        private String city;

        public WeatherpmThread(String city) {
            this.city = city;
        }

        public void run() {
            try {
                //将城市字段转码为UTF-8
                city1 = URLEncoder.encode(city, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //通过“私钥”直接请求方式获取天气信息
            String address = "https://free-api.heweather.com/v5/aqi?city=" + city1 + "&key=%20f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            Log.w("WeatherpmThread接收的json:", jsonString);

            //通过obtain（）方法获取Message对象
            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x13;
            //通过handler传递Message
            mHandler.sendMessage(message);
        }
    }

    /**
     * 实时天气线程
     * API使用参考：https://docs.seniverse.com/api/weather/now.html
     */
    private class WeatherThread implements Runnable {

        private String city;

        public WeatherThread(String city) {
            this.city = city;

        }

        public void run() {
            try {
                city1 = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }

            String address = "https://free-api.heweather.com/v5/now?city=" + city1 + "&key=f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            Log.w("WeatherThread中接收到的json:", jsonString);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x12;
            mHandler.sendMessage(message);
        }
    }


    /**
     * 天气预报线程
     * API使用参考：https://docs.seniverse.com/api/weather/daily.html
     */
    private class WeatherInfoThread implements Runnable {

        private String city;

        public WeatherInfoThread(String city) {
            this.city = city;
        }

        public void run() {
            try {
                city1 = URLEncoder.encode(city, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            String address = "https://api.seniverse.com/v3/weather/daily.json?key=SXYZSglxbHO5vX5Ml&location=" + city1 + "&language=zh-Hans&unit=c&start=0&days=5";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x11;
            mHandler.sendMessage(message);
        }
    }


    /**
     * 读取实时天气信息
     */
    private void readperfernces() {
        //  读取用于设置
        SharedPreferences sharedPreferences = getSharedPreferences("city",
                MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值  无返回none
        String string1 = sharedPreferences.getString("CITY", "0");          //城市
        String string2 = sharedPreferences.getString("MAIN_TEMP", "0");     //温度
        String string3 = sharedPreferences.getString("MAIN_INFO", "0");     //天气
        String string4 = sharedPreferences.getString("TITLE_TEMP", "0");    //标题栏温度
        int string8 = sharedPreferences.getInt("CODE", 0);                  //天气编号
        String string10 = sharedPreferences.getString("PUSH", "0");         //发布日期

        main_location.setText(string1);
        Log.i("TITLE_TEMP",string1);
        title_temp.setText(string4);  //标题栏温度
        main_temp.setText(string2);  //大温度
        main_info.setText(string3);  //优
        code = string8;
        push.setText(string10);

        //修改实时天气图标
        tianqi(code);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //检查网络状态是否良好
        if (NetUtil.getNetWorkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("YWeather", "网络正常连接");
        } else {
            Log.d("YWeather", "网络挂了");
            ToastUtils.showToast(MainActivity.this, "请检查网络设置");
        }
        bindViews();
        //  有数据  不会NULL
        //  运行定位
        location();
        //随机更新背景图片
        readBCperfernces();
        //更新空气质量
        readPmperfernces();

        //下拉刷新
        mSwipeLayout.setOnRefreshListener(this);
        //设置下拉刷新效果的样式
        mSwipeLayout.setColorScheme(R.color.colorAccent, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //开始定位
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
                ToastUtils.showToast(getApplicationContext(), "已定位到当前城市：" + buffer.toString());
            }
        });



        //为地图按钮设置点击事件
        fenx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Map.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 定位
     */
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(6000*10);
        //给对定位客户端象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //activity 再次回到前台
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止定位
        mLocationClient.stopLocation();
        //销毁定位客户端
        mLocationClient.onDestroy();
    }

    /**
     * 定位状态反馈
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getCity();//城市信息
                buffer = new StringBuffer();
                buffer.append(aMapLocation.getCity() + "");
                city = buffer.toString();
                Log.e("定位", "xx" + city);

                new Thread(new WeatherThread(city)).start(); // 实况
                new Thread(new WeatherInfoThread(city)).start(); //  预报
                new Thread(new WeatherpmThread(city)).start();
                mLocationClient.stopLocation();//停止定位


            } else {
                //显示错误信息ErrCode是错误码，详见错误码表。errInfo是错误信息，
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                ToastUtils.showToast(getApplicationContext(), "获取定位失败");
            }
        }
    }

    private void processThread() {
        //构建一个下载进度条
        dialog = ProgressDialog.show(MainActivity.this, "", "Loading…");
        new Thread() {
            public void run() {
                //在新线程里执行长耗时方法
                longTimeMethod();
                //执行完毕后给handler发送一个空消息
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            dialog.dismiss();
        }
    };

    private void longTimeMethod() {

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 保存空气质量信息
     */
    private void savePmperfernces(int pm, String air) {

        SharedPreferences mySharedPreferences = getSharedPreferences("pm", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putInt("PM", pm);
        editor.putString("AIR", air);
        //提交当前数据
        editor.commit();
        Log.e("保存成功", air);
    }

    /**
     * 更新空气质量
     */
    private void readPmperfernces() {
        SharedPreferences sharedPreferences = getSharedPreferences("pm",
                MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值  无返回none
        String air = sharedPreferences.getString("AIR", "");
        Log.i("读取到的空气质量:",air);
        pm1.setText("空气" + air);
    }

    /**
     * 保存实时天气信息
     */
    private boolean saveperfernces(String cityname, String main_temp, String main_info, String title_temp, String feels_like, String visibility, String winds, int code, String pressure, String push) {

        SharedPreferences mySharedPreferences = getSharedPreferences("city",
                MODE_PRIVATE);

        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("CITY", cityname);
        editor.putString("MAIN_TEMP", main_temp);
        editor.putString("MAIN_INFO", main_info);
        editor.putString("TITLE_TEMP", title_temp);
        editor.putString("VISIBILITY", visibility);
        editor.putInt("CODE", code);
        editor.putString("PUSH", push);
        //提交当前数据
        editor.commit();
        Log.e("保存成功", cityname);
        return editor.commit();
    }

    /**
     *创建可供选择的菜单选项
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.city_search:
                LayoutInflater factory = LayoutInflater.from(this);
                final View view_search = factory.inflate(R.layout.view_search, null);
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.search)
                        .setView(view_search)
                        .setTitle("搜索城市")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //确认操作
                                EditText searchTextView = (EditText) view_search.findViewById(R.id.searchTextView);
                                //  获取输入文字
                                city = searchTextView.getText().toString();
                                processThread();

                                new Thread(new WeatherThread(city)).start();
                                new Thread(new WeatherInfoThread(city)).start();
                                new Thread(new WeatherpmThread(city)).start();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消操作
                                dialogInterface.dismiss();
                            }
                        }).create()
                        .show();
                break;
            case R.id.about_app:
                Intent intent2 = new Intent(MainActivity.this, About.class);
                startActivity(intent2);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新天气预报
     */
    private void setDatas() {
        Weather_model newWeather;
        SharedPreferences mySharedPreferences = getSharedPreferences("fore",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 第一天
        newWeather = WeatherList.get(0);
        nowDateTime.setText("今天");
        //获取当前天气编号（下同）
        int code1 = newWeather.getWeatherCode();
        //更新天气图标（下同）
        onexiao(code1);
        //设置天气信息（下同）
        nowWeatherInfo.setText(newWeather.getText_day());
        //设置气温（下同）
        nowTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        //保存天气信息（下同）
        editor.putString("oneday", nowDateTime.getText().toString());
        editor.putString("oneyu", nowWeatherInfo.getText().toString());
        editor.putInt("onecode", code1);
        editor.putString("onetem", nowTempRange.getText().toString());
        //  第二天
        newWeather = WeatherList.get(1);
        twoDateTime.setText("明天");
        int code2 = newWeather.getWeatherCode();
        twoxiao(code2);
        twoWeatherInfo.setText(newWeather.getText_day());
        twoTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("twoday", twoDateTime.getText().toString());
        editor.putString("twoyu", twoWeatherInfo.getText().toString());
        editor.putInt("twocode", code2);
        editor.putString("twotem", twoTempRange.getText().toString());
        // 第三天
        newWeather = WeatherList.get(2);
        threeDateTime.setText("后天");
        int code3 = newWeather.getWeatherCode();
        threexiao(code3);
        threeWeatherInfo.setText(newWeather.getText_day());
        threeTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("threeday", threeDateTime.getText().toString());
        editor.putString("threeyu", threeWeatherInfo.getText().toString());
        editor.putInt("threecode", code3);
        editor.putString("threetem", threeTempRange.getText().toString());

        //第四天
        newWeather = WeatherList.get(2);
        fourDateTime.setText("外天");
        int code4 = newWeather.getWeatherCode();
        fourxiao(code4);
        fourWeatherInfo.setText(newWeather.getText_day());
        fourTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("fourday", fourDateTime.getText().toString());
        editor.putString("fouryu", fourWeatherInfo.getText().toString());
        editor.putInt("fourcode", code4);
        editor.putString("fourtem", fourTempRange.getText().toString());

        editor.commit();
    }

    /**
     * 更新天气预报
     */
    public void readfore() {
        SharedPreferences sharedPreferences = getSharedPreferences("fore",
                MODE_PRIVATE);
        // 今天
        String string00 = sharedPreferences.getString("oneday", "");
        String string11 = sharedPreferences.getString("oneyu", "");
        String string22 = sharedPreferences.getString("onetem", "");
        int string88 = sharedPreferences.getInt("onecode", 0);
        nowDateTime.setText(string00);
        onexiao(string88);
        nowTempRange.setText(string22);
        nowWeatherInfo.setText(string11);
        // 明天
        String string000 = sharedPreferences.getString("twoday", "");
        String string111 = sharedPreferences.getString("twoyu", "");
        String string222 = sharedPreferences.getString("twotem", "");
        int string888 = sharedPreferences.getInt("twocode", 0);
        twoDateTime.setText(string000);
        twoxiao(string888);
        twoTempRange.setText(string222);
        twoWeatherInfo.setText(string111);
        // 后天
        String string0000 = sharedPreferences.getString("threeday", "");
        String string1111 = sharedPreferences.getString("threeyu", "");
        String string2222 = sharedPreferences.getString("threetem", "");
        int string8888 = sharedPreferences.getInt("threecode", 0);
        threeDateTime.setText(string0000);
        threexiao(string8888);
        threeTempRange.setText(string2222);
        threeWeatherInfo.setText(string1111);
        // 外天
        String string00000 = sharedPreferences.getString("fourday", "");
        String string11111 = sharedPreferences.getString("fouryu", "");
        String string22222 = sharedPreferences.getString("fourtem", "");
        int string88888 = sharedPreferences.getInt("fourcode", 0);
        fourDateTime.setText(string00000);
        fourxiao(string88888);
        fourTempRange.setText(string22222);
        fourWeatherInfo.setText(string11111);

    }


    /**
     * 绑定控件
     */
    public void bindViews() {
        fenx = (ImageButton) findViewById(R.id.share);
        push = (TextView) findViewById(R.id.push);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        pm1 = (TextView) findViewById(R.id.pm);
        location = (ImageButton) findViewById(R.id.location);
        tianqi = (ImageView) findViewById(R.id.imageView2);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        title_temp = (TextView) findViewById(R.id.title_temp);
        main_location = (TextView) findViewById(R.id.main_location);
        main_temp = (TextView) findViewById(R.id.main_tem);
        main_info = (TextView) findViewById(R.id.main_info);
        nowDateTime = (TextView) findViewById(R.id.weather_now).findViewById(R.id.datetime);
        nowWeatherInfo = (TextView) findViewById(R.id.weather_now).findViewById(R.id.weather_info);
        nowTempRange = (TextView) findViewById(R.id.weather_now).findViewById(R.id.range_temp);
        onexiao = (ImageView) findViewById(R.id.weather_now).findViewById(R.id.imageView);

        twoDateTime = (TextView) findViewById(R.id.weather_second).findViewById(R.id.datetime);
        twoWeatherInfo = (TextView) findViewById(R.id.weather_second).findViewById(R.id.weather_info);
        twoTempRange = (TextView) findViewById(R.id.weather_second).findViewById(R.id.range_temp);
        twoxiao = (ImageView) findViewById(R.id.weather_second).findViewById(R.id.imageView);

        threeDateTime = (TextView) findViewById(R.id.weather_third).findViewById(R.id.datetime);
        threeWeatherInfo = (TextView) findViewById(R.id.weather_third).findViewById(R.id.weather_info);
        threeTempRange = (TextView) findViewById(R.id.weather_third).findViewById(R.id.range_temp);
        threexiao = (ImageView) findViewById(R.id.weather_third).findViewById(R.id.imageView);

        fourDateTime = (TextView) findViewById(R.id.weather_four).findViewById(R.id.datetime);
        fourWeatherInfo = (TextView) findViewById(R.id.weather_four).findViewById(R.id.weather_info);
        fourTempRange = (TextView) findViewById(R.id.weather_four).findViewById(R.id.range_temp);
        fourxiao = (ImageView) findViewById(R.id.weather_four).findViewById(R.id.imageView);
        //设置自定义的toolbar为actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);        //隐藏label

    }

    /**
     * 实时更新天气图标
     * 具体天气现象代码参考：https://docs.seniverse.com/api/start/code.html
     */
    public void tianqi(int code) {
        if (code == 100 || code == 900) {
            tianqi.setImageResource(R.mipmap.sunsun);
        } else if (code == 101 || code == 102 || code == 103) {
            tianqi.setImageResource(R.mipmap.duoyun);
        } else if (code == 104) {
            tianqi.setImageResource(R.mipmap.yingtian);
        } else if (code == 300 || code == 301 || code == 305 || code == 309) {
            tianqi.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 306) {
            tianqi.setImageResource(R.mipmap.zhongyu);
        } else if (code == 307 || code == 308 || code == 310 || code == 311 || code == 312) {
            tianqi.setImageResource(R.mipmap.dayu);
        } else if (code == 302 || code == 303) {
            tianqi.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 404 || code == 405 || code == 406 || code == 401 || code == 407) {
            tianqi.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 313 || code == 400) {
            tianqi.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 402 || code == 403) {
            tianqi.setImageResource(R.mipmap.daxue);
        } else if (code == 503 || code == 504 || code == 507 || code == 508) {
            tianqi.setImageResource(R.mipmap.shachen);
        } else if (code == 502) {
            tianqi.setImageResource(R.mipmap.gg);
        } else if (code == 501 || code == 500) {
            tianqi.setImageResource(R.mipmap.tt);
        } else if (code == 200 || code == 201 || code == 202 || code == 203 || code == 204 || code == 205 || code == 206 || code == 207 || code == 208 || code == 209 || code == 210 || code == 211 || code == 212 || code == 213) {
            tianqi.setImageResource(R.mipmap.dafeng);
        } else if (code == 304) {
            tianqi.setImageResource(R.mipmap.leibing);
        }
    }

    private void threexiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            threexiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            threexiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            threexiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            threexiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14 | code == 12) {
            threexiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            threexiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18 || code == 11) {
            threexiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            threexiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            threexiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            threexiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            threexiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            threexiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            threexiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            threexiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            threexiao.setImageResource(R.mipmap.leibing);
        }
    }


    private void onexiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            onexiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            onexiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            onexiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            onexiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14 || code == 12) {
            onexiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            onexiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18 || code == 11) {
            onexiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            onexiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            onexiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            onexiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            onexiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            onexiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            onexiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            onexiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            onexiao.setImageResource(R.mipmap.leibing);
        }
    }

    private void twoxiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            twoxiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            twoxiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            twoxiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            twoxiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14 || code == 12) {
            twoxiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            twoxiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18 || code == 11) {
            twoxiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            twoxiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            twoxiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            twoxiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            twoxiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            twoxiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            twoxiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            twoxiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            twoxiao.setImageResource(R.mipmap.leibing);
        }
    }

    private void fourxiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            fourxiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            fourxiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            fourxiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            fourxiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14 || code == 12) {
            fourxiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            fourxiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18 || code == 11) {
            fourxiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            fourxiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            fourxiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            fourxiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            fourxiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            fourxiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            fourxiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            fourxiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            fourxiao.setImageResource(R.mipmap.leibing);
        }
    }

    //清空天气列表中的数据
    private void clearWeatherList() {
        WeatherList.clear();
    }
}