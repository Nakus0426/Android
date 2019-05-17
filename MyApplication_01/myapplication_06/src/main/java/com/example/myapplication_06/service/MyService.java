package com.example.myapplication_06.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MyService extends Service {
    private final String TAG = MyService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "绑定成功");
        return new PlayMusicBinder();
    }

    private MediaPlayer mediaPlayer;

    /**
     * 播放音乐行为
     */
    class PlayMusicBinder extends Binder implements MusicPlay {

        public PlayMusicBinder() {
            mediaPlayer = new MediaPlayer();
        }

        /**
         * 播放音乐
         * @param musicPath
         */
        @Override
        public void playMusic(String musicPath) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 暂停播放
         */
        @Override
        public void pausedPlay() {
            mediaPlayer.pause();
        }

        /**
         * 继续播放
         */
        @Override
        public void continuePlay() {
            mediaPlayer.start();
        }

        /**
         * 停止播放
         */
        @Override
        public void stopPlay() {
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "解绑成功");

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            // 释放硬件播放资源
            mediaPlayer.release();
        }
        return super.onUnbind(intent);
    }
}
