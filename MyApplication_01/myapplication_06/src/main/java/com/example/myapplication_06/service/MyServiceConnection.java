package com.example.myapplication_06.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.example.myapplication_06.service.MusicPlay;

public class MyServiceConnection implements ServiceConnection {
    private MusicPlay MusicPlay;

        /**
         * 连接到服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlay = (MusicPlay) service;
        }

        /**
         * 断开连接
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }



}
