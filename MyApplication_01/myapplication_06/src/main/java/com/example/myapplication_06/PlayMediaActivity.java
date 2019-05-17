package com.example.myapplication_06;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication_06.service.MusicPlay;
import com.example.myapplication_06.service.MyService;
import com.example.myapplication_06.service.MyServiceConnection;

import java.io.File;

public class PlayMediaActivity extends Activity implements View.OnClickListener{
        private Button btPlayMusic;
        private Button btPausedContinue;
        private Button btStop;
        private MusicPlay MusicPlay;
        private final String MUSIC_PATH =Environment.getExternalStorageDirectory()+ "/1.mp3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_media);

        initView();
        initListener();

    }

    @Override
    protected void onStart(){
        super.onStart();
        //绑定服务
        if (bindService(new Intent(this, MyService.class), connection, BIND_AUTO_CREATE)) {
            Toast.makeText(this, "音乐播放服务绑定成功", Toast.LENGTH_LONG).show();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        /**
         * 连接到服务
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlay = (MusicPlay) service;
        }

        /**
         * 断开连接
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void initListener() {
        btPlayMusic.setOnClickListener(this);
        btPausedContinue.setOnClickListener(this);
        btStop.setOnClickListener(this);
    }

    private void initView() {
        btPlayMusic = findViewById(R.id.bt_play_music);
        btPausedContinue = findViewById(R.id.bt_paused_continue);
        btStop = findViewById(R.id.bt_stop);
    }


    @Override
    public void onClick(View v) {
        if (MusicPlay == null) {
            Toast.makeText(this, "音乐播放服务连接失败", Toast.LENGTH_LONG).show();
            return;
        }

        switch (v.getId()) {
            case R.id.bt_play_music:
                MusicPlay.playMusic(MUSIC_PATH);
                break;
            case R.id.bt_paused_continue:
                if ("暂停".equals(btPausedContinue.getText().toString())){
                    btPausedContinue.setText("继续");
                    MusicPlay.pausedPlay();
                }else {
                    btPausedContinue.setText("暂停");
                    MusicPlay.continuePlay();
                }
                break;
            case R.id.bt_stop:
                MusicPlay.stopPlay();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑服务
        if (null != connection) {
            unbindService(connection);
        }
    }


}
