package com.bitlworks.intlib_music_base.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VONewInfo;
import com.bitlworks.intlib_music_base.data.VOPhoto;
import com.bitlworks.intlib_music_base.data.VOSong;
import com.bitlworks.intlib_music_base.data.VOVideo;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataDownloader {
  public DataDownloader(Context c, final Handler mHandler, JsonObject updateList) {

    MusicUtils.initDirectory(c);

    Message msg = new Message();
    msg.what = 0;
    msg.arg1 = StaticValues.videos.size() + StaticValues.newInfos.size() + 1;
    if (updateList.get(StaticValues.METADATA_VERSION).getAsBoolean()) {
      msg.arg1 += 13;
    }
    if (updateList.get(StaticValues.SONG_VERSION).getAsBoolean()) {
      msg.arg1 += StaticValues.songs.size();
    }
    if (updateList.get(StaticValues.PHOTO_VERSION).getAsBoolean()) {
      msg.arg1 += StaticValues.photos.size();
    }
    if (mHandler != null) mHandler.sendMessage(msg);

    ExecutorService executorService = Executors.newFixedThreadPool(15);
    String serverUrl = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/";

    if (updateList.get(StaticValues.METADATA_VERSION).getAsBoolean()) {
      String metadataUrl = serverUrl + "metadata/";
      String metadataPath = MusicUtils.getAlbumPath(c) + "metadata/";
      try {
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.album_cover, "UTF-8"), metadataPath + StaticValues.metadata.album_cover, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.disk_bg, "UTF-8"), metadataPath + StaticValues.metadata.disk_bg, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.review_bg, "UTF-8"), metadataPath + StaticValues.metadata.review_bg, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.setting_bg, "UTF-8"), metadataPath + StaticValues.metadata.setting_bg, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.main_image, "UTF-8"), metadataPath + StaticValues.metadata.main_image, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.title_image, "UTF-8"), metadataPath + StaticValues.metadata.title_image, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.music_player_bg, "UTF-8"), metadataPath + StaticValues.metadata.music_player_bg, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.song_play_icon, "UTF-8"), metadataPath + StaticValues.metadata.song_play_icon, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.song_pause_icon, "UTF-8"), metadataPath + StaticValues.metadata.song_pause_icon, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.song_list_icon, "UTF-8"), metadataPath + StaticValues.metadata.song_list_icon, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.lyrics_icon, "UTF-8"), metadataPath + StaticValues.metadata.lyrics_icon, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.disk_icon, "UTF-8"), metadataPath + StaticValues.metadata.disk_icon, mHandler));
        executorService.execute(new DownloadRunable(metadataUrl + URLEncoder.encode(StaticValues.metadata.mini_icon, "UTF-8"), metadataPath + StaticValues.metadata.mini_icon, mHandler));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }



    for (VOVideo video : StaticValues.videos) {
      String url = null;
      try {
        url = serverUrl + "video/" + URLEncoder.encode(video.photoPath, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      executorService.execute(new DownloadRunable(url, MusicUtils.getAlbumPath(c) + "video/" + video.photoPath, mHandler));
    }

    for (VONewInfo newInfo : StaticValues.newInfos) {
      String url = null;
      try {
        url = serverUrl + "news/" + URLEncoder.encode(newInfo.image_data, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      executorService.execute(new DownloadRunable(url, MusicUtils.getAlbumPath(c) + "news/" + newInfo.image_data, mHandler));
    }

    serverUrl += StaticValues.selectedDisk.disk_id +"/";
    // 디스크 아이콘
    try {
      executorService.execute(new DownloadRunable(serverUrl + URLEncoder.encode(StaticValues.selectedDisk.disk_icon, "UTF-8"), MusicUtils.getAlbumPath(c) + StaticValues.selectedDisk.disk_id +"/" + StaticValues.selectedDisk.disk_icon, mHandler));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    if (updateList.get(StaticValues.SONG_VERSION).getAsBoolean()) {
      for (VOSong song : StaticValues.songs) {
        String url = null;
        try {
          url = serverUrl + "mp3/" + URLEncoder.encode(song.song_file_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        executorService.execute(new DownloadRunable(url, MusicUtils.getAlbumPath(c) + StaticValues.selectedDisk.disk_id +"/mp3/" + song.song_file_name, mHandler));
      }
    }

    if (updateList.get(StaticValues.PHOTO_VERSION).getAsBoolean()) {
      for (VOPhoto photo : StaticValues.photos) {
        String url = null;
        try {
          url = serverUrl + "photo/" + URLEncoder.encode(photo.photo_file_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        executorService.execute(new DownloadRunable(url, MusicUtils.getAlbumPath(c) + StaticValues.selectedDisk.disk_id +"/photo/" + photo.photo_file_name, mHandler));
      }
    }
    executorService.shutdown();
  }
}


