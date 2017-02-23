package com.bitlworks.intlib_music_base.source;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;

public class MusicNotification {

  private Context parent;
  private NotificationManager nManager;
  private NotificationCompat.Builder nBuilder;
  private RemoteViews remoteView;

  public MusicNotification(Context parent) {
    this.parent = parent;
    nBuilder = new NotificationCompat.Builder(parent)
        .setContentTitle("Mobile Music")
        .setSmallIcon(R.mipmap.icon)
        .setOngoing(true);

    remoteView = new RemoteViews(parent.getPackageName(), R.layout.layout_notification);
    String title = StaticValues.album == null
        ? "앨범 존재하지 않음" : StaticValues.album.album_name;
    remoteView.setTextViewText(R.id.text_album_name, title);
    setListeners(remoteView);
    nBuilder.setContent(remoteView);
    nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
    nManager.notify(StaticValues.NOTIFICATION_ID, nBuilder.build());
  }

  public void updatePause() {
    remoteView.setImageViewResource(R.id.button_play_song, R.drawable.song_play_button);
    nBuilder.setContent(remoteView);
    nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
    nManager.notify(StaticValues.NOTIFICATION_ID, nBuilder.build());
  }

  public void updatePlay() {
    remoteView.setImageViewResource(R.id.button_play_song, R.drawable.song_pause_button);
    nBuilder.setContent(remoteView);
    nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
    nManager.notify(StaticValues.NOTIFICATION_ID, nBuilder.build());
  }

  public void updateName(String song_name) {
    remoteView.setTextViewText(R.id.text_song_name, song_name);
    nBuilder.setContent(remoteView);
    nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
    nManager.notify(StaticValues.NOTIFICATION_ID, nBuilder.build());
  }

  private void setListeners(RemoteViews view) {
    Intent volume = new Intent();
    volume.setAction("play");
    PendingIntent btn1 = PendingIntent.getBroadcast(parent, 0, volume, PendingIntent.FLAG_UPDATE_CURRENT);
    view.setOnClickPendingIntent(R.id.button_play_song, btn1);

    Intent volume2 = new Intent();
    volume2.setAction("prev");
    PendingIntent btn2 = PendingIntent.getBroadcast(parent, 0, volume2, PendingIntent.FLAG_UPDATE_CURRENT);
    view.setOnClickPendingIntent(R.id.button_song_prev, btn2);

    Intent volume3 = new Intent();
    volume3.setAction("next");
    PendingIntent btn3 = PendingIntent.getBroadcast(parent, 0, volume3, PendingIntent.FLAG_UPDATE_CURRENT);
    view.setOnClickPendingIntent(R.id.button_song_next, btn3);

    Intent volume4 = new Intent();
    volume4.setAction("end");
    PendingIntent btn4 = PendingIntent.getBroadcast(parent, 0, volume4, PendingIntent.FLAG_UPDATE_CURRENT);
    view.setOnClickPendingIntent(R.id.button_exit, btn4);
  }

  public void notificationCancel() {
    nManager.cancel(StaticValues.NOTIFICATION_ID);
  }

}