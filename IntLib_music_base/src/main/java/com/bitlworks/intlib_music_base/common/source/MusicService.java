package com.bitlworks.intlib_music_base.common.source;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.bitlworks.intlib_music_base.common.StaticValues;

import java.io.File;
import java.io.IOException;

public class MusicService extends Service {

  private MediaPlayer player;


  @Override
  public IBinder onBind(Intent arg0) {
    return musicBind;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    if (player != null) {
      player.stop();
      player.release();
    }
    return false;
  }

  public MediaPlayer getMedia() {
    return player;
  }

  public void startMusic(int index) {
    if (player == null) {
      initBgm(index);
    } else if (!player.isPlaying()) {
      if (StaticValues.playIndex == index) {
        player.start();
      } else {
        changeSong(index);
      }
    }
  }

  public void stopMusic() {
    if (player != null && player.isPlaying()) {
      player.pause();
    }
  }

  private void initBgm(int index) {
    StaticValues.playIndex = index;
    player = new MediaPlayer();

    try {
      String rootPath = "/mnt/sdcard/";
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
      }

      rootPath += "bitlworks/";
      rootPath += "mobilemusic/";

      final String LocalPath = rootPath + StaticValues.songList.get(index).song_file_name;// StaticValues.photoList.get(0).photo_file_name;
      File file = new File(LocalPath);
      Uri uri = Uri.fromFile(file);
      player.setDataSource(this, uri);
      player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
          Log.e("ab1233", "BaseActivity onPrepared");
          player.start();
          StaticValues.pagerMainActivity.songProgressBar.setProgress(0);
          StaticValues.pagerMainActivity.songProgressBar.setMax(100);
          StaticValues.pagerMainActivity.updateProgressBar();
        }
      });

      player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
          int nextIndex = StaticValues.playIndex + 1;
          if (nextIndex >= StaticValues.songList.size()) {
            nextIndex = 0;
          }
          changeSong(nextIndex);
          try {
            Thread.sleep(500);
          } catch (Exception e) {
            e.getLocalizedMessage();
          }
          Log.i("Completion Listener", "Song Complete");
        }
      });
      player.prepareAsync();

    } catch (IllegalArgumentException | IllegalStateException | SecurityException | IOException e) {
      e.printStackTrace();
    }
  }

  private void changeSong(int index) {
    StaticValues.playIndex = index;
    player.reset();
//        player.release();
//        player = new MediaPlayer();

    try {
      String rootPath = "/mnt/sdcard/";
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
      }

      rootPath += "bitlworks/";
      rootPath += "mobilemusic/";

      final String LocalPath =
          rootPath + StaticValues.songList.get(StaticValues.playIndex).song_file_name;

      File file = new File(LocalPath);
      Uri uri = Uri.fromFile(file);
      player.setDataSource(this, uri);
      player.prepareAsync();
    } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }

  private final IBinder musicBind = new MusicBinder();

  public class MusicBinder extends Binder {
    MusicService getService() {
      return MusicService.this;
    }
  }
}
