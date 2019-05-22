package com.zhz_zsl.glutweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 网络状态工具类
 */
public class NetUtil {
    public static final int NETWORK_NONE = 0;   //无连接
    public static final int NETWORK_WIFI = 1;   //使用WIFI
    public static final int NETWORK_MOBILE = 2; //使用移动网络

    public static int getNetWorkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        //获取当前活动网络连接信息
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return NETWORK_NONE;
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return NETWORK_MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NETWORK_WIFI;
        }
        return NETWORK_NONE;
    }
}
