package com.bitlworks.intlib_music_base.source;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.StaticValues;

import java.io.File;
import java.io.IOException;

public class MusicService extends Service implements
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnPreparedListener {

  public boolean isSoundOn = true;
  private Context context;
  private MediaPlayer mediaPlayer;

  MusicService() {}
  MusicService(Context context) {
    this.context = context;
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnErrorListener(this);
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    mediaPlayer.reset();
    return false;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    mediaPlayer.start();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

  public void startMusic(int index) {
    if (StaticValues.playIndex == index) {
      return;
    }
    StaticValues.playIndex = index;
    mediaPlayer.reset();
    String rootPath = MusicUtils.getAlbumPath(context) + StaticValues.selectedDisk.disk_id +"/mp3/";
    File file = new File(rootPath + StaticValues.songs.get(index).song_file_name);
    Uri uri = Uri.fromFile(file);
    try {
      mediaPlayer.setDataSource(context, uri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    mediaPlayer.prepareAsync();
  }

  public void pauseMusic() {
    mediaPlayer.pause();
  }

  public void restartMusic() {
    mediaPlayer.start();
  }

  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  public void seekTo(int position) {
    mediaPlayer.seekTo(position);
  }

  public int getDuration() {
    return mediaPlayer.getDuration();
  }

  public int getCurrentPosition() {
    return mediaPlayer.getCurrentPosition();
  }

  public void releaseMusic() {
    mediaPlayer.release();
    mediaPlayer = null;
  }

  public void setVolume(float leftVolume, float rightVolume) {
    mediaPlayer.setVolume(leftVolume, rightVolume);
  }

  public void soundOn() {
    mediaPlayer.setVolume(1.0f, 1.0f);
    isSoundOn = true;
  }

  public void soundOff() {
    mediaPlayer.setVolume(0f, 0f);
    isSoundOn = false;
  }
}
