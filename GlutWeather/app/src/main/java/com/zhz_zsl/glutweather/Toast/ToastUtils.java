package com.zhz_zsl.glutweather.Toast;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.zhz_zsl.glutweather.R;


public class ToastUtils {
    private static Toast toast;
    private static TextView textView;
    /**
     * 自定义样式的消息框
     * 静态toast 只创建一个toast实例 可以实时显示弹出的内容
     */
    public static void showToast(Context context, String text) {
        // 创建前和消失后
        if (toast == null) {
            // 获取LayoutInflater对象
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //创建视图
            View view = inflater.inflate(R.layout.toast, null);
            textView = (TextView) view.findViewById(R.id.tv_toast_text);
            //创建消息框
            toast = new Toast(context);
            //设置消息框的持续时长
            toast.setDuration(Toast.LENGTH_SHORT);
            //设置消息框的背景View
            toast.setView(view);
        }
        //设置消息框的显示内容
        textView.setText(text);
        toast.show();
    }
}
