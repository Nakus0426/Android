package com.zhz_zsl.glutweather;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.zhz_zsl.glutweather.ui.AAlertDialog;

import java.io.File;

/**
 * 关于页面
 */
public class About extends Activity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置窗口全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.about);
        //创建菜单
        Button zhuye = (Button) findViewById(R.id.zhuye);
        Button fankui = (Button) findViewById(R.id.fankui);
        Button genxin = (Button) findViewById(R.id.genxin);
        Button fenxianng = (Button) findViewById(R.id.fenxiang);
        ImageButton imageButton = (ImageButton) findViewById(R.id.back);

        //设置返回按钮
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(About.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //访问项目主页
        zhuye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转网页
                Uri uri=Uri.parse("https://github.com/Nakus0426/Android/tree/master/GlutWeather");   //指定网址
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                //启动Activit
                startActivity(intent);
        }
        });

        //反馈
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置对话框
                new AAlertDialog(About.this).builder()
                      .setMsg("")
                      .setTitle("反馈信息")
                      .show();
            }
        });

        //检查更新
        genxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置对话框
                new AAlertDialog(About.this).builder().setTitle("已经是最新版本！").show();
            }
        });

        //分享
        fenxianng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享本应用
                Intent intent = new Intent(Intent.ACTION_SEND);
                File file=new File("app-debug.apk");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, file);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //应用选择器
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });
    }



}
